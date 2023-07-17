package com.example.appnextexercise.utils

import android.content.Context
import android.util.TypedValue

class AppUtils {

    fun dpToPx(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

}