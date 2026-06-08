package com.gibs.kadeesebi.data.local

import androidx.room.TypeConverter
import com.gibs.kadeesebi.domain.model.GiftDirection

class Converters {
    @TypeConverter
    fun directionToString(value: GiftDirection): String = value.name

    @TypeConverter
    fun stringToDirection(value: String): GiftDirection =
        runCatching { GiftDirection.valueOf(value) }.getOrDefault(GiftDirection.GIVEN)
}
