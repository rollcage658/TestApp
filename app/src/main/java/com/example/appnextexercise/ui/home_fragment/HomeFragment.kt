package com.example.appnextexercise.ui.home_fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.appnextexercise.base.BaseFragment
import com.example.appnextexercise.databinding.HomeFragmentBinding
import com.example.appnextexercise.ui.main.MainActivity
import com.example.appnextexercise.utils.animation.PushDownAnim

class HomeFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeViewModel =
            (activity as MainActivity).getHomeViewModel()

        PushDownAnim().setPushDownAnimTo(binding.timelineButton)
        binding.timelineButton.setOnClickListener {
            (activity as MainActivity).switchToTimelineFragment()
        }

        //TODO need to fix homeViewModel variable its not the same as mainActivityViewModel
        //TODO תסתכל מוואצאפ
        Log.d("HomeFragment", "homeViewModel.data.value: " + (homeViewModel.data.value))
        Log.d("HomeFragment", "homeViewModel.data.value size: " + (homeViewModel.data.value?.size))
    }


}