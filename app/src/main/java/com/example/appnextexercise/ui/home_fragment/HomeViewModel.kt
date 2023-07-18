package com.example.appnextexercise.ui.home_fragment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnextexercise.db.DataRepository
import com.example.appnextexercise.model.DailyItem
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    val data = MutableLiveData<List<DailyItem>>()

    fun setData(newData: List<DailyItem>) {
        data.postValue(newData)
    }

    fun initData(context: Context) {
        viewModelScope.launch {
            val weeklyDataEntity = DataRepository().getInstance(context)?.getData()
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

}