package com.goodthings.app.view

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import com.goodthings.app.R
import com.goodthings.app.R.id.home_menu_msg_biaoji
import com.goodthings.app.inerfaces.WindowDismissLister
import org.jetbrains.anko.layoutInflater

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/6
 * 修改内容：
 * 最后修改时间：
 */
class HomeMenuPopwindow(context: Context?) : PopupWindow(context) {

    private var windowDismissListener: WindowDismissLister? = null
    private var newMsgImage:ImageView
    private var newMsg: RelativeLayout
    private var scan:RelativeLayout
    private var msgListener: OnMsgClickListener?= null
    private var scanListener:OnScanClickListener? = null

    init {
        var mView = context?.layoutInflater?.inflate(R.layout.view_menupopupwindow,null)
        newMsgImage = mView!!.findViewById(R.id.home_menu_msg_biaoji)
        newMsg = mView!!.findViewById(R.id.home_menu_msg)
        newMsg.setOnClickListener {
            if(msgListener != null){
                msgListener?.onMsgClick()
            }
        }

        scan = mView!!.findViewById(R.id.home_menu_scan)
        scan.setOnClickListener {
            scanListener?.onScanClick()
        }
        //设置SelectPicPopupWindow的View
        this.contentView = mView
        //设置SelectPicPopupWindow弹出窗体的宽
        this.width = WindowManager.LayoutParams.WRAP_CONTENT
        //设置SelectPicPopupWindow弹出窗体的高
        this.height = WindowManager.LayoutParams.WRAP_CONTENT
        //设置SelectPicPopupWindow弹出窗体可点击
        this.isFocusable = true
        //设置PopupWindow可触摸
        this.isTouchable = true
        //设置非PopupWindow区域是否可触摸
        this.isOutsideTouchable = true
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.animationStyle = R.style.menupopwindow_anim_style
        //实例化一个ColorDrawable颜色为半透明
        val dw = ColorDrawable(Color.parseColor("#ff0000"))
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw)
        this.setOnDismissListener {
            if(windowDismissListener != null){
                windowDismissListener?.onWindowDismiss()
            }
        }

    }

    fun setWindowDismissListener(listener:WindowDismissLister){
        this.windowDismissListener = listener
    }

    fun setNewMsgVisible(visible: Int) {
        newMsgImage.visibility = visible
    }

    interface OnMsgClickListener{
        fun onMsgClick()
    }

    interface OnScanClickListener{
        fun onScanClick()
    }

    fun setNewMsgClickListener(onMsgClickListener: HomeMenuPopwindow.OnMsgClickListener) {
        msgListener = onMsgClickListener
        newMsg.setOnClickListener {
            if(msgListener != null){
                msgListener?.onMsgClick()
            }
        }
    }

    fun setScanClickListener(onScanClickListener: OnScanClickListener){
        scanListener = onScanClickListener
        scan.setOnClickListener {
            scanListener?.onScanClick()
        }
    }



}