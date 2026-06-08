package com.gibs.kadeesebi.data.export

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.gibs.kadeesebi.domain.model.GiftDirection
import com.gibs.kadeesebi.domain.repository.EventTypeRepository
import com.gibs.kadeesebi.domain.repository.GiftRepository
import com.gibs.kadeesebi.domain.repository.PersonRepository
import com.gibs.kadeesebi.domain.repository.ToiRepository
import com.gibs.kadeesebi.domain.util.Money
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExportManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val giftRepository: GiftRepository,
    private val toiRepository: ToiRepository,
    private val personRepository: PersonRepository,
    private val eventTypeRepository: EventTypeRepository,
) {
    private val dateFmt = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))

    private data class Row(
        val date: String,
        val toi: String,
        val person: String,
        val direction: String,
        val amount: String,
        val note: String,
    )

    private suspend fun rows(): List<Row> {
        val gifts = giftRepository.observeGifts().first()
        val tois = toiRepository.observeTois().first().associateBy { it.id }
        val people = personRepository.observePeople().first().associateBy { it.id }
        val eventTypeNames = eventTypeRepository.observeEventTypes().first().associate { it.id to it.name }
        return gifts.map { g ->
            val toi = tois[g.toiId]
            Row(
                date = dateFmt.format(Date(g.date)),
                toi = toi?.let { it.title?.takeIf { t -> t.isNotBlank() } ?: eventTypeNames[it.eventTypeId] ?: "\u2014" } ?: "\u2014",
                person = people[g.personId]?.fullName ?: "\u2014",
                direction = if (g.direction == GiftDirection.RECEIVED) "\u041f\u043e\u0434\u0430\u0440\u0438\u043b\u0438 \u043c\u043d\u0435" else "\u041f\u043e\u0434\u0430\u0440\u0438\u043b(\u0430) \u044f",
                amount = Money.format(g.amount),
                note = g.note.orEmpty(),
            )
        }
    }

    suspend fun exportCsv(): Uri {
        val dir = File(context.cacheDir, "exports").apply { mkdirs() }
        val file = File(dir, "kade_esebi_export.csv")
        val sb = StringBuilder()
        sb.append("\uFEFF")
        sb.append("\u0414\u0430\u0442\u0430;\u0421\u043e\u0431\u044b\u0442\u0438\u0435;\u0427\u0435\u043b\u043e\u0432\u0435\u043a;\u041d\u0430\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u0435;\u0421\u0443\u043c\u043c\u0430;\u0417\u0430\u043c\u0435\u0442\u043a\u0430\n")
        rows().forEach { r ->
            sb.append(listOf(r.date, r.toi, r.person, r.direction, r.amount, r.note).joinToString(";") { csvEscape(it) })
            sb.append("\n")
        }
        file.writeText(sb.toString(), Charsets.UTF_8)
        return uriFor(file)
    }

    suspend fun exportPdf(): Uri {
        val data = rows()
        val dir = File(context.cacheDir, "exports").apply { mkdirs() }
        val file = File(dir, "kade_esebi_export.pdf")
        val doc = PdfDocument()
        val titlePaint = Paint().apply { textSize = 18f; isFakeBoldText = true }
        val headerPaint = Paint().apply { textSize = 11f; isFakeBoldText = true }
        val textPaint = Paint().apply { textSize = 10f }

        val pageWidth = 595
        val pageHeight = 842
        val left = 32f
        val lineHeight = 18f
        var pageNumber = 1

        fun startPage(): PdfDocument.Page {
            val info = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            return doc.startPage(info)
        }

        var page = startPage()
        var canvas = page.canvas
        var y = 48f
        canvas.drawText("K\u04d9\u0434\u0435 \u0435\u0441\u0435\u0431\u0456 \u2014 \u044d\u043a\u0441\u043f\u043e\u0440\u0442 \u043f\u043e\u0434\u0430\u0440\u043a\u043e\u0432", left, y, titlePaint)
        y += lineHeight * 1.5f
        canvas.drawText("\u0414\u0430\u0442\u0430 / \u0421\u043e\u0431\u044b\u0442\u0438\u0435 / \u0427\u0435\u043b\u043e\u0432\u0435\u043a / \u041d\u0430\u043f\u0440. / \u0421\u0443\u043c\u043c\u0430", left, y, headerPaint)
        y += lineHeight

        data.forEach { r ->
            if (y > pageHeight - 40) {
                doc.finishPage(page)
                pageNumber++
                page = startPage()
                canvas = page.canvas
                y = 48f
            }
            val line = "${r.date}  \u00b7  ${r.toi}  \u00b7  ${r.person}  \u00b7  ${r.direction}  \u00b7  ${r.amount}"
            canvas.drawText(line, left, y, textPaint)
            y += lineHeight
        }
        doc.finishPage(page)

        file.outputStream().use { doc.writeTo(it) }
        doc.close()
        return uriFor(file)
    }

    private fun uriFor(file: File): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    private fun csvEscape(value: String): String {
        val needsQuote = value.contains(';') || value.contains('"') || value.contains('\n')
        val escaped = value.replace("\"", "\"\"")
        return if (needsQuote) "\"$escaped\"" else escaped
    }
}
