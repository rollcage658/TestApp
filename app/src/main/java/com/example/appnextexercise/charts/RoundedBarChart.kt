package com.example.appnextexercise.charts

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import com.example.appnextexercise.R
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.highlight.Range
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.model.GradientColor
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.utils.Transformer;
import java.util.Calendar


class RoundedBarChart : BarChart {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        readRadiusAttr(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        readRadiusAttr(context, attrs)
    }

    private val linePaint = Paint().apply {
        color = context.getColor(R.color.blue_indicator)
        strokeWidth = 7f
    }

//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
//
//        // Get the index of the entry for which to draw the line
////        val entryIndex = 0 // Change this value to specify a different entry
//
//        // Get the x-coordinate and width of the entry's bar
//        if (barData == null) return
//        val entry = barData.getDataSetByIndex(0).getEntryForIndex(currentDayOfWeek)
//        val x = entry.x
//        val width = barData.barWidth
//        val groupWidth = barData.barWidth + barData.getGroupWidth(0.36f, 0.02f)
////        val groupSpace = groupWidth / 2
//        val positions = floatArrayOf(x - width / 2 /*- groupSpace*/, x + width / 2 /*+ groupSpace*/)
//        getTransformer(YAxis.AxisDependency.RIGHT).pointValuesToPixel(positions)
//
//        // Draw the line from the start to the end of the entry's bar
//        val xStart = positions[0]
//        val xEnd = positions[1]
//        canvas.drawLine(xStart, mViewPortHandler.contentBottom() + 80, xEnd , mViewPortHandler.contentBottom() + 80, linePaint)
//    }



    // couldn't make it work properly :/
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Get the current day of the week as an index (e.g. Sunday = 0, Monday = 1, etc.)
        val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1

        // Get the x-coordinates of the bars
        if (barData == null) return
        val positions = FloatArray(barData.dataSetCount)

        for (i in 0 until barData.dataSetCount) {
            positions[i] = barData.getDataSetByIndex(i).getEntryForIndex(currentDayOfWeek).x
        }

//        getTransformer(YAxis.AxisDependency.LEFT).pointValuesToPixel(positions)

        // Draw the line under the current day's bar
//        val xStart = positions.first()
//        val xEnd = positions.last()


//        canvas.drawLine(xStart, mViewPortHandler.contentBottom() + 80, xEnd , mViewPortHandler.contentBottom() + 80, linePaint)

