package com.goodthings.app.activity.crowdorderlist

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
interface CrowdOrderListContract {

    interface CrowdOrderListView : BaseView{
        fun showSelectStatusOrderList(orderList: List<CrowdOrderBean>?)
        fun noMoreLoad()
        fun upateStatus(position: Int,newStatus:Int)
    }

    interface CrowdOrderListPresenter : BasePresenter<CrowdOrderListView>{
        fun requestAllOrder(isMore:Boolean,status:Int)
        fun cancelOrder(order_no: CrowdOrderBean, position: Int)
        fun sureTake(bean: CrowdOrderBean, position: Int)

    }
}