package com.example.appnextexercise.adapters

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appnextexercise.R
import com.example.appnextexercise.databinding.DailyItemTimelineBinding
import com.example.appnextexercise.model.DailyItemTimeline
import com.example.appnextexercise.utils.AppUtils
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlin.Int
import kotlin.apply

class DailyItemTimelineAdapter : RecyclerView.Adapter<DailyItemTimelineAdapter.ViewHolder>() {

    private var days = mutableListOf<DailyItemTimeline>()

    fun setDaysList( movies: List<DailyItemTimeline>) {
        days = movies.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DailyItemTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            bind(days[position])
            if (days[position].dailyActivity >= days[position].dailyGoal) {
                holder.binding.dailyActivity.setTextColor(itemView.resources.getColor(R.color.green_indicator))
            } else {
                holder.binding.dailyActivity.setTextColor(itemView.resources.getColor(R.color.blue_indicator))
            }
            holder.binding.dailyGoal.text = "/" + days[position].dailyGoal.toString()

            holder.binding.dailyKcal.text = formatKcal(days[position].dailyKcal, holder.binding.dailyKcal.context)
            holder.binding.dailyDistanceMeters.text = formatDistance(days[position].dailyDistanceMeters, holder.binding.dailyKcal.context)
            val isGoalAchieved = days[position].dailyActivity.toFloat() >= days[position].dailyGoal.toFloat()
            if (isGoalAchieved) {
                holder.binding.dailyDistanceMeters.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.green_indicator, 0, 0, 0)
                holder.binding.dailyKcal.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.green_indicator, 0, 0, 0)
            } else {
                holder.binding.dailyDistanceMeters.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.grey_indicator, 0, 0, 0)
                holder.binding.dailyKcal.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.grey_indicator, 0, 0, 0)
            }
            initPieChart(position, holder)
        }
    }

    fun formatKcal(dailyKcal: Int, context: Context): SpannableString {
        val kcalString = dailyKcal.toString() + " KCAL"
        val spannableTotal = SpannableString(kcalString)

        val startIndex = kcalString.indexOf("KCAL")
        val endIndex = startIndex + "KCAL".length

        spannableTotal.setSpan(
            StyleSpan(AppUtils.getRobotoBoldFont(context)!!.style),
            0,
            startIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableTotal.setSpan(
            StyleSpan(AppUtils.getRobotoRegularFont(context)!!.style),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableTotal
    }


//    fun formatKcal(dailyKcal: Int, context: Context) : SpannableString{
//        val kcalString = dailyKcal.toString() + " KCAL"
//        val spannableTotal = SpannableString(kcalString)
//
//        val startIndex = kcalString.indexOf("KCAL")
//        val endIndex = startIndex + "KCAL".length
//
//        spannableTotal.setSpan(
//            StyleSpan(AppUtils.getRobotoRegularFont(context)!!.style),
//            startIndex,
//            endIndex,
//            0
//        )
//        return  spannableTotal
//    }

    fun formatDistance(distanceInMeters: Int, context: Context): SpannableString {
        val spannableString = if (distanceInMeters >= 1000) {
            val distanceInKilometers = distanceInMeters / 1000.0
            val formattedDistance = if (distanceInKilometers % 1 == 0.0) {
                "%.0f KM".format(distanceInKilometers)
            } else {
                "%.1f KM".format(distanceInKilometers)
            }
            SpannableString(formattedDistance)
        } else {
            SpannableString("$distanceInMeters M")
        }

        spannableString.setSpan(
            StyleSpan(AppUtils.getRobotoBoldFont(context)!!.style),
            0,
            spannableString.indexOf(" "),
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        spannableString.setSpan(
            StyleSpan(AppUtils.getRobotoRegularFont(context)!!.style),
            spannableString.indexOf(" ") + 1,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    private fun initPieChart(position: Int, holder: ViewHolder) {
        val entries = mutableListOf<PieEntry>()
        val walkDistance = days[position].dailyActivity.toFloat()
        val walkGoal = days[position].dailyGoal.toFloat()
        entries.add(PieEntry(walkDistance, "Walk Distance"))
        entries.add(PieEntry(walkGoal, "Goal"))

        val dataSet = PieDataSet(entries, "Walk Data")
        // choose pieChart color depending if goal was achieved
        dataSet.colors = if (walkDistance >= walkGoal) {
            listOf(holder.binding.dailyKcal.resources.getColor(R.color.green_indicator, null))
        } else {
            listOf(holder.binding.dailyKcal.resources.getColor(R.color.blue_indicator, null), holder.binding.dailyKcal.resources.getColor(R.color.grey_light, null))
        }
        val data = PieData(dataSet)
        holder.binding.dailyPieChart.data = data
        holder.binding.dailyPieChart.setDrawCenterText(false)
        holder.binding.dailyPieChart.setDrawEntryLabels(false)
        holder.binding.dailyPieChart.description.isEnabled = false
        holder.binding.dailyPieChart.legend.isEnabled = false
        holder.binding.dailyPieChart.setDrawEntryLabels(false)
        holder.binding.dailyPieChart.isHighlightPerTapEnabled = false
        holder.binding.dailyPieChart.setTouchEnabled(false)
        holder.binding.dailyPieChart.holeRadius = 80f
        dataSet.setDrawValues(false)
        dataSet.setDrawIcons(false)

        holder.binding.dailyPieChart.invalidate()
    }

    override fun getItemCount(): Int = days.size

    inner class ViewHolder(val binding: DailyItemTimelineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: DailyItemTimeline) {
            binding.dayItem= day
        }
    }

}