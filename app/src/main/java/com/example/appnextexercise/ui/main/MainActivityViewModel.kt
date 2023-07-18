package com.example.appnextexercise.ui.main

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnextexercise.R
import com.example.appnextexercise.db.DataRepository
import com.example.appnextexercise.db.entity.LastSyncEntity
import com.example.appnextexercise.db.entity.WeeklyDataEntity
import com.example.appnextexercise.model.DailyData
import com.example.appnextexercise.model.DailyItem
import com.example.appnextexercise.model.WeeklyData
import com.example.appnextexercise.model.WeeklyResponse
import com.example.appnextexercise.network.RetrofitService
import com.example.appnextexercise.ui.home_fragment.HomeFragment
import com.example.appnextexercise.ui.timeline_fragment.TimelineFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivityViewModel() : ViewModel() {

    companion object {
        const val HOURS_12 = 43200000L
    }

    val title = ObservableField<String>()

    var isDataAvailable = MutableLiveData<Boolean>()

    private suspend fun isNeedToSync(context: Context, requestTimeSyncMs: Long): Boolean {
        val lastSync = withContext(viewModelScope.coroutineContext) {
            DataRepository(context).getInstance(context)?.getLastSyncMs()
        }
        Log.d("MainActivityViewModel", "isNeedToSync: $lastSync")
        // just do simple minus operation to check if 12 hours passed
        return lastSync == null || requestTimeSyncMs - lastSync >= HOURS_12
    }

    fun fetchWeeklyData(context: Context, requestTimeSyncMs: Long) {
        // launch coroutine to not block UI responsiveness
        viewModelScope.launch {
            if (isNeedToSync(context, requestTimeSyncMs)) {
                Log.d("MainActivityViewModel", "fetchWeeklyData from server")
                val call: Call<WeeklyResponse> = RetrofitService.getInstance().getDailyData()
                call.enqueue(object : Callback<WeeklyResponse> {
                    override fun onResponse(call: Call<WeeklyResponse>, response: Response<WeeklyResponse>) {
                        if (response.isSuccessful) {
                            val weeklyResponse: WeeklyResponse? = response.body()
                            weeklyResponse?.let {
                                val weeklyDataList: List<WeeklyData> = it.weekly_data
                                val weeklyDataEntityList = mutableListOf<WeeklyDataEntity>()

                                for (weeklyData in weeklyDataList) {
                                    val dailyItem: DailyItem = weeklyData.daily_item
                                    val dailyData: DailyData = weeklyData.daily_data

                                    val weeklyDataEntity = WeeklyDataEntity(
                                        id = weeklyDataList.indexOf(weeklyData),
                                        dailyGoal = dailyItem.daily_goal,
                                        dailyActivity = dailyItem.daily_activity,
                                        dailyDistanceMeters = dailyData.daily_distance_meters,
                                        dailyKcal = dailyData.daily_kcal
                                    )

                                    weeklyDataEntityList.add(weeklyDataEntity)
                                }
                                insertDataIntoDatabase(context, weeklyDataEntityList)
                                insertLastSyncMs(context,requestTimeSyncMs)
                                isDataAvailable.postValue(true)
                            }
                        } else {
                            // Handle error, because this is mock app we dont really have a behavior for error
                        }
                    }

                    override fun onFailure(call: Call<WeeklyResponse>, t: Throwable) {
                        isDataAvailable.postValue(false)
                        // Handle failure,  because this is mock app we dont really have a behavior for failure
                    }
                })
            } else {
                Log.d("MainActivityViewModel", "use data from DB")
//                val weeklyDataEntity = DataRepository().getInstance(context)?.getWeeklyData()
//                val weeklyDataList = mutableListOf<WeeklyData>()
//                if (weeklyDataEntity != null) {
//                    for (weekEntity in weeklyDataEntity)
//                        weeklyDataList.add(WeeklyData(
//                            DailyItem(weekEntity.dailyGoal, weekEntity.dailyActivity),
//                            DailyData(weekEntity.dailyDistanceMeters, weekEntity.dailyKcal))
//                        )
//                }
                isDataAvailable.postValue(true)
            }
        }
    }

    private fun insertDataIntoDatabase(context: Context, weeklyDataEntityList: List<WeeklyDataEntity>) {
        // Run database operation on a background thread
        viewModelScope.launch {
            DataRepository(context).getInstance(context)?.insertWeeklyData(weeklyDataEntityList)
        }
    }

    private fun insertLastSyncMs(context: Context, lastSyncMs: Long) {
        viewModelScope.launch {
            DataRepository(context).getInstance(context)?.insertLastSyncMs(LastSyncEntity(lastSyncMs))
        }
    }

    private fun replaceFragment(supportFragmentManager: FragmentManager, fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.enter,
            R.anim.exit,
            R.anim.pop_enter,
            R.anim.pop_exit)

        transaction.replace(R.id.fragmentContainer, fragment)
            .addToBackStack(fragment.tag)
            .commit()
    }

    fun switchToHomeFragment(supportFragmentManager: FragmentManager) {
        replaceFragment(supportFragmentManager, HomeFragment())
    }

    fun switchToTimelineFragment(supportFragmentManager: FragmentManager) {
        replaceFragment(supportFragmentManager, TimelineFragment())
    }

    fun canGoBack(context: Context): Boolean{
        return title.get().equals(context.getString(R.string.timeline_title))
    }

}