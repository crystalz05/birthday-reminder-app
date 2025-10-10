package com.tyro.birthdayreminder.services

import com.tyro.birthdayreminder.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import androidx.core.content.ContextCompat.getSystemService
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.tyro.birthdayreminder.MainActivity
import com.tyro.birthdayreminder.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService: FirebaseMessagingService() {

    @Inject
    lateinit var supabase: SupabaseClient

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM from myfirebasemessagingservice", "New token: $token")

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        CoroutineScope(Dispatchers.IO).launch {
            val result = saveTokenToServer(uid, token)
            result.onSuccess {
                Log.d("FCM", "Token saved successfully")
            }.onFailure { e ->
                Log.e("FCM", "Error saving token", e)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: "Reminder"
        val body = message.notification?.body ?: ""
        val contactId = message.data["contactId"] // custom data

        Log.d("Messaging Service", contactId.toString())
        if(message.notification != null){
            showNotification(title, body, contactId)
        }
    }

    private fun showNotification(title: String?, body: String?, contactId: String?){
        val channelId = "default_channel"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, "General",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("route", Screen.BirthDayDetail.passContactId(contactId))
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title ?: "Notification")
            .setContentText(body ?: "")
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private suspend fun saveTokenToServer(uid: String, fcmToken: String): Result<Unit> {
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

}
