package com.gibs.kadeesebi.domain.model

import java.util.Locale

enum class Currency(
    val code: String,
    val symbol: String,
    private val localeTag: String,
    val symbolFirst: Boolean,
) {
    RUB("RUB", "\u20bd", "ru-RU", symbolFirst = false),
    KZT("KZT", "\u20b8", "ru-KZ", symbolFirst = false),
    USD("USD", "$", "en-US", symbolFirst = true);

    fun locale(): Locale = Locale.forLanguageTag(localeTag)

    companion object {
        fun fromCode(code: String?): Currency =
            entries.firstOrNull { it.code == code } ?: KZT
    }
}
