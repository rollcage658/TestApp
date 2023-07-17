package com.example.appnextexercise.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appnextexercise.db.AppDatabase.Companion.DB_VERSION
import com.example.appnextexercise.db.dao.LastSyncDao
import com.example.appnextexercise.db.dao.WeeklyDataDao
import com.example.appnextexercise.db.entity.LastSyncEntity
import com.example.appnextexercise.db.entity.WeeklyDataEntity

@Database(
    entities = [WeeklyDataEntity::class, LastSyncEntity::class],
    version = DB_VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weeklyDataDao(): WeeklyDataDao
    abstract fun lastSyncDao(): LastSyncDao

    companion object {

        const val DB_VERSION = 1
        const val DB_FILE_NAME = "AppNextExerciseDatabase"
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DB_FILE_NAME
            ).build()
        }
    }
}