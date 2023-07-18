package com.example.appnextexercise.db

import android.content.Context
import com.example.appnextexercise.db.entity.LastSyncEntity
import com.example.appnextexercise.db.entity.WeeklyDataEntity

class DataRepository constructor(context: Context) {

    // A nullable instance of the AppDatabase
    private var database: AppDatabase? = AppDatabase.getDatabase(context)

    // A nullable instance of the DataRepository
    private var instance: DataRepository? = null


    // Returns an instance of the DataRepository, creating it if it doesn't already exist
    fun getInstance(context: Context): DataRepository? {
        if (instance == null) {
            synchronized(DataRepository::class.java) {
                if (instance == null) {
                    instance = DataRepository(context)
                }
            }
        }
        return instance
    }

    // Returns a list of WeeklyDataEntity objects from the database
    suspend fun getData(): List<WeeklyDataEntity> {
            return database?.weeklyDataDao()!!.getData()
    }

    // Inserts a list of WeeklyDataEntity objects into the database
    suspend fun insertWeeklyData(weeklyDataList: List<WeeklyDataEntity>) {
            database?.weeklyDataDao()!!.insertAll(weeklyDataList)
    }

    // Inserts a LastSyncEntity object into the database, after deleting any existing LastSyncEntity objects
    suspend fun insertLastSyncMs(lastSyncMs: LastSyncEntity) {
        database?.lastSyncDao()!!.deleteLastSyncMs()
        database?.lastSyncDao()!!.saveLastSyncMs(lastSyncMs)
    }

    // Returns the last sync time in milliseconds from the database
    suspend fun getLastSyncMs() : Long {
        return database?.lastSyncDao()!!.getLastSyncMs()
    }
}