        // a bit of ugly code but that work...
        // i thought this bug will be quick to fix so i left it to the end.. BUT after about 5 hours trying to get it working properly with bar pixel location
        when (positions.first()) {
            // Sun
            -0.16F -> canvas.drawLine(100f, mViewPortHandler.contentBottom() + 80, 170f , mViewPortHandler.contentBottom() + 80, linePaint)
            // Mon
            0.84000003F -> canvas.drawLine(235f, mViewPortHandler.contentBottom() + 80, 305f , mViewPortHandler.contentBottom() + 80, linePaint)
            // Tue
            1.84F -> canvas.drawLine(370f, mViewPortHandler.contentBottom() + 80, 440f , mViewPortHandler.contentBottom() + 80, linePaint)
            // Wed
            2.8400002F -> canvas.drawLine(505f, mViewPortHandler.contentBottom() + 80, 585f , mViewPortHandler.contentBottom() + 80, linePaint)
            // Thu
            3.8400002F -> canvas.drawLine(645f, mViewPortHandler.contentBottom() + 80, 715f , mViewPortHandler.contentBottom() + 80, linePaint)
            // Fri
            4.84F -> canvas.drawLine(780f, mViewPortHandler.contentBottom() + 80, 850f , mViewPortHandler.contentBottom() + 80, linePaint)
            // Sat
            5.84F -> canvas.drawLine(910f, mViewPortHandler.contentBottom() + 80, 980f , mViewPortHandler.contentBottom() + 80, linePaint)
            else -> {
                Log.e("RoundedBarChart", "onDraw position error", )
            }
        }
    }

    private fun readRadiusAttr(context: Context, attrs: AttributeSet) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.RoundedBarChart, 0, 0)
        try {
            radius = a.getDimensionPixelSize(R.styleable.RoundedBarChart_radius, 0)
        } finally {
            a.recycle()
        }
    }

    var radius: Int = 0
        set(value) {
            field = value
            renderer = RoundedBarChartRenderer(this, animator, viewPortHandler, value)
        }

    private class RoundedBarChartRenderer(
        chart: BarDataProvider?,
        animator: ChartAnimator?,
        viewPortHandler: ViewPortHandler?,
        private val mRadius: Int
    ) :
        BarChartRenderer(chart, animator, viewPortHandler) {
        private val mBarShadowRectBuffer = RectF()
        override fun drawHighlighted(c: Canvas, indices: Array<Highlight>) {
            val barData = mChart.barData
            for (high in indices) {
                val set = barData.getDataSetByIndex(high.dataSetIndex)
                if (set == null || !set.isHighlightEnabled) continue
                val e = set.getEntryForXValue(high.x, high.y)
                if (!isInBoundsX(e, set)) continue
                val trans: Transformer = mChart.getTransformer(set.axisDependency)
                mHighlightPaint.color = set.highLightColor
                mHighlightPaint.alpha = set.highLightAlpha
                val isStack = high.stackIndex >= 0 && e.isStacked
                val y1: Float
                val y2: Float
                if (isStack) {
                    if (mChart.isHighlightFullBarEnabled) {
                        y1 = e.positiveSum
                        y2 = -e.negativeSum
                    } else {
                        val range: Range = e.ranges[high.stackIndex]
                        y1 = range.from
                        y2 = range.to
                    }
                } else {
                    y1 = e.y
                    y2 = 0f
                }
                prepareBarHighlight(e.x, y1, y2, barData.barWidth / 2f, trans)
                setHighlightDrawPos(high, mBarRect)
                c.drawRoundRect(mBarRect, mRadius.toFloat(), mRadius.toFloat(), mHighlightPaint)
            }
        }

        override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
            val trans = mChart.getTransformer(dataSet.axisDependency)
            mBarBorderPaint.color = dataSet.barBorderColor
            mBarBorderPaint.strokeWidth = Utils.convertDpToPixel(dataSet.barBorderWidth)
            val drawBorder = dataSet.barBorderWidth > 0f
            val phaseX = mAnimator.phaseX
            val phaseY = mAnimator.phaseY

            // initialize the buffer
            val buffer = mBarBuffers[index]
            buffer.setPhases(phaseX, phaseY)
            buffer.setDataSet(index)
            buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
            buffer.setBarWidth(mChart.barData.barWidth)
            buffer.feed(dataSet)
            trans.pointValuesToPixel(buffer.buffer)
            val isSingleColor = dataSet.colors.size == 1
            if (isSingleColor) {
                mRenderPaint.color = dataSet.color
            }
            var j = 0
            while (j < buffer.size()) {
                if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                    j += 4
                    continue
                }
                if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break

                // Create a path object for the bar with round corners only at the top
                val path = Path()

                // Get the coordinates of the bar from the buffer
                val left = buffer.buffer[j]
                val top = buffer.buffer[j + 1]
                val right = buffer.buffer[j + 2]
                val bottom = buffer.buffer[j + 3]

                // Move the path to the bottom left corner of the bar
                path.moveTo(left, bottom)

                // Draw a line from the bottom left corner to the top left corner of the bar
                path.lineTo(left, top + mRadius)

                // Draw an arc from the top left corner to the top center of the bar
                path.arcTo(RectF(left, top, left + 2 * mRadius, top + 2 * mRadius), 180f, 90f)

                // Draw a line from the top center to the top right corner of the bar
                path.lineTo(right - mRadius, top)

                // Draw an arc from the top right corner to the right center of the bar
                path.arcTo(RectF(right - 2 * mRadius, top, right, top + 2 * mRadius), 270f, 90f)

                // Draw a line from the right center to the bottom right corner of the bar
                path.lineTo(right, bottom)

                // Close the path to create a closed shape for the bar with round corners only at the top
                path.close()

                if (!isSingleColor) {
                    // Set the color for the currently drawn value. If the index is out of bounds, reuse colors.
                    mRenderPaint.color = dataSet.getColor(j / 4)
                }

                if (dataSet.gradientColor != null) {
                    val gradientColor: GradientColor? = dataSet.gradientColor
                    mRenderPaint.shader = LinearGradient(
                        buffer.buffer[j],
                        buffer.buffer[j + 3],
                        buffer.buffer[j],
                        buffer.buffer[j + 1],
                        gradientColor!!.startColor,
                        gradientColor.endColor,
                        Shader.TileMode.MIRROR
                    )
                }
                if (dataSet.gradientColors != null) {
                    mRenderPaint.shader = LinearGradient(
                        buffer.buffer[j],
                        buffer.buffer[j + 3],
                        buffer.buffer[j],
                        buffer.buffer[j + 1],
                        dataSet.getGradientColor(j / 4).startColor,
                        dataSet.getGradientColor(j / 4).endColor,
                        Shader.TileMode.MIRROR
                    )
                }

                // Draw the path using the render paint
                c.drawPath(path, mRenderPaint)

                if (drawBorder) {
                    c.drawPath(path, mBarBorderPaint)
                }
                j += 4
            }
        }
    }
}