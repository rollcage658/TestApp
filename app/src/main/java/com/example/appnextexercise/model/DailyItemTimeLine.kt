package com.example.appnextexercise.model

import java.time.LocalDate

data class DailyItemTimeline (
    val dailyGoal: Int,
    val dailyActivity: Int,
    val dailyDistanceMeters: Int,
    val dailyKcal: Int,
    val dateDayNumber: Int,
    val dateDayName: String,
    val isCurrentDay: Boolean
)