package com.example.counter2magic.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.counter2magic.data.local.db.dao.EventDao
import com.example.counter2magic.data.local.db.dao.ReminderDao
import com.example.counter2magic.data.local.db.entity.EventEntity
import com.example.counter2magic.data.local.db.entity.ReminderEntity

@Database(
    entities = [EventEntity::class, ReminderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CountdownDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: CountdownDatabase? = null

        fun getInstance(context: Context): CountdownDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CountdownDatabase::class.java,
                    "countdown_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
