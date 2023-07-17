package com.example.appnextexercise.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appnextexercise.db.entity.WeeklyDataEntity

@Dao
interface WeeklyDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weeklyDataList: List<WeeklyDataEntity>)

    @Query("SELECT * FROM weekly_data")
    suspend fun getData(): List<WeeklyDataEntity>

}