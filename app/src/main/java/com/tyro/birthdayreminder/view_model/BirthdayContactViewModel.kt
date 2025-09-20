package com.tyro.birthdayreminder.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyro.birthdayreminder.auth.ContactOperationState
import com.tyro.birthdayreminder.auth.UiEvent
import com.tyro.birthdayreminder.entity.Contact
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
                _contactOperationState.update { ContactOperationState.Success(contacts) }

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
                _uiEvent.send(UiEvent.ShowSnackBar("${contact.fullName} deleted"))
                _uiEvent.send(UiEvent.Navigate(Screen.Home.route))
                _contactOperationState.update { ContactOperationState.Success(contact) }
            }.onFailure { e->
                _contactOperationState.update { ContactOperationState.Error(e.message ?: "Error deleting contact") }
            }
        }
    }

    fun updateContact(contactId: String, contact: ContactFormState){
        viewModelScope.launch {
            _contactOperationState.update { ContactOperationState.Loading }

            val result = birthdayContactRepository.updateContact(contactId, contact)

            result.onSuccess { contact ->
                _uiEvent.send(UiEvent.ShowSnackBar("Birthday Contact Updated"))
                _uiEvent.send(UiEvent.Navigate(Screen.BirthDayDetail.passContactId(contact.id)))
                _contactOperationState.update { ContactOperationState.Success(contact) }
            }.onFailure { e->
                _contactOperationState.update { ContactOperationState.Error(e.message ?: "Error saving contact") }
            }
        }
    }

}