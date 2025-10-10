package com.tyro.birthdayreminder.view_model

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.imageLoader
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
import kotlinx.coroutines.flow.single
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

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> = _imageUrl

    private val _fullName = MutableStateFlow<String?>(null)
    val fullName: StateFlow<String?> = _fullName

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        fetchCurrentUser()
    }
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

    fun verifyUserAccount(password: String){
        viewModelScope.launch {

            val result = authRepository.verifyUserAccount(password)

            result.onSuccess {
                _uiEvent.send(UiEvent.ShowSnackBar("Verification Successful"))
                _uiEvent.send(UiEvent.Navigate(Screen.ProfileEdit.route))
            }.onFailure {e->
                _uiEvent.send(UiEvent.ShowSnackBar(e.message.toString()))
            }
        }
    }

    fun deleteAccount(password: String, onSuccess: (()-> Unit)? = null){
        viewModelScope.launch {
            _authState.update { AuthState.Loading }
            val result = authRepository.reAuthenticateAndDelete(password)

            result.onSuccess {
                clearUserData()
                _authState.update { AuthState.LoggedOut }
                _uiEvent.send(UiEvent.ShowSnackBar("Account deleted Successfully"))
                onSuccess?.invoke()
            }.onFailure { e->
                _authState.update { AuthState.Error("Failed to delete account") }
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

    fun updateUserFullName(newName: String){
        val currentUser = authRepository.currentUser()

        if(currentUser != null){
            viewModelScope.launch {
                val result = authRepository.updateUserData(
                    currentUser.uid,
                    mapOf("fullName" to newName, "updatedAt" to System.currentTimeMillis())
                )
                result.onSuccess {
                    _fullName.update { newName } // ✅ immediate update
                    _uiEvent.send(UiEvent.ShowSnackBar("Name updated successfully"))
                    _uiEvent.send(UiEvent.Navigate(Screen.Home.route))
                    _authState.update { AuthState.Success(currentUser) }
                }.onFailure { e ->
                    _uiEvent.send(UiEvent.ShowSnackBar("Update failed: ${e.message}"))
                }
            }
        }
    }

    fun updateProfilePhoto(bitmap: Bitmap){
        val currentUser = authRepository.currentUser()
        if(currentUser != null){
            _authState.update { AuthState.Loading }
            viewModelScope.launch {
                val result = authRepository.uploadProfilePhoto(currentUser.uid, bitmap)
                result.onSuccess { newUrl ->
                    _imageUrl.update { newUrl }
                    _uiEvent.send(UiEvent.ShowSnackBar("Profile photo updated"))
                    _authState.update { AuthState.Idle }
                }.onFailure { e ->
                    _uiEvent.send(UiEvent.ShowSnackBar("Update failed: ${e.message}"))
                    _authState.update { AuthState.Idle }
                }
            }
        }
    }

    fun saveFcmToken(){
        viewModelScope.launch {
            val token = com.google.firebase.messaging.FirebaseMessaging.getInstance().token.await()
            authRepository.getUid()?.let { authRepository.saveFcmToken(it, token) }
        }
    }

    fun signOut(context: Context){
        _authState.update { AuthState.Loading }
        viewModelScope.launch {
            val uid = authRepository.getUid()
            if (uid != null) {
                authRepository.deleteFcmToken(uid)
            }

            authRepository.signOut()
            clearUserData()
            clearImageCache(context)

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

    fun loadProfilePhoto(){
        val uid = authRepository.currentUser()?.uid ?: return
        viewModelScope.launch {
            val snapshot = authRepository.getUserData(uid)
            _imageUrl.update { snapshot.getString("profilePhotoUrl") }
            _fullName.update { snapshot.getString("fullName") }
        }
    }

    fun fetchCurrentUser(){
        val user = authRepository.currentUser()
        _authState.update {
            when {
                user == null -> AuthState.LoggedOut
                user.isEmailVerified -> {
                    _email.update { user.email }
                    loadProfilePhoto()
                    AuthState.Verified(user)
                }
                else -> AuthState.Unverified(user)
            }
        }
    }

    fun deleteProfilePhoto(){
        val currentUser = authRepository.currentUser()
        if(currentUser != null){
            viewModelScope.launch {

                _authState.update { AuthState.Loading }

                val uid = authRepository.getUid()

                // 🧹 Always try to remove FCM token before deleting account
                if (uid != null) {
                    authRepository.deleteFcmToken(uid)
                }

                val result = authRepository.deleteProfilePhoto(currentUser.uid)
                result.onSuccess {
                    _imageUrl.update { null } // clear local state
                    _uiEvent.send(UiEvent.ShowSnackBar("Profile photo removed"))
                    _authState.update { AuthState.Idle }
                }.onFailure { e ->
                    _uiEvent.send(UiEvent.ShowSnackBar("Failed to remove photo: ${e.message}"))
                    _authState.update { AuthState.Idle }
                }
            }
        }
    }

    private fun clearUserData() {
        _imageUrl.value = null
        _fullName.value = null
        _email.value = null
    }

    private fun clearImageCache(context: Context) {
        context.imageLoader.memoryCache?.clear()
        context.imageLoader.diskCache?.clear()
    }

}