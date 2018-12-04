package com.goodthings.app.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ViewFlipper
import com.goodthings.app.R
import com.goodthings.app.bean.AutoVerticalViewDataData

/**
 * Created by Administrator on 2017/4/7.
 */

class AutoVerticalViewView : ViewFlipper {
    private var mContext: Context? = null
    /**是否开启动画 */
    private val isSetAnimDuration = false
    /**时间间隔 */
    private val interval = 3000
    /**动画时间  */
    private val animDuration = 500


    private var onItemClickListener: OnItemClickListener? = null


    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        this.mContext = context
        setFlipInterval(interval)
        val animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in)
        if (isSetAnimDuration) animIn.duration = animDuration.toLong()
        inAnimation = animIn
        val animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out)
        if (isSetAnimDuration) animOut.duration = animDuration.toLong()
        outAnimation = animOut
    }

    /**
     * 设置循环滚动的View数组
     * @param
     */
    fun setViews(datas: List<AutoVerticalViewDataData>?) {
        if (datas == null || datas.size == 0) return
        removeAllViews()
        val size = datas.size
        var i = 0
        while (i < size) {
            val position = i
            //根布局
            val item = LayoutInflater.from(mContext).inflate(R.layout.item_view, null) as LinearLayout
            //设置监听
            var r1 = item.findViewById(R.id.rl) as RelativeLayout
            var rl2 = item.findViewById<RelativeLayout>(R.id.rl2)
            r1 .setOnClickListener(OnClickListener {
                if (onItemClickListener != null) {
                    onItemClickListener!!.onItemClick(position)
                }
            })

            //控件赋值
            (item.findViewById(R.id.tv1) as TextView).setText(datas[position].value)
            (item.findViewById(R.id.title_tv1) as TextView).setText(datas[position].title)
            //当数据是奇数时，最后那个item仅有一项
            if (position + 1 < size) {
                rl2.setOnClickListener({
                    if (onItemClickListener != null) {
                        onItemClickListener!!.onItemClick(position + 1)
                    }
                })
                (item.findViewById(R.id.tv2) as TextView).setText(datas[position + 1].value)
                (item.findViewById(R.id.title_tv2) as TextView).setText(datas[position + 1].title)
            } else
                rl2.visibility = View.GONE
            addView(item)
            i += 2
        }
        startFlipping()
    }

    /**
     * 设置监听接口
     *
     * @param onItemClickListener
     */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    /**
     * item_view的接口
     */
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


}
