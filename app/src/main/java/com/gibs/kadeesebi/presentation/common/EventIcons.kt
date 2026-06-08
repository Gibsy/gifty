package com.gibs.kadeesebi.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

object EventIcons {
    const val DEFAULT_KEY = "gift"

    val catalog: Map<String, ImageVector> = linkedMapOf(
        "gift" to Icons.Filled.CardGiftcard,
        "cake" to Icons.Filled.Cake,
        "celebration" to Icons.Filled.Celebration,
        "favorite" to Icons.Filled.Favorite,
        "flower" to Icons.Filled.LocalFlorist,
        "military" to Icons.Filled.MilitaryTech,
        "trophy" to Icons.Filled.EmojiEvents,
        "home" to Icons.Filled.Home,
        "school" to Icons.Filled.School,
        "child" to Icons.Filled.ChildCare,
        "star" to Icons.Filled.Star,
        "music" to Icons.Filled.MusicNote,
        "work" to Icons.Filled.Work,
        "ring" to Icons.Filled.Diamond,
        "flag" to Icons.Filled.Flag,
        "groups" to Icons.Filled.Groups,
        "restaurant" to Icons.Filled.Restaurant,
    )

    val keys: List<String> = catalog.keys.toList()

    fun icon(key: String?): ImageVector = catalog[key] ?: catalog.getValue(DEFAULT_KEY)
}
