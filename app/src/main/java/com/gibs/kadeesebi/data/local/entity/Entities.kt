package com.gibs.kadeesebi.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.gibs.kadeesebi.domain.model.GiftDirection

@Entity(tableName = "circles")
data class CircleEntity(
    @PrimaryKey val id: String,
    val name: String,
    val colorArgb: Int,
    val builtInKey: String? = null,
)

@Entity(tableName = "event_types")
data class EventTypeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val iconKey: String,
    val builtInKey: String? = null,
    val sortOrder: Int = 0,
)

@Entity(
    tableName = "persons",
    foreignKeys = [
        ForeignKey(
            entity = CircleEntity::class,
            parentColumns = ["id"],
            childColumns = ["circleId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [Index("circleId")],
)
data class PersonEntity(
    @PrimaryKey val id: String,
    val fullName: String,
    val phone: String? = null,
    val photoUri: String? = null,
    val circleId: String? = null,
    val note: String? = null,
    val relation: String? = null,
)

@Entity(
    tableName = "tois",
    indices = [Index("hostPersonId"), Index("date"), Index("eventTypeId")],
)
data class ToiEntity(
    @PrimaryKey val id: String,
    val hostPersonId: String?,
    val eventTypeId: String,
    val date: Long,
    val place: String? = null,
    val isOwnToi: Boolean,
    val note: String? = null,
    val title: String? = null,
    val reminderAt: Long? = null,
)

@Entity(
    tableName = "gifts",
    foreignKeys = [
        ForeignKey(
            entity = ToiEntity::class,
            parentColumns = ["id"],
            childColumns = ["toiId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("toiId"), Index("personId")],
)
data class GiftEntity(
    @PrimaryKey val id: String,
    val toiId: String,
    val personId: String,
    val direction: GiftDirection,
    val amount: Long,
    val currency: String = "KZT",
    val date: Long,
    val note: String? = null,
    val photoUri: String? = null,
)
