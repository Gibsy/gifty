package com.gibs.kadeesebi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gibs.kadeesebi.data.local.dao.CircleDao
import com.gibs.kadeesebi.data.local.dao.EventTypeDao
import com.gibs.kadeesebi.data.local.dao.GiftDao
import com.gibs.kadeesebi.data.local.dao.PersonDao
import com.gibs.kadeesebi.data.local.dao.ToiDao
import com.gibs.kadeesebi.data.local.entity.CircleEntity
import com.gibs.kadeesebi.data.local.entity.EventTypeEntity
import com.gibs.kadeesebi.data.local.entity.GiftEntity
import com.gibs.kadeesebi.data.local.entity.PersonEntity
import com.gibs.kadeesebi.data.local.entity.ToiEntity

@Database(
    entities = [
        CircleEntity::class,
        EventTypeEntity::class,
        PersonEntity::class,
        ToiEntity::class,
        GiftEntity::class,
    ],
    version = 6,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class KadeDatabase : RoomDatabase() {
    abstract fun circleDao(): CircleDao
    abstract fun eventTypeDao(): EventTypeDao
    abstract fun personDao(): PersonDao
    abstract fun toiDao(): ToiDao
    abstract fun giftDao(): GiftDao

    companion object {
        const val NAME = "kade_esebi.db"
    }
}
