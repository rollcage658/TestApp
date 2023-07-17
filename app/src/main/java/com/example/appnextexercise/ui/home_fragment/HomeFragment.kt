package com.example.appnextexercise.ui.home_fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.example.appnextexercise.base.BaseFragment
import com.example.appnextexercise.databinding.HomeFragmentBinding
import com.example.appnextexercise.ui.main.MainActivity
import com.example.appnextexercise.utils.animation.PushDownAnim

class HomeFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate) {

    val homeViewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.getAndSetData(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.data.observe(viewLifecycleOwner) {
            //TODO have data
            Log.d("HomeFragment", "homeViewModel.data.value: " + (homeViewModel.data.value))
            Log.d("HomeFragment", "homeViewModel.data.value size: " + (homeViewModel.data.value?.size)
            )
        }

        setTimelineButton()
    }
    private fun setTimelineButton() {
        PushDownAnim().setPushDownAnimTo(binding.timelineButton)
        binding.timelineButton.setOnClickListener {
            (activity as MainActivity).switchToTimelineFragment()
        }
    }

}