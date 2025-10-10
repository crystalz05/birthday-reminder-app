package com.tyro.birthdayreminder.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
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
        Log.d(TAG, "getNotifications: Fetching for userId=$userId")

        return try {
            val remoteNotifications = supabase
                .from("notifications")
                .select { filter { eq("user_id", userId) } }
                .decodeList<Notification>()
            Log.d(TAG, "getNotifications: Fetched ${remoteNotifications.size} raw notifications")

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
            Log.d(TAG, "getNotifications: Mapped to ${mapped.size} formatted notifications. Success.")

            Result.success(mapped)
        } catch (e: Exception) {
            Log.e(TAG, "getNotifications: Failed to fetch notifications for userId=$userId", e)
            Result.failure(e)
        }
    }

    private var channel: RealtimeChannel? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun observeNewNotifications(userId: String, scope: CoroutineScope): Flow<Notification> {
        Log.d(TAG, "observeNewNotifications: Starting realtime observer for userId=$userId")
        channel = supabase.channel("notifications-channel")  // Fixed: No "realtime:" prefix

        scope.launch {
            try {
                Log.d(TAG, "observeNewNotifications: Subscribing to channel")
                channel?.subscribe()
                Log.d(TAG, "observeNewNotifications: Channel subscribed successfully")
            } catch (e: Exception) {
                Log.e(TAG, "observeNewNotifications: Failed to subscribe to channel", e)
                // Don't rethrow—let the flow handle it gracefully
            }
        }

        return channel!!.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "notifications"
        }
            .filter { it is PostgresAction.Insert }
            .mapNotNull { it as? PostgresAction.Insert }
            .mapNotNull { change ->
                Log.d(TAG, "observeNewNotifications: Received insert change: $change")
                val record = change.record as? JsonObject ?: run {
                    Log.w(TAG, "observeNewNotifications: Invalid record type, skipping")
                    return@mapNotNull null
                }
                val recordUserId = record["user_id"]?.jsonPrimitive?.content ?: run {
                    Log.w(TAG, "observeNewNotifications: Missing user_id in record, skipping")
                    return@mapNotNull null
                }
                if (recordUserId != userId) {
                    Log.d(TAG, "observeNewNotifications: Change not for userId=$userId, skipping")
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
}