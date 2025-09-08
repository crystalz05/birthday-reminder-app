package com.tyro.birthdayreminder.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyro.birthdayreminder.auth.UiEvent
import com.tyro.birthdayreminder.entity.objects.LoginFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginFormViewModel @Inject constructor(): ViewModel() {

    private val _formState = MutableStateFlow(LoginFormState())
    val formState: StateFlow<LoginFormState> = _formState

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEmailChange(newEmail: String){
       _formState.update { it.copy(email = newEmail, emailError = null) }
    }

    fun onPasswordChange(newPassword: String){
        _formState.update { it.copy(password = newPassword, passwordError = null) }
    }

    fun togglePasswordVisibility(){
        _formState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun validateForm(): Boolean{
        var isValid = true

        _formState.update { state ->
            var emailError: String? = null
            var passwordError: String? = null

            if(state.email.isBlank()){
                emailError = "Email cannot be empty"
                isValid =  false
                sendUiEvent(UiEvent.ShowSnackBar(emailError))
            }
            if(state.password.length < 6){
                passwordError = "Password must b at least 6 characters"
                isValid = false
                sendUiEvent(UiEvent.ShowSnackBar(passwordError))
            }

            state.copy(emailError = emailError, passwordError = passwordError)
        }
        return isValid
    }
    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}

