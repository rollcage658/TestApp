package com.example.appnextexercise.ui.timeline_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.appnextexercise.adapters.DailyItemTimelineAdapter
import com.example.appnextexercise.base.BaseFragment
import com.example.appnextexercise.databinding.TimelineFragmentBinding

class TimelineFragment : BaseFragment<TimelineFragmentBinding>(TimelineFragmentBinding::inflate) {

    val timelineViewModel: TimelineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timelineViewModel.initData(requireContext())
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.monthName.text = timelineViewModel.getMonthName()

        val adapter = DailyItemTimelineAdapter()
        binding.rvTimeline.adapter = adapter

        // Observe on days var for data change and add to adapter
        timelineViewModel.days.observe(viewLifecycleOwner, Observer {
            adapter.setDaysList(it)
        } )
    }

}