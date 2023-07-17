package com.example.appnextexercise.ui.home_fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import com.example.appnextexercise.R
import com.example.appnextexercise.base.BaseFragment
import com.example.appnextexercise.charts.ChartXYMarkerView
import com.example.appnextexercise.charts.CustomXAxisRenderer
import com.example.appnextexercise.databinding.HomeFragmentBinding
import com.example.appnextexercise.model.DailyItem
import com.example.appnextexercise.ui.main.MainActivity
import com.example.appnextexercise.utils.AppUtils
import com.example.appnextexercise.utils.animation.PushDownAnim
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.util.Calendar

class HomeFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate) {

    var _selectedYIndex = 0f
    var data: List<DailyItem> = mutableListOf()
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        // check if user still in screen so we dont get null
        if (isVisible) {
            binding.homeBarChart.highlightValue(null)
        }
    }


    val homeViewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.InitData(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTimelineButton()
        homeViewModel.data.observe(viewLifecycleOwner) {
            data = it
            setupBarChart()
        }
    }

    private fun setTimelineButton() {
        PushDownAnim().setPushDownAnimTo(binding.timelineButton)
        binding.timelineButton.setOnClickListener {
            (activity as MainActivity).switchToTimelineFragment()
        }
    }

    private fun setupBarChart() {
        binding.homeBarChart.description.isEnabled = false
        binding.homeBarChart.isDragEnabled = false
        binding.homeBarChart.setPinchZoom(false)
        binding.homeBarChart.isDoubleTapToZoomEnabled = false
        binding.homeBarChart.setFitBars(true)
        binding.homeBarChart.animateY(1000)
        binding.homeBarChart.extraBottomOffset = 35f
        binding.homeBarChart.setDrawBarShadow(false)

        val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        val calendar = Calendar.getInstance()
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

        val distanceWalkList: MutableList<Float> = mutableListOf()
        val goalList: MutableList<Float> = mutableListOf()

        for (dailItem in data) {
            distanceWalkList.add(dailItem.daily_activity.toFloat())
            goalList.add(dailItem.daily_goal.toFloat())
        }

        val entriesDistanceWalk = mutableListOf<BarEntry>()
        val entriesGoalDistance = mutableListOf<BarEntry>()

        for (i in daysOfWeek.indices) {
            entriesDistanceWalk.add(BarEntry(i.toFloat(), distanceWalkList[i], data[i]))
            entriesGoalDistance.add(BarEntry(i.toFloat(), goalList[i], data[i]))
        }

        val setDistanceWalk = BarDataSet(entriesDistanceWalk, getString(R.string.activity))
        setDistanceWalk.color = resources.getColor(R.color.blue_indicator, null)
        setDistanceWalk.setDrawValues(false)

        val setGoalDistance = BarDataSet(entriesGoalDistance, getString(R.string.daily_goal_lowercase))
        setGoalDistance.color = resources.getColor(R.color.green_indicator,null)
        setGoalDistance.setDrawValues(false)

        val data = BarData(setDistanceWalk, setGoalDistance)
        data.barWidth = 0.3f
        data.setValueFormatter(LargeValueFormatter())
        data.setValueTypeface(AppUtils.getRobotoLightFont(context))

        binding.homeBarChart.data = data
        binding.homeBarChart.groupBars(-0.5f, 0.36f, 0.02f)

        val xAxis = binding.homeBarChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(daysOfWeek)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.yOffset = 10f;
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        xAxis.labelCount = daysOfWeek.size

        val renderer = CustomXAxisRenderer(
            binding.homeBarChart.viewPortHandler,
            binding.homeBarChart.xAxis,
            binding.homeBarChart.getTransformer(YAxis.AxisDependency.LEFT),
            currentDayOfWeek
        )
        binding.homeBarChart.setXAxisRenderer(renderer)

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

        binding.homeBarChart.setOnChartGestureListener(object : OnChartGestureListener {
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

            override fun onChartFling(
                me1: MotionEvent,
                me2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ) {
            }

            override fun onChartScale(me: MotionEvent, scaleX: Float, scaleY: Float) {}
            override fun onChartTranslate(me: MotionEvent, dX: Float, dY: Float) {}
        })

        binding.homeBarChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight) {
                if (_selectedYIndex != 0f && h != null && h.yPx <= _selectedYIndex) {
                    val selectedDailyItem = e.data as DailyItem
                    val mv = ChartXYMarkerView(requireContext(), selectedDailyItem)
                    mv.setChartView(binding.homeBarChart)
                    binding.homeBarChart.setMarker(mv)
                    // Post a delayed runnable to close the marker after five seconds
                    handler.removeCallbacks(runnable)
                    handler.postDelayed(runnable, 5000)

                } else {
                    binding.homeBarChart.highlightValue(null)
                }
            }

            override fun onNothingSelected() {
            }
        })

    }

}