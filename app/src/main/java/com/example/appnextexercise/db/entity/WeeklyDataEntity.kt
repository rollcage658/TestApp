package com.example.appnextexercise.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_data")
data class WeeklyDataEntity(
    @PrimaryKey
    val id: Int = 0,
    val dailyGoal: Int,
    val dailyActivity: Int,
    val dailyDistanceMeters: Int,
    val dailyKcal: Int
)