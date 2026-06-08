package com.gibs.kadeesebi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.gibs.kadeesebi.data.local.entity.CircleEntity
import com.gibs.kadeesebi.data.local.entity.EventTypeEntity
import com.gibs.kadeesebi.data.local.entity.GiftEntity
import com.gibs.kadeesebi.data.local.entity.PersonEntity
import com.gibs.kadeesebi.data.local.entity.ToiEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CircleDao {
    @Query("SELECT * FROM circles ORDER BY name")
    fun observeAll(): Flow<List<CircleEntity>>

    @Query("SELECT COUNT(*) FROM circles")
    suspend fun count(): Int

    @Upsert
    suspend fun upsert(circle: CircleEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(circles: List<CircleEntity>)

    @Query("DELETE FROM circles WHERE id = :id")
    suspend fun delete(id: String)
}

@Dao
interface EventTypeDao {
    @Query("SELECT * FROM event_types ORDER BY sortOrder, name")
    fun observeAll(): Flow<List<EventTypeEntity>>

    @Query("SELECT COUNT(*) FROM event_types")
    suspend fun count(): Int

    @Upsert
    suspend fun upsert(eventType: EventTypeEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(eventTypes: List<EventTypeEntity>)

    @Query("DELETE FROM event_types WHERE id = :id")
    suspend fun delete(id: String)
}

@Dao
interface PersonDao {
    @Query("SELECT * FROM persons ORDER BY fullName")
    fun observeAll(): Flow<List<PersonEntity>>

    @Query("SELECT * FROM persons WHERE id = :id")
    fun observeById(id: String): Flow<PersonEntity?>

    @Upsert
    suspend fun upsert(person: PersonEntity)

    @Query("DELETE FROM persons WHERE id = :id")
    suspend fun delete(id: String)
}

@Dao
interface ToiDao {
    @Query("SELECT * FROM tois ORDER BY date DESC")
    fun observeAll(): Flow<List<ToiEntity>>

    @Query("SELECT * FROM tois WHERE date >= :fromMillis ORDER BY date ASC")
    fun observeUpcoming(fromMillis: Long): Flow<List<ToiEntity>>

    @Query("SELECT * FROM tois WHERE id = :id")
    fun observeById(id: String): Flow<ToiEntity?>

    @Query("SELECT COUNT(*) FROM tois WHERE eventTypeId = :eventTypeId")
    suspend fun countForEventType(eventTypeId: String): Int

    @Upsert
    suspend fun upsert(toi: ToiEntity)

    @Query("DELETE FROM tois WHERE id = :id")
    suspend fun delete(id: String)
}

@Dao
interface GiftDao {
    @Query("SELECT * FROM gifts ORDER BY date DESC")
    fun observeAll(): Flow<List<GiftEntity>>

    @Query("SELECT * FROM gifts WHERE personId = :personId ORDER BY date DESC")
    fun observeForPerson(personId: String): Flow<List<GiftEntity>>

    @Query("SELECT * FROM gifts WHERE toiId = :toiId ORDER BY date DESC")
    fun observeForToi(toiId: String): Flow<List<GiftEntity>>

    @Upsert
    suspend fun upsert(gift: GiftEntity)

    @Query("DELETE FROM gifts WHERE id = :id")
    suspend fun delete(id: String)
}
