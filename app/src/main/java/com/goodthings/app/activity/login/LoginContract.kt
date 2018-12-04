package com.goodthings.app.activity.login

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
interface LoginContract {

    interface LoginView :BaseView {
        /**
         * 显示用户信息
         */
        fun setShowUser(nickname: String?, headUrl: String?)

        fun goBack(id: Int, msg: String?)

    }

    interface LoginPresenter : BasePresenter<LoginView>{
        /**
         * 短信登录，点击下一步，判断用户是否存在，如果存在进入短信验证码页面
         * 否则进入验证码页面（第一次登录）
         */
        fun loginByPhone(phone: String)

        /**
         * 根据输入的手机号码查找用户
         */
        fun queryUserByPhone(toString: String)

        /**
         * 密码验证登录
         */
        fun loginbyPsd(phone: String, psd: String)

        /**
         * 忘记密码
         */
        fun forgetPassword(phone: String)

        fun setFromHongbao(fromHongBao: Boolean)

    }
}