package com.example.appnextexercise.ui.home_fragment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnextexercise.R
import com.example.appnextexercise.db.DataRepository
import com.example.appnextexercise.model.DailyItem
import com.example.appnextexercise.utils.AppUtils
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel : ViewModel() {

    companion object {
        val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
    }

    val data = MutableLiveData<List<DailyItem>>()

    fun setData(newData: List<DailyItem>) {
        data.postValue(newData)
    }

    // Initializes the data by fetching it from the DataRepository and setting it to the data LiveData property
    fun initData(context: Context) {
        viewModelScope.launch {
            val weeklyDataEntity = DataRepository(context).getInstance(context)?.getData()
            val dailyItemList = mutableListOf<DailyItem>()
            if (weeklyDataEntity != null) {
                for (weekEntity in weeklyDataEntity) {
                    dailyItemList.add(
                        DailyItem(weekEntity.dailyGoal, weekEntity.dailyActivity)
                    )
                }
                setData(dailyItemList)
            }
        }
    }

    fun getDataForChart(context: Context) : BarData{
        val distanceWalkList: MutableList<Float> = mutableListOf()
        val goalList: MutableList<Float> = mutableListOf()

        for (dailItem in data.value!!) {
            distanceWalkList.add(dailItem.daily_activity.toFloat())
            goalList.add(dailItem.daily_goal.toFloat())
        }

        val entriesDistanceWalk = mutableListOf<BarEntry>()
        val entriesGoalDistance = mutableListOf<BarEntry>()

        for (i in daysOfWeek.indices) {
            entriesDistanceWalk.add(BarEntry(i.toFloat(), distanceWalkList[i], data.value!![i]))
            entriesGoalDistance.add(BarEntry(i.toFloat(), goalList[i], data.value!![i]))
        }

        val setDistanceWalk = BarDataSet(entriesDistanceWalk, context.getString(R.string.activity))
        setDistanceWalk.color = context.resources.getColor(R.color.blue_indicator, null)
        setDistanceWalk.setDrawValues(false)

        val setGoalDistance = BarDataSet(entriesGoalDistance, context.getString(R.string.daily_goal_lowercase))
        setGoalDistance.color = context.resources.getColor(R.color.green_indicator,null)
        setGoalDistance.setDrawValues(false)

        val data = BarData(setDistanceWalk, setGoalDistance)
        data.barWidth = 0.3f
        data.setValueFormatter(LargeValueFormatter())
        data.setValueTypeface(AppUtils.getRobotoLightFont(context))
        return data
    }

}