package com.goodthings.app.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ViewFlipper
import com.bumptech.glide.Glide
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.bean.GroupingBean
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Administrator on 2017/4/7.
 */

class AutoVerticalViewForGroup : ViewFlipper {
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
    fun setViews(datas: List<GroupingBean>?) {
        if (datas == null || datas.isEmpty()) return
        removeAllViews()
        val size = datas.size
        var i = 0
        while (i < size) {
            val position = i
            var bean1= datas[position]
            //根布局
            val item = LayoutInflater.from(mContext).inflate(R.layout.item_autoview_group, null) as RelativeLayout
            //设置监听
            var ctbt = item.findViewById<TextView>(R.id.item_autoview_group_ctbt)
            ctbt .setOnClickListener({
                if (onItemClickListener != null) {
                    onItemClickListener!!.onItemClick(position)
                }
            })
            var headpic= item.findViewById<CircleImageView>(R.id.item_autoview_group_headpic)
            var count = item.findViewById<TextView>(R.id.item_autoview_group_count)
            var nickname= item.findViewById<TextView>(R.id.item_autoview_group_nickname)

            Glide.with(mContext).load(Consts.IMAGEURL+bean1.head_url).into(headpic)
            count.text = "还差${bean1.person_num}人成团"
            nickname.text = bean1.nickname
            var group1= item.findViewById<RelativeLayout>(R.id.item_autoview_group1)

            //控件赋值
            /*(item.findViewById(R.id.tv1) as TextView).setText(datas[position].value)
            (item.findViewById(R.id.title_tv1) as TextView).setText(datas[position].title)*/
            //当数据是奇数时，最后那个item仅有一项
            if (position + 1 < size) {
                group1.visibility = View.VISIBLE
                var bean2= datas[position+1]
                var headpic1= item.findViewById<CircleImageView>(R.id.item_autoview_group_headpic1)
                var count1 = item.findViewById<TextView>(R.id.item_autoview_group_count1)
                var nickname1= item.findViewById<TextView>(R.id.item_autoview_group_nickname1)
                Glide.with(mContext).load(Consts.IMAGEURL+bean2.head_url).into(headpic1)
                count1.text = "还差${bean2.person_num}人成团"
                nickname1.text = bean2.nickname

                var ctbt1 = item.findViewById<TextView>(R.id.item_autoview_group_ctbt1)
                ctbt1 .setOnClickListener({
                    if (onItemClickListener != null) {
                        onItemClickListener!!.onItemClick(position+1)
                    }
                })
                /*(item.findViewById(R.id.tv2) as TextView).setText(datas[position + 1].value)
                (item.findViewById(R.id.title_tv2) as TextView).setText(datas[position + 1].title)*/
            } else{
                group1.visibility = View.GONE
            }
            addView(item)
            i += 2
        }
        if(size > 2){
            startFlipping()
        }
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
