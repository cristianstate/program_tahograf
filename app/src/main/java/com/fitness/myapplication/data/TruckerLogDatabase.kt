package com.fitness.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TruckerLog::class], version = 1, exportSchema = false)
abstract class TruckerLogDatabase : RoomDatabase() {
    abstract fun truckerLogDao(): TruckerLogDao

    companion object {
        @Volatile
        private var Instance: TruckerLogDatabase? = null

        fun getDatabase(context: Context): TruckerLogDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TruckerLogDatabase::class.java, "trucker_log_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
