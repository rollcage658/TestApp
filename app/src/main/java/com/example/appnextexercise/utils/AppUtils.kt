package com.example.appnextexercise.utils

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat
import com.example.appnextexercise.R

// a Utils class for the app
class AppUtils {

    companion object {
        fun getRobotoFont(context: Context?): Typeface? {
            return ResourcesCompat.getFont(context!!, R.font.roboto_medium)
        }

        fun getRobotoLightFont(context: Context?): Typeface? {
            return ResourcesCompat.getFont(context!!, R.font.roboto_light)
        }

        fun getRobotoRegularFont(context: Context?): Typeface? {
            return ResourcesCompat.getFont(context!!, R.font.roboto_regular)
        }

        fun getRobotoBoldFont(context: Context?): Typeface? {
            return ResourcesCompat.getFont(context!!, R.font.roboto_bold)
        }
    }


}