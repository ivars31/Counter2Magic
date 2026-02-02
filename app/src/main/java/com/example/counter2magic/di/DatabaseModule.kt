package com.example.counter2magic.di

import android.content.Context
import com.example.counter2magic.data.local.db.CountdownDatabase
import com.example.counter2magic.data.local.db.dao.EventDao
import com.example.counter2magic.data.local.db.dao.ReminderDao
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
    fun provideDatabase(@ApplicationContext context: Context): CountdownDatabase {
        return CountdownDatabase.getInstance(context)
    }

    @Provides
    fun provideEventDao(database: CountdownDatabase): EventDao {
        return database.eventDao()
    }

    @Provides
    fun provideReminderDao(database: CountdownDatabase): ReminderDao {
        return database.reminderDao()
    }
}
