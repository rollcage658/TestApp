package com.example.appnextexercise.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appnextexercise.R
import com.example.appnextexercise.databinding.ActivityMainBinding
import com.example.appnextexercise.ui.home_fragment.HomeViewModel
import com.example.appnextexercise.ui.timeline_fragment.TimelineViewModel
import com.example.appnextexercise.utils.animation.PushDownAnim
import java.util.Calendar


// this mainActivity control app flow which show homeFragment and if clicked timelineFragment
// the viewModel gets data from server and pass relevant information to fragments
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewModelHome: HomeViewModel
    private lateinit var viewModelTimeLine: TimelineViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModelHome = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModelTimeLine = ViewModelProvider(this).get(TimelineViewModel::class.java)

        binding.viewModel = viewModel
        handleData()
//        switchToHomeFragment()
        setBackButton()
    }

    private fun handleData() {
        viewModel.fetchWeeklyData(this, Calendar.getInstance().timeInMillis)
        viewModel.resultList.observe(this, Observer {
            switchToHomeFragment()
            //TODO later pass here data for charts to show
            Log.d("MainActivity", "resultList: $it")
            Log.d("MainActivity", "handleData: result size = " + viewModel.resultList.value?.size)
        })
    }

    private fun setBackButton() {
        PushDownAnim().setPushDownAnimTo(binding.backButton)
        binding.backButton.setOnClickListener {
            switchToHomeFragment()
        }
    }

    fun switchToHomeFragment() {
        // adjust backButton
        binding.backButton.visibility = View.GONE
        viewModel.switchToHomeFragment(supportFragmentManager)
        // adjust title
        viewModel.title.set(getString(R.string.daily_activity_title))
    }

    fun switchToTimelineFragment() {
        // adjust backButton
        binding.backButton.visibility = View.VISIBLE
        viewModel.switchToTimelineFragment(supportFragmentManager)
        // adjust title
        viewModel.title.set(getString(R.string.timeline_title))

    }

    override fun onBackPressed() {
        if (viewModel.canGoBack(this)) {
            switchToHomeFragment()
        } else {
            this.finish()
        }
    }

    fun getHomeViewModel(): HomeViewModel {
        return viewModel.homeFragmentViewModel
    }

    fun getTimelineViewModel(): TimelineViewModel {
        return viewModel.timelineFragmentViewModel
    }
}