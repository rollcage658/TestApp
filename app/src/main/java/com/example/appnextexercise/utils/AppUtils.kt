package com.example.appnextexercise.utils

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat
import com.example.appnextexercise.R

class AppUtils {

    companion object {

        fun dpToPx(context: Context, dp: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            ).toInt()
        }

        fun getRobotoFont(context: Context?): Typeface? {
            return ResourcesCompat.getFont(context!!, R.font.roboto_medium)
        }

        fun getRobotoLightFont(context: Context?): Typeface? {
            return ResourcesCompat.getFont(context!!, R.font.roboto_light)
        }
    }


}