package com.example.appnextexercise.charts

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler

class CustomXAxisRenderer (
    viewPortHandler: ViewPortHandler,
    xAxis: XAxis,
    trans: Transformer,
    private val currentDayOfWeek: Int
) : XAxisRenderer(viewPortHandler, xAxis, trans) {

    override fun drawLabels(c: Canvas, pos: Float, anchor: MPPointF) {
        super.drawLabels(c, pos, anchor)

        val paint = Paint()
        paint.color = Color.BLUE
        paint.strokeWidth = 7f

        val paintGray = Paint()
        paintGray.color = Color.LTGRAY
        paintGray.strokeWidth = 3f

        val positions = FloatArray(mXAxis.mEntryCount * 2)

        for (i in mXAxis.mEntryCount downTo 1) {
            positions[i] = mXAxis.mEntries[i - 1]
//            val x = positions[i]
//            c.drawLine(x - 400 , mViewPortHandler.contentBottom() + 80 , x +400, mViewPortHandler.contentBottom() + 80, paintGray)
        }

        mTrans.pointValuesToPixel(positions)

        for (i in mXAxis.mEntryCount downTo 1) {
            if (i - 1 == currentDayOfWeek) {
                val x = positions[(i - 1) * 2]
                c.drawLine(x - 40 , mViewPortHandler.contentBottom() + 80 , x +40, mViewPortHandler.contentBottom() + 80, paint)
            }
        }
    }
}