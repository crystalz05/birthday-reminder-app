package com.tyro.birthdayreminder.view_model

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyro.birthdayreminder.auth.UiEvent
import com.tyro.birthdayreminder.entity.Contact
import com.tyro.birthdayreminder.entity.WishItem
import com.tyro.birthdayreminder.entity.objects.ContactFormState
import com.tyro.birthdayreminder.entity.objects.ContactPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactFormViewModel @Inject constructor(): ViewModel() {

    private val _formState = MutableStateFlow(ContactFormState())
    val formState: StateFlow<ContactFormState> = _formState

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun fullNameChange(newName: String){
        _formState.update { it.copy(fullName = newName, fullNameError = null) }
    }

    fun onGenderChange(newGender: String){
        _formState.update { it.copy(gender = newGender, genderError = null) }
    }

    fun onBirthDateChange(newBirthDate: String){
        _formState.update { it.copy(birthday = newBirthDate, birthDateError = null) }
    }

    fun onRelationshipChange(newRelationship: String){
        _formState.update { it.copy(relationship = newRelationship) }
    }

    fun onPersonalNoteChange(newNote: String){
        _formState.update { it.copy(personalNote = newNote) }
    }

    fun onPhoneNumberChange(newPhoneNumber: String){
        _formState.update { it.copy(phoneNumber = newPhoneNumber, phoneNumberError = null) }
    }

    fun onEmailChange(newEmail: String){
        _formState.update { it.copy(email = newEmail) }
    }

    fun onInstagramChange(newInstagram: String){
        _formState.update { it.copy(instagram = newInstagram) }
    }

    fun onTwitterChange(newTwitter: String){
        _formState.update { it.copy(twitter = newTwitter) }
    }

    fun onImagePicked(bitmap: Bitmap) {
        _formState.update { it.copy(photo = ContactPhoto.Local(bitmap)) }
    }

    fun setPhotoUrl(photo: Bitmap) {
        _formState.update { it.copy(photo = ContactPhoto.Local(photo)) }
    }

    fun onJournalChange(newJournal: String){
        _formState.update { it.copy(journal = newJournal) }
    }

    fun on2WeeksToggled(){
        _formState.update { state ->
            val updateReminders = state.reminders.toMutableList()
            if("2w" in updateReminders){
                updateReminders.remove("2w")
            }else{
                updateReminders.add("2w")
            }
            state.copy(reminders = updateReminders)
        }
    }

    fun on1WeekToggled(){
        _formState.update { state ->
            val updateReminders = state.reminders.toMutableList()
            if("1w" in updateReminders){
                updateReminders.remove("1w")
            }else{
                updateReminders.add("1w")
            }
            state.copy(reminders = updateReminders)
        }
    }

    fun resetForm(){
        _formState.update { ContactFormState() }
    }

    fun on3DaysToggled(){
        _formState.update { state ->
            val updateReminders = state.reminders.toMutableList()
            if("3d" in updateReminders){
                updateReminders.remove("3d")
            }else{
                updateReminders.add("3d")
            }
            state.copy(reminders = updateReminders)
        }
    }

    fun onDayToggled(){
        _formState.update { state ->
            val updateReminders = state.reminders.toMutableList()
            if("0d" in updateReminders){
                updateReminders.remove("0d")
            }else{
                updateReminders.add("0d")
            }
            state.copy(reminders = updateReminders)
        }
    }

    fun setFromContact(contact: Contact) {
        _formState.update {
            it.copy(
                fullName = contact.fullName,
                gender = contact.gender ?: "",
                photo = contact.photo?.let { url -> ContactPhoto.Remote(url) },
                birthday = contact.birthday,
                relationship = contact.relationship ?: "",
                personalNote = contact.personalNote ?: "",
                phoneNumber = contact.phoneNumber,
                email = contact.email ?: "",
                instagram = contact.instagram ?: "",
                twitter = contact.twitter ?: "",
                reminders = contact.reminders,
                journal = contact.journal ?: "",
                wishList = contact.wishList ?: emptyList()
            )
        }
    }

    fun toggleWishItem(index: Int): List<WishItem> {
        var updated: List<WishItem> = emptyList()
        _formState.update { state ->
            updated = state.wishList.mapIndexed { i, item ->
                if (i == index) item.copy(selected = !item.selected) else item
            }
            state.copy(wishList = updated)
        }
        return updated
    }

//    fun on2WeeksToggled(){
//        _formState.update { state ->
//            val updateReminders = state.reminders.toMutableList()
//            if("2w" in updateReminders){
//                updateReminders.remove("2w")
//            }else{
//                updateReminders.add("2w")
//            }
//            state.copy(reminders = updateReminders)
//        }
//    }

    fun validateForm(): Boolean{
        var isValid = true

        _formState.update { state ->
            var fullNameError: String? = null
            var birthDateError: String? = null
            var phoneNumberError: String? = null
            var genderError: String? = null

            if(state.fullName.isBlank()){
                fullNameError = "Name cannot be empty"
                isValid =  false
                sendUiEvent(UiEvent.ShowSnackBar(fullNameError))
            }
            if(state.gender.isBlank()){
                genderError = "Gender required"
                isValid = false
                sendUiEvent(UiEvent.ShowSnackBar(genderError))
            }
            if(state.birthday.isBlank()){
                birthDateError = "Birth date cannot be empty"
                isValid = false
                sendUiEvent(UiEvent.ShowSnackBar(birthDateError))
            }
            if(state.phoneNumber.isBlank()){
                phoneNumberError = "Phone number cannot be empty"
                isValid = false
                sendUiEvent(UiEvent.ShowSnackBar(phoneNumberError))
            }
            val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
            if(state.email.isNotBlank() && emailRegex.matches(state.email) ){
                phoneNumberError = "Invalid email address"
                isValid = false
                sendUiEvent(UiEvent.ShowSnackBar(phoneNumberError))
            }
            if(state.phoneNumber.length!=11 || !state.phoneNumber.all { it.isDigit() }){
                phoneNumberError = "Invalid Phone number"
                isValid = false
                sendUiEvent(UiEvent.ShowSnackBar(phoneNumberError))
            }
            state.copy(fullNameError = fullNameError, birthDateError = birthDateError, phoneNumberError = phoneNumberError, genderError = genderError)
        }
        return isValid
    }
    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}