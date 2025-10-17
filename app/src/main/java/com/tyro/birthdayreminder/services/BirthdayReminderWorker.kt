package com.tyro.birthdayreminder.services

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters

class BirthdayReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        val name = inputData.getString("name")
        val id = inputData.getString("id")

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("name", name)
            putExtra("id", id)
        }
        context.sendBroadcast(intent)

        return Result.success()
    }


}