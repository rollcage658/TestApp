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

// Defines a Room database with two entities (WeeklyDataEntity and LastSyncEntity) and a version number
@Database(
    entities = [WeeklyDataEntity::class, LastSyncEntity::class],
    version = DB_VERSION)
abstract class AppDatabase : RoomDatabase() {

    // Abstract methods to get instances of the WeeklyDataDao and LastSyncDao interfaces
    abstract fun weeklyDataDao(): WeeklyDataDao
    abstract fun lastSyncDao(): LastSyncDao

    companion object {

        const val DB_VERSION = 1
        // The file name of the database
        const val DB_FILE_NAME = "AppNextExerciseDatabase"
        // A nullable instance of the AppDatabase, marked as volatile to ensure thread safety
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Returns an instance of the AppDatabase, creating it if it doesn't already exist
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

        // Builds an instance of the AppDatabase using the Room.databaseBuilder method
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DB_FILE_NAME
            ).build()
        }
    }
}