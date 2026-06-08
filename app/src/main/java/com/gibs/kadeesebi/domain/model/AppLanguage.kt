package com.gibs.kadeesebi.domain.model

enum class AppLanguage(val code: String) {
    RU("ru"),
    KK("kk"),
    EN("en");

    companion object {
        fun fromCode(code: String?): AppLanguage =
            entries.firstOrNull { it.code == code } ?: RU
    }
}
