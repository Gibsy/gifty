package com.gibs.kadeesebi.domain.repository

import com.gibs.kadeesebi.domain.model.Circle
import com.gibs.kadeesebi.domain.model.EventType
import com.gibs.kadeesebi.domain.model.Gift
import com.gibs.kadeesebi.domain.model.Person
import com.gibs.kadeesebi.domain.model.Toi
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    fun observePeople(): Flow<List<Person>>
    fun observePerson(id: String): Flow<Person?>
    suspend fun upsert(person: Person)
    suspend fun delete(id: String)
}

interface CircleRepository {
    fun observeCircles(): Flow<List<Circle>>
    suspend fun upsert(circle: Circle)
    suspend fun delete(id: String)
    suspend fun ensureDefaults()
}

interface EventTypeRepository {
    fun observeEventTypes(): Flow<List<EventType>>
    suspend fun upsert(eventType: EventType)
    suspend fun delete(id: String): Boolean
    suspend fun ensureDefaults()
}

interface ToiRepository {
    fun observeTois(): Flow<List<Toi>>
    fun observeUpcoming(fromMillis: Long): Flow<List<Toi>>
    fun observeToi(id: String): Flow<Toi?>
    suspend fun upsert(toi: Toi)
    suspend fun delete(id: String)
}

interface GiftRepository {
    fun observeGifts(): Flow<List<Gift>>
    fun observeGiftsForPerson(personId: String): Flow<List<Gift>>
    fun observeGiftsForToi(toiId: String): Flow<List<Gift>>
    suspend fun upsert(gift: Gift)
    suspend fun delete(id: String)
}
