package com.tyro.birthdayreminder.repository

import android.graphics.Bitmap
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
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.messaging.FirebaseMessaging
import com.tyro.birthdayreminder.BuildConfig
import com.tyro.birthdayreminder.custom_class.compressBitmap
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import java.io.ByteArrayOutputStream

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val supabase: SupabaseClient
) {

    suspend fun registerUser(fullName: String, email: String, password: String): Result<FirebaseUser?>{
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user?: return Result.failure(Exception("User registration failed"))

            firebaseUser.sendEmailVerification().await()

            val token = com.google.firebase.messaging.FirebaseMessaging.getInstance().token.await()
            saveFcmToken(firebaseUser.uid, token)

            val user = User(
                uid = firebaseUser.uid,
                email = firebaseUser.email?: email,
                fullName = fullName,
                newUser = true
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


    suspend fun verifyUserAccount(password: String): Result<Boolean> {
        return try {
            val user = auth.currentUser ?: return Result.failure(Exception("No user logged in"))
            val credential = user.email?.let {
                EmailAuthProvider.getCredential(it, password)
            }
            credential?.let { credentialValue-> user.reauthenticate(credentialValue).await() }
            Result.success(true)
        } catch (e: Exception) {
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

            val token = com.google.firebase.messaging.FirebaseMessaging.getInstance().token.await()
            if (user != null) {
                saveFcmToken(user.uid, token)
            }

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

            val token = com.google.firebase.messaging.FirebaseMessaging.getInstance().token.await()
            if (user != null) {
                saveFcmToken(user.uid, token)
            }

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

    suspend fun updateUserData(uid: String, updates: Map<String, Any>): Result<Unit>{
        return try{
            fireStore.collection("users")
                .document(uid)
                .update(updates)
                .await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun saveFcmToken(uid: String, fcmToken: String): Result<Unit> {
        return try {
            supabase.from("user_tokens")
                .upsert(
                    listOf(
                        mapOf(
                            "user_id" to uid,
                            "fcm_token" to fcmToken
                        )
                    )
                )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFcmToken(uid: String): Result<Unit> {
        return try {
            // Get the current device's FCM token
            val currentToken = FirebaseMessaging.getInstance().token.await()
            if (currentToken.isNullOrBlank()) {
                return Result.failure(Exception("No FCM token found for this device"))
            }

            // Delete only this user's current device token
            supabase.from("user_tokens")
                .delete {
                    filter {
                        eq("user_id", uid)
                        eq("fcm_token", currentToken)
                    }
                }

            FirebaseMessaging.getInstance().deleteToken()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadProfilePhoto(uid: String, bitmap: Bitmap): Result<String>{
        return try{
            val bucket = supabase.storage.from("profile-photos")
            val path = "profile-photos/$uid/profile.jpg"

            val bytes: ByteArray = compressBitmap(bitmap)

            bucket.upload(path, bytes){
                upsert = true
            }
            val baseUrl = bucket.publicUrl(path)

            val freshUrl = "$baseUrl?t=${System.currentTimeMillis()}" // 👈 bust cache
            fireStore.collection("users")
                .document(uid)
                .update(
                    mapOf(
                        "profilePhotoUrl" to freshUrl,
                        "updatedAt" to System.currentTimeMillis()
                    )
                )
                .await()
            Result.success(freshUrl)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    fun signOut(){
        auth.signOut()
    }

    fun getUid(): String? {
        return auth.uid
    }


    suspend fun reAuthenticateAndDelete(password: String): Result<Boolean> {
        return try {
            val user = auth.currentUser ?: return Result.failure(Exception("No user logged in"))
            val credential = user.email?.let { EmailAuthProvider.getCredential(it, password) }

            // Reauthenticate
            if (credential != null) {
                user.reauthenticate(credential).await()
            }

            deleteProfilePhoto(user.uid)
            fireStore.collection("users").document(user.uid).delete().await()

            user.delete().await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun currentUser(): FirebaseUser? = auth.currentUser

    suspend fun getUserData(uid: String): DocumentSnapshot{
        return fireStore.collection("users")
            .document(uid)
            .get().await()
    }

    suspend fun deleteProfilePhoto(uid: String): Result<Unit> {
        return try {
            val bucket = supabase.storage.from("profile-photos")
            val path = "profile-photos/$uid/profile.jpg"

            // Delete from Supabase storage
            bucket.delete(path)

            // Remove reference from Firestore
            fireStore.collection("users")
                .document(uid)
                .update(mapOf("profilePhotoUrl" to null))
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
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