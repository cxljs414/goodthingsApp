package com.goodthings.app.activity.verifcode

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/2/7
 * 修改内容：
 * 最后修改时间：
 */
interface VerifCodeContract {

    companion object {
        val TYPE_FIRSTREGIST    :Int = 1
        val TYPE_VERIFCODE      :Int = 2
        val TYPE_FORGETPASSWORD :Int = 3
    }

    interface VerifCodeView : BaseView{
        //初始化页面
        fun initView(phone: String?, type: Int?)

    }

    interface VerifCodePresenter : BasePresenter<VerifCodeView>{
        fun start()
        //点击提交
        fun commit(toString: String, toString1: String, type: Int?)

        //发送验证码
        fun sendVerifCode()

    }
}