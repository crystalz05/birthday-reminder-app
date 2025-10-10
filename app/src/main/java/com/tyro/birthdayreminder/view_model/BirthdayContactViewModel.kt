package com.tyro.birthdayreminder.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyro.birthdayreminder.auth.AuthState
import com.tyro.birthdayreminder.auth.ContactOperationState
import com.tyro.birthdayreminder.auth.UiEvent
import com.tyro.birthdayreminder.entity.Contact
import com.tyro.birthdayreminder.entity.WishItem
import com.tyro.birthdayreminder.entity.objects.ContactFormState
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.repository.BirthdayContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BirthdayContactViewModel @Inject constructor(
    private val birthdayContactRepository: BirthdayContactRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    private val _giftItems = MutableStateFlow<List<WishItem>>(emptyList())
    val giftItems: StateFlow<List<WishItem>> = _giftItems

    private val _contactsDetail = MutableStateFlow<Contact?>(null)
    val contactDetail: StateFlow<Contact?> = _contactsDetail

    private val _contactOperationState = MutableStateFlow<ContactOperationState>(ContactOperationState.Idle)
    val contactOperationState: StateFlow<ContactOperationState> = _contactOperationState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        loadContacts()
    }

    fun loadContacts(showLoading : Boolean = true, pullDown: Boolean = false){
        viewModelScope.launch {
            if(pullDown)_isRefreshing.value = true
            if(showLoading) _contactOperationState.update { ContactOperationState.Loading }
            val result = birthdayContactRepository.getContacts()

            result.onSuccess { contacts ->
                _isRefreshing.value = false
                _contacts.update { contacts }
                _contactOperationState.update { ContactOperationState.Success(contacts) }
            }.onFailure {
                _isRefreshing.value = false
                _uiEvent.send(UiEvent.ShowSnackBar("Failed to load contacts"))
                _contactOperationState.update { ContactOperationState.Error("Failed to load contact") }
            }
        }
    }

    fun loadSingleContact(contactId: String, showLoading : Boolean = true, pullDown: Boolean = false){
        viewModelScope.launch {
            if(pullDown)_isRefreshing.value = true
            if(showLoading) _contactOperationState.update { ContactOperationState.Loading }
            val result = birthdayContactRepository.getSingleContact(contactId)

            result.onSuccess { contact ->
                _isRefreshing.value = false
                _contactsDetail.update { contact }
                _giftItems.value = contact.wishList!!
                _contactOperationState.update { ContactOperationState.Success(contact) }
            }.onFailure {
                _isRefreshing.value = false
                _uiEvent.send(UiEvent.ShowSnackBar("Failed to load Contact"))
                _contactOperationState.update { ContactOperationState.Error("Failed to load Contact") }
            }
        }
    }

    fun clearContactDetail(){
        _contactsDetail.update { null }
    }

    fun addContact(contact: ContactFormState){
        viewModelScope.launch {
            _contactOperationState.update { ContactOperationState.Loading }

            val result = birthdayContactRepository.saveContact(contact)

            result.onSuccess { contact ->
                _uiEvent.send(UiEvent.ShowSnackBar("Birthday Contact Added"))
                _uiEvent.send(UiEvent.Navigate(Screen.Home.route))
                _contactOperationState.update { ContactOperationState.Success(contact) }
            }.onFailure { e->
                _contactOperationState.update { ContactOperationState.Error(e.message ?: "Error saving contact") }
            }
        }
    }

    fun deleteContact(contactId: String){
        viewModelScope.launch {
            _contactOperationState.update { ContactOperationState.Loading }

            val result = birthdayContactRepository.deleteContact(contactId)

            result.onSuccess { contact ->
                _uiEvent.send(UiEvent.NavigateBack)
                _uiEvent.send(UiEvent.ShowSnackBar("${contact.fullName} deleted"))
                _contactOperationState.update { ContactOperationState.Success(contact) }
            }.onFailure { e->
                _contactOperationState.update { ContactOperationState.Error(e.message ?: "Error deleting contact") }
            }
        }
    }

    fun deleteAllContacts(){
        viewModelScope.launch {
            birthdayContactRepository.deleteAllContact()
        }
    }

    fun updateContact(contactId: String, contact: ContactFormState, softSave: Boolean = false){
        viewModelScope.launch {
            if(!softSave)_contactOperationState.update { ContactOperationState.Loading }

            val result = birthdayContactRepository.updateContact(contactId, contact)

            result.onSuccess { contact ->
                _contactsDetail.update { contact }
                if(!softSave)_uiEvent.send(UiEvent.ShowSnackBar("Birthday Contact Updated"))
                if(!softSave)_uiEvent.send(UiEvent.Navigate(Screen.BirthDayDetail.passContactId(contact.id)))
                _contactOperationState.update { ContactOperationState.Success(contact) }
            }.onFailure { e->
                _contactOperationState.update { ContactOperationState.Error(e.message ?: "Error saving contact") }
            }
        }
    }

    fun deleteContactPhoto(userId: String, contactId: String, onReload: (()-> Unit)? = null) {
        viewModelScope.launch {
            val result = birthdayContactRepository.deleteContactPhoto(userId, contactId)
            result.onSuccess {
                _uiEvent.send(UiEvent.ShowSnackBar("Profile photo removed"))
                _contactOperationState.update { ContactOperationState.Idle }
                onReload?.invoke()
            }.onFailure { e ->
                _contactOperationState.update {
                    ContactOperationState.Error(e.message ?: "Error removing photo")
                }
            }
        }
    }

    fun updateWishList(contactId: String, newWishList: List<WishItem>) {
        viewModelScope.launch {
            birthdayContactRepository.updateContactWishList(contactId, newWishList)
        }
    }

    fun setGiftItems(items: List<WishItem>) {
        _giftItems.value = items
    }

}