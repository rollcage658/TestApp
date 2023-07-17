package com.example.appnextexercise.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.appnextexercise.R
import com.example.appnextexercise.databinding.ActivityMainBinding
import com.example.appnextexercise.utils.animation.PushDownAnim
import java.util.Calendar


// this mainActivity control app flow which show homeFragment and if clicked timelineFragment
// the viewModel gets data from server and each fragment accesses its needed data
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        handleData()
        setBackButton()
    }

    private fun handleData() {
        viewModel.fetchWeeklyData(this, Calendar.getInstance().timeInMillis)
        // wait for resultList to populate then show homeFragment
        viewModel.isDataAvailable.observe(this, Observer {
            if (it) {
                switchToHomeFragment()
            } else {
                // sort of error handing if something went wrong fetching data
                Toast.makeText(this,"Cant fetch data from server", Toast.LENGTH_LONG).show()
            }
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

}