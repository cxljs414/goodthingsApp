package com.goodthings.app.activity.aftersaledetail

import android.content.Intent
import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.AfterSaleBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/23
 * 修改内容：
 * 最后修改时间：
 */
interface AfterSaleDetailContract {

    interface AfterSaleDetailView :BaseView{
        fun getIntent(): Intent
        fun showContent(it: AfterSaleBean?)

    }

    interface AfterSaleDetailPresenter : BasePresenter<AfterSaleDetailView>{
        fun loadAfterSaleOrderDetail()

    }
}