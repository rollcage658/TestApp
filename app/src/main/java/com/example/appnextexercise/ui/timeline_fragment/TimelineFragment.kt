package com.example.appnextexercise.ui.timeline_fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.appnextexercise.base.BaseFragment
import com.example.appnextexercise.databinding.TimelineFragmentBinding


class TimelineFragment : BaseFragment<TimelineFragmentBinding>(TimelineFragmentBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val timelineViewModel =
            ViewModelProvider(this).get(TimelineViewModel::class.java)
    }
}