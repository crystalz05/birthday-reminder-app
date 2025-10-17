package com.tyro.birthdayreminder.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.tyro.birthdayreminder.BuildConfig
import com.tyro.birthdayreminder.entity.Contact
import com.tyro.birthdayreminder.entity.Notification
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val TAG = "BirthdayReminder"

class NotificationRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getNotifications(userId: String): Result<List<Notification>> {

        return try {
            val remoteNotifications = supabase
                .from("notifications")
                .select { filter { eq("user_id", userId) } }
                .decodeList<Notification>()

            val mapped = remoteNotifications.map { dto ->
                val zoned = LocalDateTime.parse(dto.sent_at).plusHours(1)
                val formattedDatetime = zoned.format(DateTimeFormatter.ofPattern("MMM d, yyyy • HH:mm"))

                Notification(
                    title = dto.title,
                    body = dto.body,
                    sent_at = formattedDatetime,
                    id = dto.id,
                    user_id = dto.user_id,
                    contact_id = dto.contact_id
                )
            }

            Result.success(mapped)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private var channel: RealtimeChannel? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun observeNewNotifications(userId: String, scope: CoroutineScope): Flow<Notification> {
        channel = supabase.channel("notifications-channel")  // Fixed: No "realtime:" prefix

        scope.launch {
            try {
                channel?.subscribe()
            } catch (e: Exception) {
            }
        }

        return channel!!.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "notifications"
        }
            .filter { it is PostgresAction.Insert }
            .mapNotNull { it as? PostgresAction.Insert }
            .mapNotNull { change ->
                val record = change.record as? JsonObject ?: run {
                    return@mapNotNull null
                }
                val recordUserId = record["user_id"]?.jsonPrimitive?.content ?: run {
                    return@mapNotNull null
                }
                if (recordUserId != userId) {
                    return@mapNotNull null
                }

                val rawSentAt = record["sent_at"]?.jsonPrimitive?.content ?: ""
                val zoned = LocalDateTime.parse(rawSentAt).plusHours(1)
                val formattedDatetime = zoned.format(DateTimeFormatter.ofPattern("MMM d, yyyy • HH:mm"))

                val notification = Notification(
                    id = record["id"]?.jsonPrimitive?.content ?: "",
                    title = record["title"]?.jsonPrimitive?.content ?: "",
                    body = record["body"]?.jsonPrimitive?.content ?: "",
                    sent_at = formattedDatetime,  // Fixed: Format consistently
                    user_id = recordUserId,
                    contact_id = record["contact_id"]?.jsonPrimitive?.content ?: ""
                )
                Log.d(TAG, "observeNewNotifications: Mapped new notification: id=${notification.id}, title=${notification.title}")

                notification
            }
    }

    suspend fun clearChannel() {
        Log.d(TAG, "clearChannel: Cleaning up realtime channel")
        channel?.let {
            try {
                supabase.realtime.removeChannel(it)
                Log.d(TAG, "clearChannel: Channel removed successfully")
            } catch (e: Exception) {
                Log.e(TAG, "clearChannel: Failed to remove channel", e)
            }
        }
        channel = null
        Log.d(TAG, "clearChannel: Channel set to null. Cleanup complete.")
    }

    //repository function to delete  notification
    suspend fun deleteNotification(notificationId: String): Result<Notification> {
        return try {
            // Step 1: Fetch the notification first
            val notification = supabase
                .from("notifications")
                .select{
                    filter { eq("id", notificationId) }
                }
                .decodeSingle<Notification>()

            supabase
                .from("notifications")
                .delete {
                    filter { eq("id", notificationId) }
                }

            // Step 3: Return the deleted notification
            Result.success(notification)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendFcmNotification(userId: String): Result<Boolean>{
        return try {
            val url = "https://zfaubbhaecfoxausjaax.supabase.co/functions/v1/welcome-notification"

            val json = JSONObject().apply {
                put("userId", userId)
            }

            val client = OkHttpClient.Builder()
                .callTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .build()

            val mediaType = "application/json".toMediaType()
            val requestBody = json.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", BuildConfig.ANON_KEY)
                .addHeader("Authorization", "Bearer ${BuildConfig.ANON_KEY}")
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                val responseBody = response.body.string()

                Log.e("NotificationRepo", "response is $response")


                if (!response.isSuccessful) {
                    Log.e("NotificationRepo", "sendFcmNotification failed: ${response.code} → $responseBody")
                    return Result.failure(Exception("Supabase function failed: ${response.code}"))
                }

                Log.d("NotificationRepo", "sendFcmNotification success: $responseBody")
                return Result.success(true)
            }

        }catch (e: Exception){
            Log.e("NotificationRepo", "Error sending FCM notification", e)
            Result.failure(e)
        }
    }
}

//val deleted = supabase.from("contacts")
//    .delete {
//        filter { eq("id", contactId) }
//        select() }
//    .decodeSingle<Contact>()