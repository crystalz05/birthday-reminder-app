package com.tyro.birthdayreminder.view_model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.tyro.birthdayreminder.custom_class.getDate
import com.tyro.birthdayreminder.entity.Contact
import com.tyro.birthdayreminder.services.ReminderReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.tyro.birthdayreminder.data_store.AppSettingsDataStore
import com.tyro.birthdayreminder.services.BirthdayReminderWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@HiltViewModel
class AlarmViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsDataStore: AppSettingsDataStore
) : ViewModel() {

    private val _alarmState = MutableStateFlow(false)
    val alarmState: StateFlow<Boolean> = _alarmState

    init {
        viewModelScope.launch {
            settingsDataStore.alarmEnabledFlow.collect {
                _alarmState.value = it
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleAlarmState(enabled: Boolean, birthdays: List<Contact>) {
        viewModelScope.launch {
            settingsDataStore.setAlarmEnabled(enabled)
            if (enabled) scheduleAllBirthdayAlarm(birthdays)
            else cancelAllBirthdayReminders(birthdays)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleAllBirthdayAlarm(birthdays: List<Contact>) {

        val workManager = WorkManager.getInstance(context)

        for(birthday in birthdays){

            val delayMillis = getNextBirthdayMillis(birthday.birthday) - System.currentTimeMillis()
            if(delayMillis <= 0) continue

            val inputData = Data.Builder()
                .putString("name", birthday.fullName)
                .putString("id", birthday.id)
                .build()

            if(birthday.id != null){
                val workRequest = OneTimeWorkRequestBuilder<BirthdayReminderWorker>()
                    .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                    .setInputData(inputData)
                    .addTag(birthday.id)
                    .build()

                workManager.enqueue(workRequest)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNextBirthdayMillis(date: String): Long {
        val birthday = getDate(date)
        val now = LocalDate.now()
        var nextBirthday = birthday.withYear(now.year)

        if (nextBirthday.isBefore(now)) {
            nextBirthday = nextBirthday.plusYears(1)
        }
        return nextBirthday
            .atTime(17, 22) // reminder time
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    private fun cancelAllBirthdayReminders(birthdays: List<Contact>) {
        val workManager = WorkManager.getInstance(context)
        for (birthday in birthdays) {
            if(birthday.id != null){
                workManager.cancelAllWorkByTag(birthday.id)
            }
        }
    }
}
