package com.goodthings.app.view

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.util.FileUtil
import com.goodthings.app.util.ScreenUtil
import com.goodthings.app.util.ZXingUtils
import kotlinx.android.synthetic.main.dialog_qrcode.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/12
 * 修改内容：
 * 最后修改时间：
 */

class QRcodeDialog : Dialog {

    /**
     * json内容
     * 用户头像  用户昵称   内容
     * 商品图片  商品标题  商品价格
     * url
     */
    private var json:String? = null
    constructor(context: Context) : this(context, R.style.popupDialog) {
    }
    constructor(context: Context,jsonStr: String) : this(context, R.style.popupDialog) {
        json = jsonStr
    }
    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.BOTTOM)
        window?.decorView!!.setPadding(0, 0, 0, 0)
        val lp = window?.attributes
        lp?.width = WindowManager.LayoutParams.MATCH_PARENT
        lp?.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = lp
        setContentView(R.layout.dialog_qrcode)
        dialog_qrcode_root.setOnClickListener {
            dismiss()
        }
        dialog_qrcode_conlayout.setOnClickListener {

        }
        dialog_qrcode_save.setOnClickListener {
            dialog_qrcode_pic.isDrawingCacheEnabled = true
            dialog_qrcode_pic.buildDrawingCache()
            var bitmap = Bitmap.createBitmap(dialog_qrcode_pic.drawingCache)
            dialog_qrcode_pic.isDrawingCacheEnabled = false
            FileUtil.saveBitmap(bitmap)
            if(!bitmap.isRecycled){
                bitmap.recycle()
            }
            Toast.makeText(context,"已保存至"+Consts.APPIMAGEPATH,Toast.LENGTH_SHORT).show()
            dismiss()
        }

        dialog_qrcode_ewm.setImageBitmap(ZXingUtils.createQRImage(Consts.URL,
                ScreenUtil.dip2px(context,75f),
                ScreenUtil.dip2px(context,75f)))

        dialog_qrcode_title.text = json
    }
}
