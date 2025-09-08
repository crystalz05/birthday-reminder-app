package com.tyro.birthdayreminder.view_model

import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.rpc.context.AttributeContext.Auth
import com.tyro.birthdayreminder.BuildConfig
import com.tyro.birthdayreminder.auth.AuthState
import com.tyro.birthdayreminder.auth.LoginResult
import com.tyro.birthdayreminder.auth.UiEvent
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val credentialManager = CredentialManager.create(context)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun registerUser(fullName: String, email: String, password: String){
        _authState.update { AuthState.Loading }

        viewModelScope.launch {
            val result = authRepository.registerUser(fullName, email.trim(), password)

            result.onSuccess { user ->
                if (user != null) {
                    if(user.isEmailVerified){
                        _authState.update { AuthState.Verified(user) }
                    } else{
                        _authState.update { AuthState.Unverified(user) }
                        _uiEvent.send(UiEvent.ShowSnackBar("Please Verify your account"))
                        _uiEvent.send(UiEvent.Navigate(Screen.EmailVerification.route))
                    }
                }else{
                    _authState.update { AuthState.Error("Registration Failed: user is null") }
                }
            }.onFailure { e ->
                _authState.update { AuthState.Error(e.message ?: "Unknown error") }
                _uiEvent.send(UiEvent.ShowSnackBar(e.message.toString()))
            }
        }
    }

    fun resendEmailVerification(){
        viewModelScope.launch {
            val result = authRepository.resendVerificationEmail()

            result.onSuccess {
                _uiEvent.send(UiEvent.ShowSnackBar("Verification email sent"))
            }.onFailure {e ->
                _uiEvent.send(UiEvent.ShowSnackBar(e.message.toString()))
            }
        }
    }

    fun login(email: String, password: String){
        _authState.update { AuthState.Loading }

        viewModelScope.launch {
            when(val result = authRepository.login(email.trim(), password)){

                is LoginResult.Verified -> {
                    _authState.update { AuthState.Verified(result.user) }
                    _uiEvent.send(UiEvent.Navigate(Screen.Home.route))
                }
                is LoginResult.Unverified -> {
                    _authState.update { AuthState.Unverified(result.user) }
                    _uiEvent.send(UiEvent.ShowSnackBar("Please Verify your account"))
                    _uiEvent.send(UiEvent.Navigate(Screen.EmailVerification.route))
                }
                is LoginResult.Error -> {
                    _authState.update { AuthState.Error(result.message) }
                    _uiEvent.send(UiEvent.ShowSnackBar(result.message))
                }
            }


        }
    }

    fun signInWithGoogle(activity: Activity) {
        viewModelScope.launch {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(
                    request = request,
                    context = activity
                )

                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleCred = GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleCred.idToken
                    if (idToken.isNotEmpty()) {
                        handleLoginResult(authRepository.signInWithGoogle(idToken))
                    }
                }
            } catch (e: Exception) {
                handleLoginResult(LoginResult.Error(e.localizedMessage ?: "Google sign-in failed"))
            }
        }
    }

    private suspend fun handleLoginResult(result: LoginResult) {
        when (result) {
            is LoginResult.Verified -> {
                _authState.update { AuthState.Verified(result.user) }
                _uiEvent.send(UiEvent.Navigate(Screen.Home.route))
            }
            is LoginResult.Unverified -> {
                _authState.update { AuthState.Unverified(result.user) }
                _uiEvent.send(UiEvent.ShowSnackBar("Please verify your account"))
                _uiEvent.send(UiEvent.Navigate(Screen.EmailVerification.route))
            }
            is LoginResult.Error -> {
                _authState.update { AuthState.Error(result.message) }
                _uiEvent.send(UiEvent.ShowSnackBar(result.message))
            }
        }
    }

    fun signOut(){
        _authState.update { AuthState.Loading }
        viewModelScope.launch {
            authRepository.signOut()
            _authState.update { AuthState.LoggedOut }
            _uiEvent.send(UiEvent.Navigate(Screen.Login.route))
            _uiEvent.send(UiEvent.ShowSnackBar("Signed out successfully"))
        }
    }

    fun refreshEmailVerification(){
        viewModelScope.launch {
            while(isActive){
                val user = authRepository.currentUser()
                user?.reload()?.await()
                if(user?.isEmailVerified == true){
                    _authState.update { AuthState.Verified(user) }
                    _uiEvent.send(UiEvent.Navigate(Screen.Home.route))
                    break
                }
                delay(5_000)
            }
        }
    }

    fun fetchCurrentUser(){
        val user = authRepository.currentUser()
        _authState.update {
            when {
                user == null -> AuthState.LoggedOut
                user.isEmailVerified -> AuthState.Verified(user)
                else -> AuthState.Unverified(user)
            }
        }
    }
}