package se.mau.grupp7.happyplant2.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object NotificationHelper {

    const val CHANNEL_ID = "watering_reminder_channel"
    private const val WORK_NAME = "watering_reminder_work"

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Vattningspåminnelser",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Påminnelser om att vattna dina växter"
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    fun scheduleWateringReminder(context: Context) {
        val request = PeriodicWorkRequestBuilder<WateringReminderWorker>(
            24, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}