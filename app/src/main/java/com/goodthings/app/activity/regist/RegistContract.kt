package com.goodthings.app.activity.regist

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
interface RegistContract {

    interface RegistView : BaseView{
        /**
         * 上传完图片后本地显示
         */
        fun showHeadPic(data: String)

    }

    interface RegistPresenter : BasePresenter<RegistView>{
        fun start()
        fun setSex(sex: String)
        fun setAge(age: String)
        fun commitRegist(nickname: String)
        fun uploadImage(data: Uri?)

    }
}