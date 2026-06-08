package com.gibs.kadeesebi.data.repository

import com.gibs.kadeesebi.data.local.dao.CircleDao
import com.gibs.kadeesebi.data.local.dao.EventTypeDao
import com.gibs.kadeesebi.data.local.dao.GiftDao
import com.gibs.kadeesebi.data.local.dao.PersonDao
import com.gibs.kadeesebi.data.local.dao.ToiDao
import com.gibs.kadeesebi.data.mapper.toDomain
import com.gibs.kadeesebi.data.mapper.toEntity
import com.gibs.kadeesebi.domain.model.BuiltInCircles
import com.gibs.kadeesebi.domain.model.BuiltInEventTypes
import com.gibs.kadeesebi.domain.model.Circle
import com.gibs.kadeesebi.domain.model.EventType
import com.gibs.kadeesebi.domain.model.Gift
import com.gibs.kadeesebi.domain.model.Person
import com.gibs.kadeesebi.domain.model.Toi
import com.gibs.kadeesebi.domain.repository.CircleRepository
import com.gibs.kadeesebi.domain.repository.EventTypeRepository
import com.gibs.kadeesebi.domain.repository.GiftRepository
import com.gibs.kadeesebi.domain.repository.PersonRepository
import com.gibs.kadeesebi.domain.repository.ToiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val dao: PersonDao,
) : PersonRepository {
    override fun observePeople(): Flow<List<Person>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observePerson(id: String): Flow<Person?> =
        dao.observeById(id).map { it?.toDomain() }

    override suspend fun upsert(person: Person) = dao.upsert(person.toEntity())
    override suspend fun delete(id: String) = dao.delete(id)
}

class CircleRepositoryImpl @Inject constructor(
    private val dao: CircleDao,
) : CircleRepository {
    override fun observeCircles(): Flow<List<Circle>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun upsert(circle: Circle) = dao.upsert(circle.toEntity())
    override suspend fun delete(id: String) = dao.delete(id)

    override suspend fun ensureDefaults() {
        if (dao.count() > 0) return
        val defaults = listOf(
            Circle(UUID.randomUUID().toString(), "Друг", 0xFF2E8B57.toInt(), BuiltInCircles.FRIEND),
            Circle(UUID.randomUUID().toString(), "Коллега", 0xFF1B6CA8.toInt(), BuiltInCircles.COLLEAGUE),
            Circle(UUID.randomUUID().toString(), "Родственник", 0xFFC0392B.toInt(), BuiltInCircles.RELATIVE),
        )
        dao.insertAll(defaults.map { it.toEntity() })
    }
}

class EventTypeRepositoryImpl @Inject constructor(
    private val dao: EventTypeDao,
    private val toiDao: ToiDao,
) : EventTypeRepository {
    override fun observeEventTypes(): Flow<List<EventType>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun upsert(eventType: EventType) = dao.upsert(eventType.toEntity())

    override suspend fun delete(id: String): Boolean {
        if (toiDao.countForEventType(id) > 0) return false
        dao.delete(id)
        return true
    }

    override suspend fun ensureDefaults() {
        if (dao.count() > 0) return
        val defaults = listOf(
            EventType(UUID.randomUUID().toString(), "День рождения", "cake", BuiltInEventTypes.BIRTHDAY, 0),
            EventType(UUID.randomUUID().toString(), "Новый год", "celebration", BuiltInEventTypes.NEW_YEAR, 1),
            EventType(UUID.randomUUID().toString(), "Другое", "gift", BuiltInEventTypes.OTHER, 2),
        )
        dao.insertAll(defaults.map { it.toEntity() })
    }
}

class ToiRepositoryImpl @Inject constructor(
    private val dao: ToiDao,
) : ToiRepository {
    override fun observeTois(): Flow<List<Toi>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeUpcoming(fromMillis: Long): Flow<List<Toi>> =
        dao.observeUpcoming(fromMillis).map { list -> list.map { it.toDomain() } }

    override fun observeToi(id: String): Flow<Toi?> =
        dao.observeById(id).map { it?.toDomain() }

    override suspend fun upsert(toi: Toi) = dao.upsert(toi.toEntity())
    override suspend fun delete(id: String) = dao.delete(id)
}

class GiftRepositoryImpl @Inject constructor(
    private val dao: GiftDao,
) : GiftRepository {
    override fun observeGifts(): Flow<List<Gift>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeGiftsForPerson(personId: String): Flow<List<Gift>> =
        dao.observeForPerson(personId).map { list -> list.map { it.toDomain() } }

    override fun observeGiftsForToi(toiId: String): Flow<List<Gift>> =
        dao.observeForToi(toiId).map { list -> list.map { it.toDomain() } }

    override suspend fun upsert(gift: Gift) = dao.upsert(gift.toEntity())
    override suspend fun delete(id: String) = dao.delete(id)
}
