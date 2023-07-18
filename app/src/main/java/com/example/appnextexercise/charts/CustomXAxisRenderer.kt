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

        val positions = FloatArray(mXAxis.mEntryCount)

        for (i in 0 until mXAxis.mEntryCount) {
            positions[i] = mXAxis.mEntries[i]
        }

        mTrans.pointValuesToPixel(positions)

        //TODO later when there is time try fix line
        for (i in 0 until mXAxis.mEntryCount) {
            if (i  == currentDayOfWeek) {
                val x = positions[(i)]
                if (i == 3) {
                    c.drawLine(x - 90 , mViewPortHandler.contentBottom() + 80 , x, mViewPortHandler.contentBottom() + 80, paint)
                } else {
                    c.drawLine(x - 40, mViewPortHandler.contentBottom() + 80 , x + 40, mViewPortHandler.contentBottom() + 80, paint)
                }
                break
            }
        }
    }
}