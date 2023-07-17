package com.example.appnextexercise.utils.animation

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View

class AnimationsUtils {

    fun rotateAnimation(view: View) {
        Build()
    }

    fun rotateAnimationWithVibrate(context: Context, view: View) {
        rotateAnimation(view)
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val p = longArrayOf(50, 100, 50, 100)
            v.vibrate(VibrationEffect.createWaveform(p, VibrationEffect.DEFAULT_AMPLITUDE))
        }
        v.vibrate(200)
    }

    fun animateChangeColorBackground(view: View, colorFrom: Int, colorTo: Int, duration: Int) {
        var colorFrom = colorFrom
        val background = view.background
        if (background is ColorDrawable) {
            colorFrom = background.color
        }
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = duration.toLong() // milliseconds
        colorAnimation.addUpdateListener { animator -> view.setBackgroundColor(animator.animatedValue as Int) }
        colorAnimation.start()
    }
}