package com.goodthings.app.adapter

import android.os.Build
import android.support.v4.view.ViewPager
import android.view.View

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/1/2
 * 修改内容：
 * 最后修改时间：
 */

class ScalePageTransformer : ViewPager.PageTransformer {
    /**核心就是实现transformPage(View page, float position)这个方法 */
    override fun transformPage(page: View, position: Float) {
        var position = position

        if (position < -1) {
            position = -1f
        } else if (position > 1) {
            position = 1f
        }

        val tempScale = if (position < 0) 1 + position else 1 - position

        //一个公式
        val scaleValue = MIN_SCALE + tempScale * ((MAX_SCALE - MIN_SCALE) / 1)
        val scaleXValue = MIN_SCALEX + tempScale * ((MAX_SCALE - MIN_SCALEX) / 1)
        page.scaleX = scaleXValue
        page.scaleY = scaleValue
        page.alpha = scaleValue

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            page.parent.requestLayout()
        }
    }

    companion object {

        val MAX_SCALE = 1f
        val MIN_SCALE = 0.8f
        val MIN_SCALEX = 0.9f
    }
}