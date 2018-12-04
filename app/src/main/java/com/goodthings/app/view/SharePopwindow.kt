package com.goodthings.app.view

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.goodthings.app.R

class SharePopwindow(context: Activity, itemsOnClick: View.OnClickListener) : PopupWindow(context) {
    private var mView: View? = null
    private var shareLayout: LinearLayout? = null
    private var qrcode:LinearLayout? = null

    init {
        initView(context, itemsOnClick)
    }

    private fun initView(context: Activity, itemsOnClick: View.OnClickListener) {
        val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = mInflater.inflate(R.layout.popupwindow_share, null)
        shareLayout = mView!!.findViewById(R.id.sharelayout) as LinearLayout
        val weiXFriend = mView!!.findViewById(R.id.weixinghaoyou) as LinearLayout
        val friendster = mView!!.findViewById(R.id.pengyouquan) as LinearLayout
        val QQFriend = mView!!.findViewById(R.id.qqhaoyou) as LinearLayout
        val QQZone = mView!!.findViewById(R.id.qqkongjian) as LinearLayout
        val canaleTv = mView!!.findViewById(R.id.share_cancle) as TextView
        val sinaWeibo = mView!!.findViewById(R.id.weibo) as LinearLayout
        qrcode= mView!!.findViewById<LinearLayout>(R.id.qrcode)
        canaleTv.setOnClickListener {
            dismiss()
        }
        //设置按钮监听
        weiXFriend.setOnClickListener(itemsOnClick)
        friendster.setOnClickListener(itemsOnClick)
        QQFriend.setOnClickListener(itemsOnClick)
        QQZone.setOnClickListener(itemsOnClick)
        sinaWeibo.setOnClickListener(itemsOnClick)
        qrcode?.setOnClickListener(itemsOnClick)
        //设置SelectPicPopupWindow的View
        this.contentView = mView
        //设置SelectPicPopupWindow弹出窗体的宽
        this.width = WindowManager.LayoutParams.MATCH_PARENT
        //设置SelectPicPopupWindow弹出窗体的高
        this.height = WindowManager.LayoutParams.WRAP_CONTENT
        //设置SelectPicPopupWindow弹出窗体可点击
        this.isFocusable = true
        //设置PopupWindow可触摸
        this.isTouchable = true
        //设置非PopupWindow区域是否可触摸
        this.isOutsideTouchable = true
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.animationStyle = R.style.mypopwindow_anim_style
        //实例化一个ColorDrawable颜色为半透明
        val dw = ColorDrawable(Color.parseColor("#88333333"))
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw)
        backgroundAlpha(context, 0.5f)//0.0-1.0
        this.setOnDismissListener {
            backgroundAlpha(context, 1f)
        }
    }

    fun showAtLocation(context: Activity,parent: View, gravity: Int, x: Int, y: Int) {
        super.showAtLocation(parent, gravity, x, y)
        backgroundAlpha(context, 0.5f)//0.0-1.0
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

    fun setQRcodeVisible(visible: Int) {
        qrcode?.visibility = visible
    }
}
