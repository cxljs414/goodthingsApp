package com.goodthings.app.activity.userinfo

import android.net.Uri
import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/6/7
 * 修改内容：
 * 最后修改时间：
 */
interface ChangeUserInfoContract {

    interface ChangeUserInfoVew : BaseView{
        fun updateTempHeadUrl(data: String)
        fun goBack()
        fun canEditNickname(it: Boolean?)

    }

    interface ChangeUserInfoPresenter : BasePresenter<ChangeUserInfoVew>{
        fun uploadImage(data: Uri?)
        fun updateUserInfo(tempHeadImage: String, toString: String, sexkey: Int, ageKey: Int)
        fun isUpdateUserName()

    }
}