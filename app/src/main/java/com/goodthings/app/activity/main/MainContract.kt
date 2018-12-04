package com.goodthings.app.activity.main

import android.content.Intent
import android.net.Uri
import android.view.MotionEvent
import android.webkit.ValueCallback
import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/26
 * 修改内容：
 * 最后修改时间：
 */
interface MainContract {

    interface MainView:BaseView{
        /**
         * 获取图片成功
         */
        fun getPicSuccess(s: Array<Uri>)

        fun getPicError()

        /**
         * 原生调用js方法
         */
        fun callJs(toString: String, callback: ValueCallback<String>?)

        /**
         * 有新的消息
         */
        fun notifyHasNewMsgs(b: Boolean)

        /**
         * 显示红包
         */
        fun showHongBao()

        fun showShareGold()
        fun notifyCityChanged(city: String?, needReload: Boolean)

    }

    interface MainPresenter:BasePresenter<MainView>{

        fun handleActivityResult(requestCode: Int, data: Intent?)
        /**
         * 调用微信支付
         */
        fun callWX(content: String)

        /**
         * 初始化数据
         */
        fun start()

        fun destroy()
        /**
         * 处理微信支付结果
         */
        fun delWxPayResult()

        /**
         * 页面跳转
         */
        fun loadNewPage(s: String)

        /**
         * 处理登录返回
         * b true 登录成功返回  false 取消
         */
        fun handleLoginResult(b: Boolean, data: Intent?)

        /**
         * 请求消息数量
         */
        fun menuHasNewMsg()

        /**
         * 通过城市名请求城市code
         */
        fun requestCityCode(city: String)

        /**
         * 每次进来如果本地保存有用户信息则重新登录一下
         */
        fun retryLogin()

        /**
         * 点击打开web下一页
         *
         */
        fun goNextPage(pageName: String, params: String)

        /**
         * 处理消息
         */
        fun dealMsgCount(count: String)

        fun tyDailyRedEnvelopes(userId: Int)
        fun webShareResult(shareId: String)
        fun userShareGoldEnvelopes()
        fun shareGoldEnvelopes(shareId: String)
        fun share(shareId: String)
        fun saveUserInfo(nickName: String?, revisesSex: Int, headUrl: String?, revisesAge: Int)
    }

    interface MyOnTouchListener {
        fun onTouch(ev: MotionEvent): Boolean
    }
}