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
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

class BirthdayContactRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
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
                relationship = form.relationship.ifBlank { null },
                personalNote = form.personalNote.ifBlank { null },
                phoneNumber = form.phoneNumber,
                email = form.email.ifBlank { null },
                instagram = form.instagram.ifBlank { null },
                twitter = form.twitter.ifBlank { null },
                reminders = form.reminders,
                // only overwrite photo if a new one exists
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
            val deleted = supabase.from("contacts")
                .delete {
                    filter { eq("id", contactId) }
                    select() }
                .decodeSingle<Contact>()
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

}