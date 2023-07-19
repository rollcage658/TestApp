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


    // Initializes the data by fetching it from the DataRepository and setting it to the data LiveData property
    fun initData(context: Context) {
        viewModelScope.launch {
            val weeklyDataEntity = DataRepository(context).getData()
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

    // Initializes the adapter by setting the value of the days LiveData property
    fun initAdapter() {
        val today = LocalDate.now()
        // Check if its Sunday today if so this is the start of week if not then do minusDays to get the first day of week
        val startOfWeek = if (today.dayOfWeek.name == DayOfWeek.SUNDAY.toString()) {
            today
        } else {
            today.minusDays(today.dayOfWeek.value.toLong())
        }
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

    // Returns the name of the current month.
    fun getMonthName(): String {
        val currentMonth = LocalDate.now().month
        val monthName = currentMonth.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return monthName
    }

}