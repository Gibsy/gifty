package com.gibs.kadeesebi.presentation.common

object EventEmoji {
    private val map: Map<String, String> = mapOf(
        "gift" to "\uD83C\uDF81",
        "cake" to "\uD83C\uDF82",
        "celebration" to "\uD83C\uDF89",
        "favorite" to "\u2764\uFE0F",
        "flower" to "\uD83C\uDF37",
        "military" to "\uD83C\uDF96\uFE0F",
        "trophy" to "\uD83C\uDFC6",
        "home" to "\uD83C\uDFE0",
        "school" to "\uD83C\uDF93",
        "child" to "\uD83C\uDF7C",
        "star" to "\u2B50",
        "music" to "\uD83C\uDFB5",
        "work" to "\uD83D\uDCBC",
        "ring" to "\uD83D\uDC8D",
        "flag" to "\uD83D\uDEA9",
        "groups" to "\uD83D\uDC65",
        "restaurant" to "\uD83C\uDF7D\uFE0F",
    )

    fun forKey(key: String?): String = map[key] ?: "\uD83C\uDF81"
}
