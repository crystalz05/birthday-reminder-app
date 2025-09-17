package com.tyro.birthdayreminder.repository

import android.graphics.Bitmap
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

    private val contacts = supabase.postgrest["contacts"]

    suspend fun addContact(contact: Contact) {
        contacts.insert(contact)
    }

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
//    suspend fun saveContact(form: ContactFormState): Result<Contact>{
//        return try{
//            val contactId = UUID.randomUUID().toString()
//            val uid = auth.currentUser?.uid?: throw Exception("Not Logged in")
//
//            val photoUrl = form.photo?.let { uploadContactPhoto(uid, contactId, it).getOrThrow() }
//
//
//            val contact = Contact(
//                id = contactId,
//                userId = uid,
//                fullName = form.fullName,
//                photo = photoUrl,
//                birthday = form.birthday,
//                relationship = form.relationship.ifBlank { null },
//                personalNote = form.personalNote.ifBlank { null },
//                phoneNumber = form.phoneNumber,
//                email = form.email.ifBlank { null },
//                instagram = form.instagram.ifBlank { null },
//                twitter = form.twitter.ifBlank { null },
//                reminders = form.reminders
//            )
//
//            supabase.from("contacts")
//                .insert(contact).decodeSingle<Contact>()
//
//            Result.success(contact)
//        }catch (e: Exception){
//            Result.failure(e)
//        }
//    }

    suspend fun updateContact(contactId: String, uid: String, form: ContactFormState): Result<Contact> {
        return try {
            val photoUrl = form.photo?.let {
                uploadContactPhoto(uid, contactId, it)
            }

            val updates = mapOf(
                "fullName" to form.fullName,
                "photo" to photoUrl,
                "birthday" to form.birthday,
                "relationship" to form.relationship.ifBlank { null },
                "personalNote" to form.personalNote.ifBlank { null },
                "phoneNumber" to form.phoneNumber,
                "email" to form.email.ifBlank { null },
                "instagram" to form.instagram.ifBlank { null },
                "twitter" to form.twitter.ifBlank { null },
                "reminders" to form.reminders,
                "updatedAt" to System.currentTimeMillis()
            )

            val updated = supabase
                .from("contacts")
                .update(updates) {
                    filter {
                        eq("id", contactId)
                    }
                }
                .decodeSingle<Contact>()

            Result.success(updated)
        } catch (e: Exception) {
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