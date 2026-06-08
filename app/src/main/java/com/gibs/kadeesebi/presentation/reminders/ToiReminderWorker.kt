package com.gibs.kadeesebi.presentation.reminders

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.gibs.kadeesebi.KadeEsebiApp
import com.gibs.kadeesebi.data.settings.SettingsRepository
import com.gibs.kadeesebi.domain.model.AppLanguage
import com.gibs.kadeesebi.domain.model.GiftDirection
import com.gibs.kadeesebi.domain.repository.EventTypeRepository
import com.gibs.kadeesebi.domain.repository.GiftRepository
import com.gibs.kadeesebi.domain.repository.PersonRepository
import com.gibs.kadeesebi.domain.repository.ToiRepository
import com.gibs.kadeesebi.domain.usecase.SuggestGiftAmount
import com.gibs.kadeesebi.domain.util.Money
import com.gibs.kadeesebi.presentation.common.EventEmoji
import com.gibs.kadeesebi.presentation.i18n.stringsFor
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

@HiltWorker
class ToiReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val toiRepository: ToiRepository,
    private val personRepository: PersonRepository,
    private val giftRepository: GiftRepository,
    private val eventTypeRepository: EventTypeRepository,
    private val suggest: SuggestGiftAmount,
    private val settingsRepository: SettingsRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val now = System.currentTimeMillis()
        val weekAhead = now + TimeUnit.DAYS.toMillis(7)
        val language = settingsRepository.language.first()
        val s = stringsFor(language)
        val fallbackName = s.toiFallback
        val types = eventTypeRepository.observeEventTypes().first()
        val upcoming = toiRepository.observeUpcoming(now).first()
            .filter { it.date <= weekAhead && !it.isOwnToi }

        upcoming.forEach { toi ->
            val hostId = toi.hostPersonId ?: return@forEach
            val host = personRepository.observePerson(hostId).first() ?: return@forEach
            val gifts = giftRepository.observeGiftsForPerson(hostId).first()
            val suggested = suggest(gifts, now)
            val lastReceived = gifts.filter { it.direction == GiftDirection.RECEIVED }.maxByOrNull { it.date }

            val type = types.firstOrNull { it.id == toi.eventTypeId }
            val emoji = EventEmoji.forKey(type?.iconKey)
            val name = toi.title?.takeIf { it.isNotBlank() } ?: type?.let { s.eventTypeName(it) } ?: fallbackName
            val text = buildReminderText(language, host.fullName, suggested, lastReceived?.amount)
            val title = reminderTitle(language, "$emoji $name")
            notify(toi.id.hashCode(), title, text)
        }
        return Result.success()
    }

    private fun reminderTitle(language: AppLanguage, toiTypeName: String): String = when (language) {
        AppLanguage.RU -> "\u0411\u043b\u0438\u0436\u0430\u0439\u0448\u0435\u0435 \u0441\u043e\u0431\u044b\u0442\u0438\u0435: $toiTypeName"
        AppLanguage.KK -> "\u0416\u0430\u049b\u044b\u043d \u043e\u049b\u0438\u0493\u0430: $toiTypeName"
        AppLanguage.EN -> "Upcoming event: $toiTypeName"
    }

    private fun buildReminderText(
        language: AppLanguage,
        hostName: String,
        suggested: Long,
        lastReceivedAmount: Long?,
    ): String = buildString {
        when (language) {
            AppLanguage.RU -> {
                append("\u0421\u043a\u043e\u0440\u043e \u0441\u043e\u0431\u044b\u0442\u0438\u0435 \u0443 $hostName. ")
                append("\u0420\u0435\u043a\u043e\u043c\u0435\u043d\u0434\u0443\u0435\u043c\u0430\u044f \u0441\u0443\u043c\u043c\u0430: ${Money.format(suggested)}")
                lastReceivedAmount?.let { append(", \u0432\u0430\u043c \u0434\u0430\u0440\u0438\u043b\u0438 ${Money.format(it)}") }
            }
            AppLanguage.KK -> {
                append("$hostName-\u0434\u0430 \u0436\u0430\u049b\u044b\u043d\u0434\u0430 \u043e\u049b\u0438\u0493\u0430 \u0431\u043e\u043b\u0430\u0434\u044b. ")
                append("\u04b0\u0441\u044b\u043d\u044b\u043b\u0430\u0442\u044b\u043d \u0441\u043e\u043c\u0430: ${Money.format(suggested)}")
                lastReceivedAmount?.let { append(", \u0441\u0456\u0437\u0433\u0435 ${Money.format(it)} \u0441\u044b\u0439\u043b\u0430\u0493\u0430\u043d \u0435\u0434\u0456") }
            }
            AppLanguage.EN -> {
                append("$hostName has an event soon. ")
                append("Suggested amount: ${Money.format(suggested)}")
                lastReceivedAmount?.let { append(", they gave you ${Money.format(it)}") }
            }
        }
    }

    private fun notify(id: Int, title: String, text: String) {
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) return
        val notification = NotificationCompat.Builder(applicationContext, KadeEsebiApp.CHANNEL_REMINDERS)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setAutoCancel(true)
            .build()
        applicationContext.getSystemService(NotificationManager::class.java)
            .notify(id, notification)
    }

    companion object {
        private const val WORK_NAME = "toi_reminder_periodic"

        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<ToiReminderWorker>(1, TimeUnit.DAYS).build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request,
            )
        }
    }
}
