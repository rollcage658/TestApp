package com.example.appnextexercise.ui.home_fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import com.example.appnextexercise.base.BaseFragment
import com.example.appnextexercise.charts.ChartXYMarkerView
import com.example.appnextexercise.charts.CustomXAxisRenderer
import com.example.appnextexercise.databinding.HomeFragmentBinding
import com.example.appnextexercise.model.DailyItem
import com.example.appnextexercise.ui.home_fragment.HomeViewModel.Companion.currentDayOfWeek
import com.example.appnextexercise.ui.home_fragment.HomeViewModel.Companion.daysOfWeek
import com.example.appnextexercise.ui.main.MainActivity
import com.example.appnextexercise.utils.AppUtils
import com.example.appnextexercise.utils.animation.PushDownAnim
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class HomeFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate) {

    val homeViewModel: HomeViewModel by viewModels()

    var _selectedYIndex = 0f
    var data: List<DailyItem> = mutableListOf()
    private val handler = Looper.myLooper()?.let { Handler(it) }
    private val runnable = Runnable {
        // Check if user still in screen so we dont get null
        if (isVisible) {
            binding.homeBarChart.highlightValue(null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.initData(requireContext())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTimelineButton()
        // Observe for data change and show
        homeViewModel.data.observe(viewLifecycleOwner) {
            setupBarChart()
        }
    }
    // Sets an animation to the back button and sets an onClickListener to it that calls the switchToTimelineFragment method when clicked
    private fun setTimelineButton() {
        PushDownAnim().setPushDownAnimTo(binding.timelineButton)
        binding.timelineButton.setOnClickListener {
            (activity as MainActivity).switchToTimelineFragment()
        }
    }

    // Sets barChart looks and gets data from homeViewModel
    private fun setupBarChart() {
        binding.homeBarChart.description.isEnabled = false
        binding.homeBarChart.isDragEnabled = false
        binding.homeBarChart.setPinchZoom(false)
        binding.homeBarChart.isDoubleTapToZoomEnabled = false
        binding.homeBarChart.setFitBars(true)
        binding.homeBarChart.animateY(1000)
        binding.homeBarChart.setScaleEnabled(false)
        binding.homeBarChart.extraBottomOffset = 35f
        binding.homeBarChart.setDrawBarShadow(false)

        binding.homeBarChart.data = homeViewModel.getDataForChart(requireContext())
        binding.homeBarChart.groupBars(-0.5f, 0.36f, 0.02f)

        val xAxis = binding.homeBarChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(daysOfWeek)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.labelCount = daysOfWeek.size

        binding.homeBarChart.axisRight.isEnabled = false
        val yAxis = binding.homeBarChart.axisLeft
        yAxis.axisMinimum = 0f
        yAxis.setDrawGridLines(false)
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawLabels(false)

        val legend = binding.homeBarChart.legend
        legend.typeface = AppUtils.getRobotoLightFont(context)
        legend.xEntrySpace = 15f
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.form = Legend.LegendForm.CIRCLE
        legend.textSize = 11f

        binding.homeBarChart.notifyDataSetChanged()
        binding.homeBarChart.invalidate()

        binding.homeBarChart.onChartGestureListener = object : OnChartGestureListener {
            override fun onChartGestureStart(me: MotionEvent, lastPerformedGesture: ChartGesture) {}
            override fun onChartGestureEnd(me: MotionEvent, lastPerformedGesture: ChartGesture) {}
            override fun onChartLongPressed(me: MotionEvent) {}
            override fun onChartDoubleTapped(me: MotionEvent) {}
            override fun onChartSingleTapped(me: MotionEvent) {
                val entry: Entry = binding.homeBarChart.getEntryByTouchPoint(me.x, me.y)
                if (entry != null) {
                    _selectedYIndex = me.y
                } else {
                    _selectedYIndex = 0f
                }
            }
            override fun onChartFling(me1: MotionEvent, me2: MotionEvent, velocityX: Float, velocityY: Float) {}
            override fun onChartScale(me: MotionEvent, scaleX: Float, scaleY: Float) {}
            override fun onChartTranslate(me: MotionEvent, dX: Float, dY: Float) {}
        }

        binding.homeBarChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight) {
                if (_selectedYIndex != 0f && h != null && h.yPx <= _selectedYIndex) {
                    val selectedDailyItem = e.data as DailyItem
                    val mv = ChartXYMarkerView(requireContext(), selectedDailyItem)
                    mv.chartView = binding.homeBarChart
                    binding.homeBarChart.marker = mv
                    // Post a delayed runnable to close the marker after five seconds
                    handler?.removeCallbacks(runnable)
                    handler?.postDelayed(runnable, 5000)
                } else {
                    binding.homeBarChart.highlightValue(null)
                }
            }

            override fun onNothingSelected() {
            }
        })
    }

}