package com.goodthings.app.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

/**
 * @function 屏幕工具
 */
object ScreenUtil {

    /**
     * 获取屏幕内容高度
     * @param activity
     * @return
     */
    fun getScreenHeight(activity: Activity): Int {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        var result = 0
        val resourceId = activity.resources
                .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = activity.resources.getDimensionPixelSize(resourceId)
        }
        return dm.heightPixels - result
    }

    fun getScreenWidth(activity: Activity): Int {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    /**
     * dp转px
     * @param context
     * @param dipValue
     * @return
     */
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}
