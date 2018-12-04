package com.goodthings.app.view

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupWindow
import com.goodthings.app.R

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/23
 * 修改内容：
 * 最后修改时间：
 */
class PhotoSelectPopupWindows(context: Activity?,itemOnClickListener: View.OnClickListener) : PopupWindow(context) {

    init {
        val view = View.inflate(context, R.layout.photoselectpopw,null)
        val mtakephoto = view.findViewById<Button>(R.id.take_photo)
        val mpickphoto = view.findViewById<Button>(R.id.pick_photo)
        val cancelphoto = view.findViewById<Button>(R.id.cancel_photo)
        cancelphoto.setOnClickListener(itemOnClickListener)
        mtakephoto.setOnClickListener(itemOnClickListener)
        mpickphoto.setOnClickListener(itemOnClickListener)

        contentView =view
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isFocusable = true
        animationStyle = R.style.Animation
        isOutsideTouchable = true
        setBackgroundDrawable(context?.resources?.getDrawable(R.color.color_transparent))
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    fun backgroundAlpha(context: Activity, bgAlpha: Float) {
        val lp = context.window.attributes
        lp.alpha = bgAlpha
        context.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        context.window.attributes = lp
    }
}