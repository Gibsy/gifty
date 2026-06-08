package com.gibs.kadeesebi.presentation.reminders

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun schedule(toiId: String, triggerAtMillis: Long) {
        val delay = triggerAtMillis - System.currentTimeMillis()
        if (delay <= 0) {
            cancel(toiId)
            return
        }
        val request = OneTimeWorkRequestBuilder<EventReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf(EventReminderWorker.KEY_TOI_ID to toiId))
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            workName(toiId),
            ExistingWorkPolicy.REPLACE,
            request,
        )
    }

    fun cancel(toiId: String) {
        WorkManager.getInstance(context).cancelUniqueWork(workName(toiId))
    }

    private fun workName(toiId: String): String = "event_reminder_$toiId"
}
