package com.example.appnextexercise.charts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.text.SpannableString
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.databinding.ObservableField
import com.example.appnextexercise.R
import com.example.appnextexercise.model.DailyItem
import com.example.appnextexercise.utils.AppUtils
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.lang.String
import kotlin.Float
import kotlin.Int

class ChartXYMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {

    companion object {
        const val ARROW_SIZE = 40
        const val CIRCLE_OFFSET = 10f
        const val STOKE_WIDTH = 2f
    }

    var dailyGoal = ObservableField<kotlin.String>()
    var dailyActivity = ObservableField<kotlin.String>()

    fun ChartXYMarkerView(context: Context?, layoutResource: Int, dailyItem: DailyItem) {
        dailyGoal.set(dailyItem.daily_goal.toString())
        dailyActivity.set(dailyItem.daily_activity.toString())
        inflate(context, R.layout.bar_marker, this)

//        super(context, R.layout.bar_marker)
//        if (dailyItem != null) {
////            atWork = findViewById<TextView>(R.id.bar_marker_at_work)
////            total = findViewById<TextView>(R.id.bar_marker_total)
//            val spannableTotal = SpannableString(
//                customerInOutage.getTotalCustomersInOutage() + " " + resources.getString(R.string.clients)
//            )
//            spannableTotal.setSpan(
//                StyleSpan(
//                    AppUtils.getRobotoFont(getContext())?.getStyle()!!
//                ), 0, String.valueOf(customerInOutage.getTotalCustomersInOutage()).length, 0
//            )
//            total.setText(spannableTotal)
//            val spannableAtWork =
//                SpannableString(customerInOutage.getRange() + " " + resources.getString(R.string.hours_text))
//            spannableAtWork.setSpan(
//                StyleSpan(
//                    AppUtils.getRobotoFont(getContext())?.getStyle()!!
//                ), 0, customerInOutage.getRange().length(), 0
//            )
//            atWork.setText(spannableAtWork)
//        }
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
        paint.color = resources.getColor(R.color.teal_700, null)
        val whitePaint = Paint()
        whitePaint.style = Paint.Style.FILL
        whitePaint.color = Color.WHITE
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
        canvas.drawPath(path, whitePaint)
        canvas.drawPath(path, paint)
        canvas.translate(posX + offset.x, posY + offset.y)
        draw(canvas)
        canvas.restoreToCount(saveId)
    }
}