package com.gibs.kadeesebi.di

import android.content.Context
import androidx.room.Room
import com.gibs.kadeesebi.data.local.KadeDatabase
import com.gibs.kadeesebi.data.local.dao.CircleDao
import com.gibs.kadeesebi.data.local.dao.EventTypeDao
import com.gibs.kadeesebi.data.local.dao.GiftDao
import com.gibs.kadeesebi.data.local.dao.PersonDao
import com.gibs.kadeesebi.data.local.dao.ToiDao
import com.gibs.kadeesebi.data.repository.CircleRepositoryImpl
import com.gibs.kadeesebi.data.repository.EventTypeRepositoryImpl
import com.gibs.kadeesebi.data.repository.GiftRepositoryImpl
import com.gibs.kadeesebi.data.repository.PersonRepositoryImpl
import com.gibs.kadeesebi.data.repository.ToiRepositoryImpl
import com.gibs.kadeesebi.domain.repository.CircleRepository
import com.gibs.kadeesebi.domain.repository.EventTypeRepository
import com.gibs.kadeesebi.domain.repository.GiftRepository
import com.gibs.kadeesebi.domain.repository.PersonRepository
import com.gibs.kadeesebi.domain.repository.ToiRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KadeDatabase =
        Room.databaseBuilder(context, KadeDatabase::class.java, KadeDatabase.NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideCircleDao(db: KadeDatabase): CircleDao = db.circleDao()
    @Provides fun provideEventTypeDao(db: KadeDatabase): EventTypeDao = db.eventTypeDao()
    @Provides fun providePersonDao(db: KadeDatabase): PersonDao = db.personDao()
    @Provides fun provideToiDao(db: KadeDatabase): ToiDao = db.toiDao()
    @Provides fun provideGiftDao(db: KadeDatabase): GiftDao = db.giftDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindPersonRepository(impl: PersonRepositoryImpl): PersonRepository

    @Binds @Singleton
    abstract fun bindCircleRepository(impl: CircleRepositoryImpl): CircleRepository

    @Binds @Singleton
    abstract fun bindEventTypeRepository(impl: EventTypeRepositoryImpl): EventTypeRepository

    @Binds @Singleton
    abstract fun bindToiRepository(impl: ToiRepositoryImpl): ToiRepository

    @Binds @Singleton
    abstract fun bindGiftRepository(impl: GiftRepositoryImpl): GiftRepository
}
