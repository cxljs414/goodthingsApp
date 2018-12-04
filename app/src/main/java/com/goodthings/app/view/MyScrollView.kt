package com.goodthings.app.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import com.goodthings.app.inerfaces.OnScrollListener
import com.umeng.socialize.UMShareAPI.init

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/7
 * 修改内容：
 * 最后修改时间：
 */
class MyScrollView : ScrollView {

    private var scrollListener: OnScrollListener? = null

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }


    fun setScrollListener(listener: OnScrollListener){
        scrollListener = listener
    }
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if(scrollListener != null){
            scrollListener?.onScroll(t)
        }
    }
}