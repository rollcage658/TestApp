package com.example.appnextexercise.ui.timeline_fragment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnextexercise.db.DataRepository
import com.example.appnextexercise.model.DailyData
import com.example.appnextexercise.model.DailyItem
import com.example.appnextexercise.model.DailyItemTimeline
import com.example.appnextexercise.model.WeeklyData
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class TimelineViewModel : ViewModel() {

    val daysOfWeek = listOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY
    )

    val data = MutableLiveData<List<WeeklyData>>()
    var days = MutableLiveData<List<DailyItemTimeline>>()
    fun setData(newData: List<WeeklyData>) {
        data.value = newData
    }

    fun initData(context: Context) {
        viewModelScope.launch {
            val weeklyDataEntity = DataRepository().getInstance(context)?.getData()
            val weeklyDataList = mutableListOf<WeeklyData>()
            if (weeklyDataEntity != null) {

                for (weekEntity in weeklyDataEntity) {
                    weeklyDataList.add(
                        WeeklyData(
                            DailyItem(weekEntity.dailyGoal, weekEntity.dailyActivity),
                            DailyData(weekEntity.dailyDistanceMeters, weekEntity.dailyKcal))
                    )
                }
                setData(weeklyDataList)
                initAdapter()
            }
        }
    }

    fun initAdapter() {
        val today = LocalDate.now()
        val startOfWeek = today.minusDays(today.dayOfWeek.value.toLong())
        days.postValue(List(daysOfWeek.size) { index ->
            val date = startOfWeek.plusDays(index.toLong())
            DailyItemTimeline(data.value!![index].daily_item.daily_goal,
                data.value!![index].daily_item.daily_activity,
                data.value!![index].daily_data.daily_distance_meters,
                data.value!![index].daily_data.daily_kcal,
                date.dayOfMonth,
                date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                date == today)
        })
    }

    fun getMonth(): String {
        val currentMonth = LocalDate.now().month
        val monthName = currentMonth.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return monthName
    }

}