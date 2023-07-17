package com.example.appnextexercise.ui.home_fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import com.example.appnextexercise.R
import com.example.appnextexercise.base.BaseFragment
import com.example.appnextexercise.charts.ChartXYMarkerView
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
import java.util.Locale


class HomeFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate) {

    val listOfDays = listOf("Sun", "Mon", "The", "Wed", "Thu", "Fri", "Sat")
    var _selectedYIndex = 0f
    var data: List<DailyItem> = mutableListOf()


    val homeViewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.InitData(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTimelineButton()
//        initBarChart()
        homeViewModel.data.observe(viewLifecycleOwner) {
            data = it
            setupBarChart()
//            setBarChartData()


        }

    }

    private fun setTimelineButton() {
        PushDownAnim().setPushDownAnimTo(binding.timelineButton)
        binding.timelineButton.setOnClickListener {
            (activity as MainActivity).switchToTimelineFragment()
        }
    }

    private fun initBarChart() {

        binding.homeBarChart.setDrawValueAboveBar(false)
        binding.homeBarChart.getDescription().setEnabled(false)
        binding.homeBarChart.setDragEnabled(false)
        binding.homeBarChart.setPinchZoom(true)
        binding.homeBarChart.setDoubleTapToZoomEnabled(false)
        binding.homeBarChart.setFitBars(true)
        binding.homeBarChart.animateY(1500)
        binding.homeBarChart.setDrawBarShadow(false)
        binding.homeBarChart.setExtraBottomOffset(10f)

        val xAxis: XAxis = binding.homeBarChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.typeface = AppUtils.getRobotoFont(context)
        xAxis.setDrawGridLines(false)
//        xAxis.setGranularity(1f);
        val xAxisFormatter: IndexAxisValueFormatter
        val xAxisLables = ArrayList<String>()
        // Add days label
        for (day in listOfDays) {
            xAxisLables.add(day)
        }
        xAxisFormatter = IndexAxisValueFormatter(xAxisLables)

        xAxis.valueFormatter = xAxisFormatter
        xAxis.textColor = resources.getColor(R.color.blue, null)
        xAxis.axisLineColor = resources.getColor(R.color.transparent, null)
        xAxis.axisLineWidth = 2.5f
        xAxis.setDrawGridLines(false)

//        val custom: ValueFormatter = BarChartAxisValueFormatter()

        val leftAxis: YAxis = binding.homeBarChart.getAxisLeft()
        leftAxis.typeface = AppUtils.getRobotoFont(context)
        leftAxis.setLabelCount(7, false)
//        leftAxis.valueFormatter = custom as ValueFormatter
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 5f
        leftAxis.textColor = resources.getColor(R.color.transparent, null)
        leftAxis.axisLineColor = resources.getColor(R.color.transparent, null)
        leftAxis.axisLineWidth = 2.5f
        leftAxis.gridLineWidth = 1f
        leftAxis.gridColor = resources.getColor(R.color.transparent, null)


        // TODO later return this code and modify to show correct layout
//        MarkerView mv = new MarkerView(getContext(), R.layout.sharvit_resource_item_mobile);
//        mv.setChartView(barChart); // For bounds control
//        barChart.setMarker(mv); // Set the marker to the chart


        // TODO later return this code and modify to show correct layout
//        MarkerView mv = new MarkerView(getContext(), R.layout.sharvit_resource_item_mobile);
//        mv.setChartView(barChart); // For bounds control
//        barChart.setMarker(mv); // Set the marker to the chart
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

        binding.homeBarChart.setVisibleYRangeMinimum(0f, leftAxis.axisDependency)
        binding.homeBarChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight) {
//                val myRange = Range.create(0f, h.yPx + 250f)
//                val myRangeX = Range.create(0f, h.xPx)
                if (_selectedYIndex != 0f && h != null && h.yPx <= _selectedYIndex) {
//                    val selectedBar: DashboardDetailsResourcesTab.Equipment =
//                        e.data as DashboardDetailsResourcesTab.Equipment
//                    setSelectedAmountViews(selectedBar)
                    val mv = ChartXYMarkerView(context, R.layout.bar_marker)
                    mv.setChartView(binding.homeBarChart)
                    binding.homeBarChart.setMarker(mv)
                } else {
                    binding.homeBarChart.highlightValue(null)
                }
            }

