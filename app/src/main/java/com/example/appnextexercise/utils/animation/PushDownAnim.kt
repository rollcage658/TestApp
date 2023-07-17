package com.example.appnextexercise.utils.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.IntDef
import java.lang.ref.WeakReference


const val DEFAULT_PUSH_SCALE = 0.90f
const val DEFAULT_PUSH_STATIC = 2f
const val DEFAULT_PUSH_DURATION: Long = 50
const val DEFAULT_RELEASE_DURATION: Long = 125
const val MODE_SCALE = 0
const val MODE_STATIC_DP = 1
val DEFAULT_INTERPOLATOR = AccelerateDecelerateInterpolator()

class PushDownAnim : PushDown {
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(MODE_SCALE, MODE_STATIC_DP)
    annotation class Mode

    private var defaultScale = 0f
    private var mode: Int = MODE_SCALE
    private var pushScale: Float = DEFAULT_PUSH_SCALE
    private var pushStatic: Float = DEFAULT_PUSH_STATIC
    private var durationPush: Long = DEFAULT_PUSH_DURATION
    private var durationRelease: Long = DEFAULT_RELEASE_DURATION
    private var interpolatorPush: AccelerateDecelerateInterpolator =
        DEFAULT_INTERPOLATOR
    private var interpolatorRelease: AccelerateDecelerateInterpolator =
        DEFAULT_INTERPOLATOR
    private var weakView: WeakReference<View>? = null
    private var scaleAnimSet: AnimatorSet? = null

    @SuppressLint("NotConstructor")
    private fun PushDownAnim(view: View) : PushDownAnim{
        weakView = WeakReference(view)
        weakView!!.get()!!.isClickable = true
        defaultScale = view.scaleX
        return this
    }


    fun setPushDownAnimTo(view: View): PushDownAnim {
        val pushAnim = PushDownAnim(view)
        pushAnim.setOnTouchEvent(null)
        return pushAnim
    }

    override fun setScale(scale: Float): PushDown {
        if (this.mode == MODE_SCALE) {
            this.pushScale = scale
        } else if (this.mode == MODE_STATIC_DP) {
            this.pushStatic = scale
        }
        return this
    }

    override fun setScale(@Mode mode: Int, scale: Float): PushDown {
        this.mode = mode
        this.setScale(scale)
        return this
    }

    override fun setDurationPush(duration: Long): PushDown {
        this.durationPush = duration
        return this
    }

    override fun setDurationRelease(duration: Long): PushDown {
        this.durationRelease = duration
        return this
    }

    override fun setInterpolatorPush(interpolatorPush: AccelerateDecelerateInterpolator?): PushDown {
        if (interpolatorPush != null) {
            this.interpolatorPush = interpolatorPush
        }
        return this
    }

    override fun setInterpolatorRelease(interpolatorRelease: AccelerateDecelerateInterpolator?): PushDown {
        if (interpolatorRelease != null) {
            this.interpolatorRelease = interpolatorRelease
        }
        return this
    }


    override fun setOnClickListener(clickListener: View.OnClickListener?): PushDown {
        if (weakView!!.get() != null) {
            weakView!!.get()!!.setOnClickListener(clickListener)
        }
        return this
    }

    override fun setOnLongClickListener(clickListener: OnLongClickListener?): PushDown {
        if (weakView!!.get() != null) {
            weakView!!.get()!!.setOnLongClickListener(clickListener)
        }
        return this
    }

    override fun setOnTouchEvent(eventListener: OnTouchListener?): PushDown {
        if (weakView!!.get() != null) {
            if (eventListener == null) {
                weakView!!.get()!!.setOnTouchListener(object : OnTouchListener {
                    var isOutSide = false
                    var rect: Rect? = null
                    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                        if (view.isClickable) {
                            val i = motionEvent.action
                            if (i == MotionEvent.ACTION_DOWN) {
                                isOutSide = false
                                rect = Rect(
                                    view.left,
                                    view.top,
                                    view.right,
                                    view.bottom
                                )
                                makeDecisionAnimScale(
                                    view,
                                    MODE_SCALE,
                                    DEFAULT_PUSH_SCALE,
                                    DEFAULT_PUSH_STATIC,
                                    DEFAULT_PUSH_DURATION,
                                    DEFAULT_INTERPOLATOR,
                                    i
                                )
                            } else if (i == MotionEvent.ACTION_MOVE) {
                                if (rect != null && !isOutSide
                                    && !rect!!.contains(
                                        view.left + motionEvent.x.toInt(),
                                        view.top + motionEvent.y.toInt()
                                    )
                                ) {
                                    isOutSide = true
                                    makeDecisionAnimScale(
                                        view,
                                        MODE_SCALE,
                                        defaultScale,
                                        0f,
                                        DEFAULT_RELEASE_DURATION,
                                        DEFAULT_INTERPOLATOR,
                                        i
                                    )
                                }
                            } else if (i == MotionEvent.ACTION_CANCEL
                                || i == MotionEvent.ACTION_UP
                            ) {
                                makeDecisionAnimScale(
                                    view,
                                    MODE_SCALE,
                                    defaultScale,
                                    0f,
                                    DEFAULT_RELEASE_DURATION,
                                    DEFAULT_INTERPOLATOR,
                                    i
                                )
                            }
                        }
                        return false
                    }
                })
            } else {
                weakView!!.get()!!.setOnTouchListener { v, motionEvent ->
                    eventListener.onTouch(
                        weakView!!.get(),
                        motionEvent
                    )
                }
            }
        }
        return this
    }

    /* =========================== Private method =============================================== */
    fun makeDecisionAnimScale(
        view: View,
        @Mode mode: Int,
        pushScale: Float,
        pushStatic: Float,
        duration: Long,
        interpolator: TimeInterpolator,
        action: Int
    ) {
        var tmpScale = pushScale
        if (mode == MODE_STATIC_DP) {
            tmpScale = getScaleFromStaticSize(pushStatic)
        }
        animScale(view, tmpScale, duration, interpolator)
    }

    fun animScale(
        view: View,
        scale: Float,
        duration: Long,
        interpolator: TimeInterpolator
    ) {
        view.animate().cancel()
        if (scaleAnimSet != null) {
            scaleAnimSet!!.cancel()
        }
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", scale)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", scale)
        scaleX.interpolator = interpolator
        scaleX.duration = duration
        scaleY.interpolator = interpolator
        scaleY.duration = duration
        scaleAnimSet = AnimatorSet()
        scaleAnimSet!!
            .play(scaleX)
            .with(scaleY)
        scaleX.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }
        })
        scaleX.addUpdateListener {
            val p = view.parent as View
            p?.invalidate()
        }
        scaleAnimSet!!.start()
    }

    fun getScaleFromStaticSize(sizeStaticDp: Float): Float {
        if (sizeStaticDp <= 0) return defaultScale
        val sizePx: Float = dpToPx(sizeStaticDp)
        return if (getViewWidth() > getViewHeight()) {
            if (sizePx > getViewWidth()) return 1.0f
            val pushWidth: Float = getViewWidth() - sizePx * 2
            pushWidth / getViewWidth()
        } else {
            if (sizePx > getViewHeight()) return 1.0f
            val pushHeight: Float = getViewHeight() - sizePx * 2
            pushHeight / getViewHeight().toFloat()
        }
    }

    fun getViewHeight(): Int {
        return weakView!!.get()!!.measuredHeight
    }

    fun getViewWidth(): Int {
        return weakView!!.get()!!.measuredWidth
    }

    fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            weakView!!.get()!!.resources.displayMetrics
        )
    }
}
