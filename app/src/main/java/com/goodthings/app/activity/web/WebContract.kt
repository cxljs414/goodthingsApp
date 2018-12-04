package com.goodthings.app.activity.web

import android.webkit.ValueCallback
import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/10
 * 修改内容：
 * 最后修改时间：
 */
interface WebContract {

    interface WebView : BaseView{
        fun callJs(toString: String, callback: ValueCallback<String>?)

    }

    interface WebPresenter: BasePresenter<WebView>{
        fun callWX(json: String)
        fun delWxPayResult()
        fun webShareResult(webUrl: String)
        fun userShareGoldEnvelopes()
        fun retryLogin()
        fun autoLogin(param: String)
    }
}