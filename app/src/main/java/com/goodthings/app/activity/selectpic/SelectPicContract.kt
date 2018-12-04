package com.goodthings.app.activity.selectpic

import android.content.Intent
import android.net.Uri
import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/2/8
 * 修改内容：
 * 最后修改时间：
 */
interface SelectPicContract {

    interface SelectPicView : BaseView {
        /**
         * 选择图片成功
         */
        fun getPicSuccess(arrayOf: Array<Uri>)

        /**
         * 选择图片失败
         */
        fun getPicError()

        fun getIntent(): Intent

    }

    interface SelectPicPresenter : BasePresenter<SelectPicView> {
        /**
         * 选择图片
         * isTakePhoto 是否是拍照
         */
        fun takePhoto(isTakePhoto: Boolean)

        /**
         * 返回结果处理
         */
        fun handleActivityResult(requestCode: Int, data: Intent?)

    }
}