            override fun onNothingSelected() {
//                hideSelectedInfo(false)
            }
        })

    }
    private fun setBarChartData() {
        val xAxis: XAxis = binding.homeBarChart.getXAxis()
        xAxis.labelCount = homeViewModel.data.value?.size!!
        val values1 = ArrayList<BarEntry>()
        val values2 = ArrayList<BarEntry>()
        val values3 = ArrayList<BarEntry>()
        val values4 = ArrayList<BarEntry>()
        val values5 = ArrayList<BarEntry>()
        val values6 = ArrayList<BarEntry>()
        val values7 = ArrayList<BarEntry>()

        values1.add(BarEntry(homeViewModel.data.value!!.get(0).daily_activity.toFloat(), homeViewModel.data.value!!.get(0).daily_goal.toFloat()))
        values2.add(BarEntry(homeViewModel.data.value!!.get(1).daily_activity.toFloat(), homeViewModel.data.value!!.get(1).daily_goal.toFloat()))
        values3.add(BarEntry(homeViewModel.data.value!!.get(2).daily_activity.toFloat(), homeViewModel.data.value!!.get(2).daily_goal.toFloat()))
        values4.add(BarEntry(homeViewModel.data.value!!.get(3).daily_activity.toFloat(), homeViewModel.data.value!!.get(3).daily_goal.toFloat()))
        values5.add(BarEntry(homeViewModel.data.value!!.get(4).daily_activity.toFloat(), homeViewModel.data.value!!.get(4).daily_goal.toFloat()))
        values6.add(BarEntry(homeViewModel.data.value!!.get(5).daily_activity.toFloat(), homeViewModel.data.value!!.get(5).daily_goal.toFloat()))
        values7.add(BarEntry(homeViewModel.data.value!!.get(6).daily_activity.toFloat(), homeViewModel.data.value!!.get(6).daily_goal.toFloat()))

        val set1: BarDataSet
        val set2: BarDataSet
        val set3: BarDataSet
        val set4: BarDataSet
        val set5: BarDataSet
        val set6: BarDataSet
        val set7: BarDataSet

        if (binding.homeBarChart.data != null && binding.homeBarChart.data.getDataSetCount() > 0) {
            set1 = binding.homeBarChart.data.getDataSetByIndex(0) as BarDataSet
            set2 = binding.homeBarChart.data.getDataSetByIndex(1) as BarDataSet
            set3 = binding.homeBarChart.data.getDataSetByIndex(2) as BarDataSet
            set4 = binding.homeBarChart.data.getDataSetByIndex(3) as BarDataSet
            set5 = binding.homeBarChart.data.getDataSetByIndex(4) as BarDataSet
            set6 = binding.homeBarChart.data.getDataSetByIndex(5) as BarDataSet
            set7 = binding.homeBarChart.data.getDataSetByIndex(6) as BarDataSet
            set1.values = values1
            set2.values = values2
            set3.values = values3
            set4.values = values4
            set5.values = values5
            set6.values = values6
            set7.values = values7
            binding.homeBarChart.data.notifyDataChanged()
            binding.homeBarChart.notifyDataSetChanged()
        } else {
            // create 7 DataSets
            set1 = BarDataSet(values1, "Sun")
            set1.barBorderColor = resources.getColor(R.color.teal_700, null)
            set1.barBorderWidth = 3f
            set1.color = Color.rgb(104, 241, 175)
            set2 = BarDataSet(values2, "Monday")
            set2.color = Color.rgb(164, 228, 251)
            set3 = BarDataSet(values3, "Thursday")
            set3.color = Color.rgb(242, 247, 158)
            set4 = BarDataSet(values4, "Friday")
            set4.color = Color.rgb(255, 102, 0)
            val data = BarData(set1, set2, set3, set4)
            data.setValueFormatter(LargeValueFormatter())
            data.setValueTypeface(AppUtils.getRobotoFont(context))
            binding.homeBarChart.data = data
        }


    }

    private fun setupBarChart() {
        binding.homeBarChart.description.isEnabled = false
        binding.homeBarChart.isDragEnabled = false
        binding.homeBarChart.setPinchZoom(false)
        binding.homeBarChart.isDoubleTapToZoomEnabled = false
        binding.homeBarChart.setFitBars(true)
        binding.homeBarChart.animateY(1000)
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
            entriesDistanceWalk.add(BarEntry(i.toFloat(), distanceWalkList[i]))
            entriesGoalDistance.add(BarEntry(i.toFloat(), goalList[i]))
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
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        xAxis.labelCount = daysOfWeek.size

        for (i in daysOfWeek.indices) {
            if (i == currentDayOfWeek) {
                Log.d("HomeFragment", "setupBarChart: currentDayOfWeek i= " + i)
                Log.d("HomeFragment", "setupBarChart: currentDayOfWeek = " + currentDayOfWeek)
                xAxis.getFormattedLabel(i).toUpperCase(Locale.getDefault())
            }
        }

        binding.homeBarChart.axisRight.isEnabled = false
        binding.homeBarChart.axisLeft.isEnabled = false

        val legend = binding.homeBarChart.legend
        legend.typeface = AppUtils.getRobotoLightFont(context)
        legend.xEntrySpace = 15f
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.form = Legend.LegendForm.CIRCLE
        legend.textSize = 11f
//        legend.textSize = AppUtils.dpToPx(requireContext(), 6f).toFloat()

        binding.homeBarChart.notifyDataSetChanged()
        binding.homeBarChart.invalidate()

    }

}