package se.mau.grupp7.happyplant2.notification

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import se.mau.grupp7.happyplant2.R
import se.mau.grupp7.happyplant2.local.PlantDatabase

class WateringReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        private const val DAY_MS = 24L * 60L * 60L * 1000L
        private const val NOTIFICATION_ID = 1001
    }

    override suspend fun doWork(): Result {

        val dao = PlantDatabase.getDatabase(applicationContext).plantDao()
        val plants = dao.getAllPlantsOnce()

        val now = System.currentTimeMillis()
        val plantsToWater = plants.filter { plant ->
            if (plant.wateringIntervalMin <= 0) return@filter false
            val nextWaterTime = plant.lastTimeWatered.time + plant.wateringIntervalMin * DAY_MS
            now >= nextWaterTime
        }

        if (plantsToWater.isNotEmpty()) {
            sendNotification(plantsToWater.size)
        }

        return Result.success()
    }

    private fun sendNotification(count: Int) {

        val hasPermission = ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) return

        val title = "Dags att vattna! 💧"
        val text = if (count == 1) {
            "1 växt behöver vattnas"
        } else {
            "$count växter behöver vattnas"
        }

        val notification = NotificationCompat.Builder(applicationContext, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
    }
}