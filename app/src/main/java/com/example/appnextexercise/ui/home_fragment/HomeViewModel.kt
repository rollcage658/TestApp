package com.example.appnextexercise.ui.home_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appnextexercise.model.DailyItem

class HomeViewModel : ViewModel() {

    val data = MutableLiveData<List<DailyItem>>()

    fun setData(newData: List<DailyItem>) {
        data.value = newData
    }

}