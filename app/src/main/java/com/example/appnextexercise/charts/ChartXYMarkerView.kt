package com.example.appnextexercise.charts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.widget.TextView
import com.example.appnextexercise.R
import com.example.appnextexercise.model.DailyItem
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlin.Float

class ChartXYMarkerView(context: Context, dailyItem: DailyItem) : MarkerView(context, R.layout.bar_marker) {

    companion object {
        const val ARROW_SIZE = 40
        const val CIRCLE_OFFSET = 10f
        const val STOKE_WIDTH = 0f
    }

    init {
        val dailyGoalTv = findViewById<TextView>(R.id.bar_marker_daily_goal)
        val dailyActivityTv = findViewById<TextView>(R.id.bar_marker_daily_activity)
        dailyGoalTv.text = dailyItem.daily_activity.toString() + "/"
        // if we want to change text color to green if goal is reached
        if (dailyItem.daily_activity >= dailyItem.daily_goal) {
            dailyGoalTv.setTextColor(resources.getColor(R.color.green_indicator))
        }
        dailyActivityTv.text = dailyItem.daily_goal.toString() + resources.getString(R.string.steps)
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }

    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
        val offset = offset
        val chart = chartView
        val width = width.toFloat()
        val height = height.toFloat()
        if (posY <= height + ARROW_SIZE) {
            offset.y = ARROW_SIZE.toFloat()
        } else {
            offset.y = -height - ARROW_SIZE - STOKE_WIDTH
        }
        if (posX > chart.width - width) {
            offset.x = -width
        } else {
            offset.x = 0f
            if (posX > width / 2) {
                offset.x = -(width / 2)
            }
        }
        return offset
    }
    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        val paint = Paint()
        paint.strokeWidth = STOKE_WIDTH
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.color = resources.getColor(R.color.transparent, null)
        val markerColor = Paint()
        markerColor.style = Paint.Style.FILL
        markerColor.color = resources.getColor(R.color.marker_color, null)
        val chart = chartView
        val width = width.toFloat()
        val height = height.toFloat()
        val offset = getOffsetForDrawingAtPoint(posX, posY)
        val saveId = canvas.save()
        var path = Path()
        if (posY < height + ARROW_SIZE) {
            path = Path()
            path.moveTo(0f, 0f)
            if (posX > chart.width - width) {
                path.lineTo(width - ARROW_SIZE, 0f)
                path.lineTo(width, -ARROW_SIZE + CIRCLE_OFFSET)
                path.lineTo(width, 0f)
            } else {
                if (posX > width / 2) {
                    path.lineTo(width / 2 - ARROW_SIZE / 2, 0f)
                    path.lineTo(
                        width / 2,
                        -ARROW_SIZE + CIRCLE_OFFSET
                    )
                    path.lineTo(width / 2 + ARROW_SIZE / 2, 0f)
                } else {
                    path.lineTo(0f, -ARROW_SIZE + CIRCLE_OFFSET)
                    path.lineTo((0 + ARROW_SIZE).toFloat(), 0f)
                }
            }
            path.lineTo(0 + width, 0f)
            path.lineTo(0 + width, 0 + height)
            path.lineTo(0f, 0 + height)
            path.lineTo(0f, 0f)
            path.offset(posX + offset.x, posY + offset.y)
        } else {
            path = Path()
            path.moveTo(0f, 0f)
            path.lineTo(0 + width, 0f)
            path.lineTo(0 + width, 0 + height)
            if (posX > chart.width - width) {
                path.lineTo(
                    width,
                    height + ARROW_SIZE - CIRCLE_OFFSET
                )
                path.lineTo(width - ARROW_SIZE, 0 + height)
                path.lineTo(0f, 0 + height)
            } else {
                if (posX > width / 2) {
                    path.lineTo(width / 2 + ARROW_SIZE / 2, 0 + height)
                    path.lineTo(
                        width / 2,
                        height + ARROW_SIZE - CIRCLE_OFFSET
                    )
                    path.lineTo(width / 2 - ARROW_SIZE / 2, 0 + height)
                    path.lineTo(0f, 0 + height)
                } else {
                    path.lineTo((0 + ARROW_SIZE).toFloat(), 0 + height)
                    path.lineTo(
                        0f,
                        height + ARROW_SIZE - CIRCLE_OFFSET
                    )
                    path.lineTo(0f, 0 + height)
                }
            }
            path.lineTo(0f, 0f)
            path.offset(posX + offset.x, posY + offset.y)
        }


        // translate to the correct position and draw
        canvas.drawPath(path, markerColor)
        canvas.drawPath(path, paint)
        canvas.translate(posX + offset.x, posY + offset.y)
        draw(canvas)
        canvas.restoreToCount(saveId)
    }

}