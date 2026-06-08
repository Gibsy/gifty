package com.gibs.kadeesebi.data.mapper

import com.gibs.kadeesebi.data.local.entity.CircleEntity
import com.gibs.kadeesebi.data.local.entity.EventTypeEntity
import com.gibs.kadeesebi.data.local.entity.GiftEntity
import com.gibs.kadeesebi.data.local.entity.PersonEntity
import com.gibs.kadeesebi.data.local.entity.ToiEntity
import com.gibs.kadeesebi.domain.model.Circle
import com.gibs.kadeesebi.domain.model.EventType
import com.gibs.kadeesebi.domain.model.Gift
import com.gibs.kadeesebi.domain.model.Person
import com.gibs.kadeesebi.domain.model.Toi

fun CircleEntity.toDomain() = Circle(id, name, colorArgb, builtInKey)
fun Circle.toEntity() = CircleEntity(id, name, colorArgb, builtInKey)

fun EventTypeEntity.toDomain() = EventType(id, name, iconKey, builtInKey, sortOrder)
fun EventType.toEntity() = EventTypeEntity(id, name, iconKey, builtInKey, sortOrder)

fun PersonEntity.toDomain() = Person(id, fullName, phone, photoUri, circleId, note, relation)
fun Person.toEntity() = PersonEntity(id, fullName, phone, photoUri, circleId, note, relation)

fun ToiEntity.toDomain() = Toi(id, hostPersonId, eventTypeId, date, place, isOwnToi, note, title, reminderAt)
fun Toi.toEntity() = ToiEntity(id, hostPersonId, eventTypeId, date, place, isOwnToi, note, title, reminderAt)

fun GiftEntity.toDomain() = Gift(id, toiId, personId, direction, amount, currency, date, note, photoUri)
fun Gift.toEntity() = GiftEntity(id, toiId, personId, direction, amount, currency, date, note, photoUri)
