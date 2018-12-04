package com.goodthings.app.activity.crowdorderdetail

import android.content.Intent
import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.CrowdOrderBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/8
 * 修改内容：
 * 最后修改时间：
 */
interface CrowdOrderDetailContract {

    interface CrowdOrderDetailView : BaseView{
        fun getIntent(): Intent
        fun showCrowdOrderDetail(it: CrowdOrderBean?)

    }

    interface CrowdOrderDetailPresenter : BasePresenter<CrowdOrderDetailView>{
        fun startLoadData()
        fun cancelOrder(orderBean: CrowdOrderBean)
        fun successPay()
        fun sureTake()

    }
}