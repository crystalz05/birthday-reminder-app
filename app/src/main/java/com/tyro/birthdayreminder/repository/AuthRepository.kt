package com.tyro.birthdayreminder.repository

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.tyro.birthdayreminder.auth.LoginResult
import com.tyro.birthdayreminder.entity.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.tyro.birthdayreminder.BuildConfig

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) {

    suspend fun registerUser(fullName: String, email: String, password: String): Result<FirebaseUser?>{
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user?: return Result.failure(Exception("User registration failed"))

            firebaseUser.sendEmailVerification().await()

            val user = User(
                uid = firebaseUser.uid,
                email = firebaseUser.email?: email,
                fullName = fullName,
            )

            fireStore.collection("users")
                .document(firebaseUser.uid)
                .set(user, SetOptions.merge())
                .await()

            Result.success(firebaseUser)

        }catch (e: Exception){
            Result.failure(Exception(mapAuthError(e)))
        }
    }

    suspend fun resendVerificationEmail(): Result<Unit>{
        return try {
            currentUser()?.sendEmailVerification()?.await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): LoginResult {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user

            if(user!= null){
                if(user.isEmailVerified){
                    LoginResult.Verified(user)
                }else{
                    LoginResult.Unverified(user)
                }
            }else{
                LoginResult.Error("Login failed: users is null")
            }
        }catch (e: Exception){
            LoginResult.Error(mapAuthError(e))
        }
    }
    suspend fun signInWithGoogle(googleIdToken: String): LoginResult {
        return try {
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            val authResult = auth.signInWithCredential(firebaseCredential).await()
            val user = authResult.user

            if (user != null) {
                if (user.isEmailVerified) {
                    LoginResult.Verified(user)
                } else {
                    LoginResult.Unverified(user)
                }
            } else {
                LoginResult.Error("Google Sign-In failed: user is null")
            }
        } catch (e: Exception) {
            LoginResult.Error(mapAuthError(e))
        }
    }

//    suspend fun signInWithGoogle(activity: ComponentActivity): LoginResult {
//
//        return try{
//            val credentialManager = CredentialManager.create(activity)
//
//            val googleIdOption = GetGoogleIdOption.Builder()
//                .setServerClientId(BuildConfig.WEB_CLIENT_ID)
//                .build()
//
//            val request = GetCredentialRequest.Builder()
//                .addCredentialOption(googleIdOption)
//                .build()
//
//            val result = credentialManager.getCredential(activity, request)
//            val googleIdToken = when (val credential = result.credential){
//                is CustomCredential -> {
//                    if(credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
//                        GoogleIdTokenCredential.createFrom(credential.data).idToken
//                    }else null
//                }
//                else -> null
//            }
//
//            if(googleIdToken.isNullOrEmpty()){
//                return LoginResult.Error("Google Sign-In failed: missing token")
//            }
//
//            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
//            val authResult = auth.signInWithCredential(firebaseCredential).await()
//            val user = authResult.user
//
//            if (user != null) {
//                if (user.isEmailVerified) {
//                    LoginResult.Verified(user)
//                } else {
//                    LoginResult.Unverified(user)
//                }
//            } else {
//                LoginResult.Error("Google Sign-In failed: user is null")
//            }
//        } catch (e: Exception) {
//            LoginResult.Error(mapAuthError(e))
//        }
//
//
//    }

    fun signOut(){
        auth.signOut()
    }

    fun currentUser(): FirebaseUser? = auth.currentUser
}

private fun mapAuthError(e: Exception): String{
    return when (e){
        is FirebaseAuthInvalidUserException -> "No account found with this email."
        is FirebaseAuthInvalidCredentialsException -> "Incorrect email or password"
        is FirebaseNetworkException -> "Check your internet connection."
        is FirebaseAuthUserCollisionException -> "Email already in use"
        else -> "Authentication Failed. Please try again."
    }
}

private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)