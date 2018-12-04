package com.goodthings.app.activity.splash

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.GuideBean
import com.goodthings.app.bean.UpdateAppBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/25
 * 修改内容：
 * 最后修改时间：
 */
interface SplashContract {

    interface SplashView : BaseView{
        fun updateVersion(it: UpdateAppBean?)
        fun goToMain(it: GuideBean?)
        fun shoProgress()
        fun hideProgress()
        fun updateProgress(progress: Long, total: Long)

    }

    interface SplashPresenter : BasePresenter<SplashView>{
        /**
         * 版本检查
         */
        fun checkVersion()

        /**
         * 请求广告
         */
        fun requestGuide()

        /**
         * 取消更新
         */
        fun cancelUpdate(isSuccess:Boolean,forceUpdate: Int)

        /**
         * 点击更新，下载apk
         */
        fun dealUpdate(url: String,newVersion:Int)
    }
}