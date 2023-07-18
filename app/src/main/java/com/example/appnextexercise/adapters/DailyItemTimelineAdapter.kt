package com.example.appnextexercise.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.appnextexercise.databinding.DailyItemTimelineBinding
import com.example.appnextexercise.model.DailyItemTimeline

class DailyItemTimelineAdapter : RecyclerView.Adapter<DailyItemTimelineAdapter.ViewHolder>() {
    var days: List<DailyItemTimeline> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DailyItemTimelineBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int = days.size

    inner class ViewHolder(private val binding: DailyItemTimelineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dayItem: DailyItemTimeline) {
//            binding.date.text = dayItem.date.format(DateTimeFormatter.ofPattern("EEE, d MMM"))
            binding.dailyActivity.text = dayItem.dailyActivity.toString()
            binding.dailyGoal.text = dayItem.dailyGoal.toString()
            binding.dailyDistanceMeters.text = dayItem.dailyDistanceMeters.toString()
            binding.dailyKcal.text = dayItem.dailyKcal.toString()

            binding.blueLine.isVisible = dayItem.isCurrentDay
        }
    }
}