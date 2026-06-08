package com.gibs.kadeesebi.presentation.reminders

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gibs.kadeesebi.KadeEsebiApp
import com.gibs.kadeesebi.MainActivity
import com.gibs.kadeesebi.data.settings.SettingsRepository
import com.gibs.kadeesebi.domain.model.AppLanguage
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
import java.text.SimpleDateFormat
import java.util.Date

@HiltWorker
class EventReminderWorker @AssistedInject constructor(
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
        val toiId = inputData.getString(KEY_TOI_ID) ?: return Result.success()
        val toi = toiRepository.observeToi(toiId).first() ?: return Result.success()

        val language = settingsRepository.language.first()
        val s = stringsFor(language)
        val type = eventTypeRepository.observeEventTypes().first().firstOrNull { it.id == toi.eventTypeId }
        val typeName = type?.let { s.eventTypeName(it) } ?: s.toiFallback
        val emoji = EventEmoji.forKey(type?.iconKey)
        val eventLabel = toi.title?.takeIf { it.isNotBlank() } ?: typeName
        val dateStr = SimpleDateFormat("d MMM yyyy", s.locale).format(Date(toi.date))

        val host = toi.hostPersonId?.let { personRepository.observePerson(it).first() }
        val suggested = host?.let {
            suggest(giftRepository.observeGiftsForPerson(it.id).first(), System.currentTimeMillis())
        }

        val title = "$emoji $eventLabel"
        val text = buildBody(language, dateStr, host?.fullName, suggested)
        notify(toiId.hashCode(), title, text)
        return Result.success()
    }

    private fun buildBody(
        language: AppLanguage,
        dateStr: String,
        hostName: String?,
        suggested: Long?,
    ): String = buildString {
        when (language) {
            AppLanguage.RU -> {
                append("\u0421\u043a\u043e\u0440\u043e \u0441\u043e\u0431\u044b\u0442\u0438\u0435 \u2014 $dateStr.")
                hostName?.let { append(" \u0423 $it.") }
                suggested?.let { append(" \u0420\u0435\u043a\u043e\u043c\u0435\u043d\u0434\u0443\u0435\u043c\u0430\u044f \u0441\u0443\u043c\u043c\u0430: ${Money.format(it)}.") }
            }
            AppLanguage.KK -> {
                append("\u0416\u0430\u049b\u044b\u043d\u0434\u0430 \u043e\u049b\u0438\u0493\u0430 \u2014 $dateStr.")
                hostName?.let { append(" $it-\u0434\u0430.") }
                suggested?.let { append(" \u04b0\u0441\u044b\u043d\u044b\u043b\u0430\u0442\u044b\u043d \u0441\u043e\u043c\u0430: ${Money.format(it)}.") }
            }
            AppLanguage.EN -> {
                append("Upcoming event \u2014 $dateStr.")
                hostName?.let { append(" For $it.") }
                suggested?.let { append(" Suggested amount: ${Money.format(it)}.") }
            }
        }
    }

    private fun notify(id: Int, title: String, text: String) {
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) return
        val openIntent = Intent(applicationContext, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            id,
            openIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        val notification = NotificationCompat.Builder(applicationContext, KadeEsebiApp.CHANNEL_REMINDERS)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        applicationContext.getSystemService(NotificationManager::class.java).notify(id, notification)
    }

    companion object {
        const val KEY_TOI_ID = "toi_id"
    }
}
