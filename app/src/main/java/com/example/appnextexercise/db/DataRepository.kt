package com.example.appnextexercise.db

import android.content.Context
import com.example.appnextexercise.db.entity.LastSyncEntity
import com.example.appnextexercise.db.entity.WeeklyDataEntity

class DataRepository () {

    private var database: AppDatabase? = null

    private var instance: DataRepository? = null

    constructor(context: Context) : this() {
        database = AppDatabase.getDatabase(context)
    }

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

    suspend fun getWeeklyData(): List<WeeklyDataEntity> {
            return database?.weeklyDataDao()!!.getWeeklyData()
    }

    suspend fun insertWeeklyData(weeklyDataList: List<WeeklyDataEntity>) {
            database?.weeklyDataDao()!!.insertAll(weeklyDataList)
    }

    suspend fun insertLastSyncMs(lastSyncMs: LastSyncEntity) {
        database?.lastSyncDao()!!.deleteLastSyncMs()
        database?.lastSyncDao()!!.saveLastSyncMs(lastSyncMs)
    }

    suspend fun getLastSyncMs() : Long {
        return database?.lastSyncDao()!!.getLastSyncMs()
    }
}