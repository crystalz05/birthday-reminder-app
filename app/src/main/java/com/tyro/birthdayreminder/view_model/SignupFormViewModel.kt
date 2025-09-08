package com.tyro.birthdayreminder.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyro.birthdayreminder.auth.UiEvent
import com.tyro.birthdayreminder.entity.objects.SignupFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupFormViewModel @Inject constructor(): ViewModel(){

    private val _formState = MutableStateFlow(SignupFormState())
    val formState: StateFlow<SignupFormState> = _formState

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onFullNameChange(newName: String){
        _formState.update { it.copy(fullName = newName, fullNameError = null) }
    }

    fun onEmailChange(newEmail: String){
        _formState.update { it.copy(email = newEmail, emailError = null) }
    }

    fun onPasswordChange(newPassword: String){
        _formState.update { it.copy(password = newPassword, passwordError = null) }
    }

    fun onConfirmPasswordChange(newPassword: String){
        _formState.update { it.copy(confirmPassword = newPassword, passwordError = null) }
    }

    fun togglePasswordVisibility(){
        _formState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onTermsCheckChange(){
        _formState.update { it.copy(termsCheck = !it.termsCheck) }
    }

    fun validateForm(): Boolean{

        var isValid = true

        _formState.update { state ->

            var emailError: String? = null
            var passwordError: String? = null
            var fullNameError: String? = null
            var passwordMismatchError: String? = null

            if(state.password.isBlank()){
                emailError = "Email Cannot be Empty"
                isValid = false
                sendUiEvent(UiEvent.ShowSnackBar(emailError))
            }
            if(state.email.isBlank()){
                passwordError = "Password must be at least 6 characters"
                isValid = false
                sendUiEvent(UiEvent.ShowSnackBar(passwordError))
            }
            if(state.fullName.isBlank()){
                fullNameError = "Name cannot be empty"
                isValid = false
                sendUiEvent(UiEvent.ShowSnackBar(fullNameError))
            }
            if(!state.password.equals(state.confirmPassword)){
                passwordMismatchError = "Passwords do not match"
                isValid = false
                sendUiEvent(UiEvent.ShowSnackBar(passwordMismatchError))
            }
            state.copy(
                emailError = emailError,
                passwordError = passwordError,
                fullNameError = fullNameError,
                passwordMismatch = passwordMismatchError
            )
        }
        return isValid
    }

    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}
