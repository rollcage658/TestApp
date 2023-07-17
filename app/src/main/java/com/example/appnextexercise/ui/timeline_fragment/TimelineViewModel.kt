package com.example.appnextexercise.ui.timeline_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appnextexercise.model.WeeklyData

class TimelineViewModel : ViewModel() {

    val data = MutableLiveData<List<WeeklyData>>()

    fun setData(newData: List<WeeklyData>) {
        data.value = newData
    }

}