package com.tyro.birthdayreminder.repository

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tyro.birthdayreminder.custom_class.compressBitmap
import com.tyro.birthdayreminder.entity.Contact
import com.tyro.birthdayreminder.entity.objects.ContactFormState
import com.tyro.birthdayreminder.entity.objects.ContactPhoto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

import com.tyro.birthdayreminder.BuildConfig
import com.tyro.birthdayreminder.entity.WishItem
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class BirthdayContactRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val supabase: SupabaseClient)
{

    suspend fun saveContact(form: ContactFormState): Result<Contact> {
        return try {
            val contactId = UUID.randomUUID().toString()
            val uid = auth.currentUser?.uid ?: throw Exception("Not Logged in")

            val photoUrl = form.photo?.let { uploadContactPhoto(uid, contactId, it).getOrThrow() }

            val contact = Contact(
                id = contactId,
                userId = uid,
                fullName = form.fullName,
                gender = form.gender,
                photo = photoUrl,
                birthday = form.birthday,
                relationship = form.relationship.ifBlank { null },
                personalNote = form.personalNote.ifBlank { null },
                phoneNumber = form.phoneNumber,
                email = form.email.ifBlank { null },
                instagram = form.instagram.ifBlank { null },
                twitter = form.twitter.ifBlank { null },
                reminders = form.reminders
            )

            val saved = supabase.from("contacts")
                .insert(contact) {
                    select() // ✅ makes Supabase return the inserted row
                }
                .decodeSingle<Contact>()

            Result.success(saved)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateContact(contactId: String, form: ContactFormState): Result<Contact> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Not Logged in")

            val photoUrl = form.photo?.let { uploadContactPhoto(uid, contactId, it).getOrThrow() }

            val existing = supabase
                .from("contacts")
                .select {
                    filter { eq("id", contactId); eq("userId", uid) }
                }
                .decodeSingle<Contact>()

            val updates = existing.copy(
                fullName = form.fullName,
                birthday = form.birthday,
                gender = form.gender,
                relationship = form.relationship.ifBlank { null },
                personalNote = form.personalNote.ifBlank { null },
                phoneNumber = form.phoneNumber,
                email = form.email.ifBlank { null },
                instagram = form.instagram.ifBlank { null },
                twitter = form.twitter.ifBlank { null },
                reminders = form.reminders,
                wishList = form.wishList,
                journal = form.journal.ifBlank { null },
                photo = photoUrl ?: existing.photo
            )

            val updated = supabase
                .from("contacts")
                .update(updates) {
                    filter {
                        eq("userId", uid)
                        eq("id", contactId)
                    }
                    select()
                }
                .decodeSingle<Contact>()

            Result.success(updated)
        } catch (e: Exception) {
            Log.e("updateContact", "Failed to update: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getContacts(): Result<List<Contact>> {
        return try {
            val userId = auth.currentUser?.uid
            val contacts = supabase
                .from("contacts")
                .select{filter {
                    if (userId != null) {
                        eq("userId", userId)
                    }
                }}
                .decodeList<Contact>()
            Result.success(contacts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSingleContact(contactId: String): Result<Contact> {
        return try {
            val contact = supabase
                .from("contacts")
                .select{
                    filter { eq("id", contactId) }
                }
                .decodeSingle<Contact>()
            Result.success(contact)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteContact(contactId: String): Result<Contact>{
        return try {

            val uid = auth.currentUser?.uid ?: throw Exception("Not Logged in")

            val deleted = supabase.from("contacts")
                .delete {
                    filter { eq("id", contactId) }
                    select() }
                .decodeSingle<Contact>()

            deleteContactPhoto(uid, contactId)

            Result.success(deleted)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun deleteAllContact(): Result<List<Contact>>{
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Not Logged in")

            val deleted = supabase.from("contacts")
                .delete {
                    filter { eq("userId", uid) }
                    select() }
                .decodeList<Contact>()

            deleted.forEach { contact ->
                contact.id?.let { deleteContactPhoto(uid, it) }
            }

            Result.success(deleted)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun uploadContactPhoto(uid: String, contactId: String, photo: ContactPhoto): Result<String>{
        return try{
            when (photo) {
                is ContactPhoto.Local -> {
                    val bucket = supabase.storage.from("profile-photos")
                    val path = "profile-photos/$uid/contacts/$contactId/image.jpg"
                    val bytes = compressBitmap(photo.bitmap) // only compress local bitmaps
                    bucket.upload(path, bytes) { upsert = true }
                    val baseUrl = bucket.publicUrl(path)
                    val freshUrl = "$baseUrl?t=${System.currentTimeMillis()}"
                    Result.success(freshUrl)
                }
                is ContactPhoto.Remote -> {
                    Result.success(photo.url)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateContactWishList(contactId: String, newWishList: List<WishItem>): Result<Contact> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Not Logged in")

            // Fetch the existing contact
            val existing = supabase
                .from("contacts")
                .select {
                    filter { eq("id", contactId); eq("userId", uid) }
                }
                .decodeSingle<Contact>()

            // Create an updated copy with new wishList
            val updated = existing.copy(
                wishList = newWishList
            )

            // Update in Supabase
            val result = supabase
                .from("contacts")
                .update(updated) {
                    filter {
                        eq("id", contactId)
                        eq("userId", uid)
                    }
                    select()
                }
                .decodeSingle<Contact>()

            Result.success(result)
        } catch (e: Exception) {
            Log.e("updateContactWishList", "Failed to update wishList: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun deleteContactPhoto(uid: String, contactId: String): Result<Unit> {
        return try {
            val bucket = supabase.storage.from("profile-photos")
            val path = "profile-photos/$uid/contacts/$contactId/image.jpg"

            // ✅ Supabase requires a list of file paths, not a single string
            bucket.delete(listOf(path))

            // ✅ Also clear the photo field in the contact table
            supabase.from("contacts")
                .update(mapOf("photo" to null)) {
                    filter { eq("id", contactId) }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}