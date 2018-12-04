package com.goodthings.app.activity.topnews

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.MainFragProdBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/11
 * 修改内容：
 * 最后修改时间：
 */
interface TopNewsContract {

    interface TopNewsView : BaseView{
        fun notifyTopNewsUpdate(it: List<MainFragProdBean>?)

    }

    interface TopNewsPresenter : BasePresenter<TopNewsView>{
        fun getTopNews()

    }
}