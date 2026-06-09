package com.gibs.kadeesebi.data.backup

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import com.gibs.kadeesebi.domain.model.BuiltInEventTypes
import com.gibs.kadeesebi.domain.model.Circle
import com.gibs.kadeesebi.domain.model.EventType
import com.gibs.kadeesebi.domain.model.Gift
import com.gibs.kadeesebi.domain.model.GiftDirection
import com.gibs.kadeesebi.domain.model.Person
import com.gibs.kadeesebi.domain.model.Toi
import com.gibs.kadeesebi.domain.repository.CircleRepository
import com.gibs.kadeesebi.domain.repository.EventTypeRepository
import com.gibs.kadeesebi.domain.repository.GiftRepository
import com.gibs.kadeesebi.domain.repository.PersonRepository
import com.gibs.kadeesebi.domain.repository.ToiRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val circleRepository: CircleRepository,
    private val eventTypeRepository: EventTypeRepository,
    private val personRepository: PersonRepository,
    private val toiRepository: ToiRepository,
    private val giftRepository: GiftRepository,
) {
    suspend fun export(uri: Uri) {
        val json = buildJson()
        context.contentResolver.openOutputStream(uri)?.use { os ->
            os.write(json.toByteArray(Charsets.UTF_8))
        }
    }

    /**
     * Создаёт новый файл резервной копии внутри выбранной пользователем папки
     * (Storage Access Framework). Папка может находиться в Google Drive, Dropbox
     * и т. п. — синхронизацию выполняет само облачное приложение. Токены не нужны.
     */
    suspend fun backupToFolder(treeUri: Uri): Boolean = runCatching {
        val json = buildJson()
        val parent = DocumentsContract.buildDocumentUriUsingTree(
            treeUri,
            DocumentsContract.getTreeDocumentId(treeUri),
        )
        val stamp = SimpleDateFormat("yyyy-MM-dd_HHmm", Locale.US).format(Date())
        val fileUri = DocumentsContract.createDocument(
            context.contentResolver,
            parent,
            "application/json",
            "gifty_backup_$stamp.json",
        ) ?: return@runCatching false
        context.contentResolver.openOutputStream(fileUri)?.use { os ->
            os.write(json.toByteArray(Charsets.UTF_8))
        } ?: return@runCatching false
        true
    }.getOrDefault(false)

    private suspend fun buildJson(): String {
        val root = JSONObject()
        root.put("version", 3)
        root.put("exportedAt", System.currentTimeMillis())

        val circles = JSONArray()
        circleRepository.observeCircles().first().forEach { c ->
            circles.put(
                JSONObject().put("id", c.id).put("name", c.name)
                    .put("colorArgb", c.colorArgb).put("builtInKey", c.builtInKey),
            )
        }
        root.put("circles", circles)

        val eventTypes = JSONArray()
        eventTypeRepository.observeEventTypes().first().forEach { e ->
            eventTypes.put(
                JSONObject()
                    .put("id", e.id).put("name", e.name).put("iconKey", e.iconKey)
                    .put("builtInKey", e.builtInKey).put("sortOrder", e.sortOrder),
            )
        }
        root.put("eventTypes", eventTypes)

        val persons = JSONArray()
        personRepository.observePeople().first().forEach { p ->
            persons.put(
                JSONObject()
                    .put("id", p.id).put("fullName", p.fullName)
                    .put("phone", p.phone).put("photoUri", p.photoUri)
                    .put("circleId", p.circleId).put("note", p.note)
                    .put("relation", p.relation),
            )
        }
        root.put("persons", persons)

        val tois = JSONArray()
        toiRepository.observeTois().first().forEach { t ->
            tois.put(
                JSONObject()
                    .put("id", t.id).put("hostPersonId", t.hostPersonId)
                    .put("eventTypeId", t.eventTypeId).put("date", t.date)
                    .put("place", t.place).put("isOwnToi", t.isOwnToi).put("note", t.note)
                    .put("title", t.title),
            )
        }
        root.put("tois", tois)

        val gifts = JSONArray()
        giftRepository.observeGifts().first().forEach { g ->
            gifts.put(
                JSONObject()
                    .put("id", g.id).put("toiId", g.toiId).put("personId", g.personId)
                    .put("direction", g.direction.name).put("amount", g.amount)
                    .put("currency", g.currency).put("date", g.date)
                    .put("photoUri", g.photoUri).put("note", g.note),
            )
        }
        root.put("gifts", gifts)

        return root.toString(2)
    }

    suspend fun import(uri: Uri) {
        val text = context.contentResolver.openInputStream(uri)?.use {
            it.readBytes().toString(Charsets.UTF_8)
        } ?: return
        val root = JSONObject(text)

        eventTypeRepository.ensureDefaults()

        root.optJSONArray("circles")?.let { arr ->
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                circleRepository.upsert(
                    Circle(
                        id = o.getString("id"),
                        name = o.getString("name"),
                        colorArgb = o.getInt("colorArgb"),
                        builtInKey = o.optStringOrNull("builtInKey"),
                    ),
                )
            }
        }
        root.optJSONArray("eventTypes")?.let { arr ->
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                eventTypeRepository.upsert(
                    EventType(
                        id = o.getString("id"),
                        name = o.getString("name"),
                        iconKey = o.optString("iconKey", "gift"),
                        builtInKey = o.optStringOrNull("builtInKey"),
                        sortOrder = o.optInt("sortOrder", 0),
                    ),
                )
            }
        }

        val existingTypes = eventTypeRepository.observeEventTypes().first()
        val typeIds = existingTypes.map { it.id }.toSet()
        val fallbackId = existingTypes.firstOrNull { it.builtInKey == BuiltInEventTypes.OTHER }?.id
            ?: existingTypes.firstOrNull()?.id

        root.optJSONArray("persons")?.let { arr ->
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                personRepository.upsert(
                    Person(
                        id = o.getString("id"),
                        fullName = o.getString("fullName"),
                        phone = o.optStringOrNull("phone"),
                        photoUri = o.optStringOrNull("photoUri"),
                        circleId = o.optStringOrNull("circleId"),
                        note = o.optStringOrNull("note"),
                        relation = o.optStringOrNull("relation"),
                    ),
                )
            }
        }
        root.optJSONArray("tois")?.let { arr ->
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val rawTypeId = o.optStringOrNull("eventTypeId")
                val resolvedTypeId = rawTypeId?.takeIf { typeIds.contains(it) } ?: fallbackId ?: continue
                toiRepository.upsert(
                    Toi(
                        id = o.getString("id"),
                        hostPersonId = o.optStringOrNull("hostPersonId"),
                        eventTypeId = resolvedTypeId,
                        date = o.getLong("date"),
                        place = o.optStringOrNull("place"),
                        isOwnToi = o.optBoolean("isOwnToi", false),
                        note = o.optStringOrNull("note"),
                        title = o.optStringOrNull("title"),
                    ),
                )
            }
        }
        root.optJSONArray("gifts")?.let { arr ->
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                giftRepository.upsert(
                    Gift(
                        id = o.getString("id"),
                        toiId = o.getString("toiId"),
                        personId = o.getString("personId"),
                        direction = runCatching { GiftDirection.valueOf(o.getString("direction")) }.getOrDefault(GiftDirection.GIVEN),
                        amount = o.getLong("amount"),
                        currency = o.optString("currency", "KZT"),
                        date = o.getLong("date"),
                        note = o.optStringOrNull("note"),
                        photoUri = o.optStringOrNull("photoUri"),
                    ),
                )
            }
        }
    }

    private fun JSONObject.optStringOrNull(key: String): String? =
        if (!has(key) || isNull(key)) null else getString(key)
}
