package com.goodthings.app.activity.groupbuyorders

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.GroupOrderBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/29
 * 修改内容：
 * 最后修改时间：
 */
interface GroupBuyOrdersContract {

    interface GroupBuyOrdersView : BaseView{
        fun showSelectStatusOrderList(get: List<GroupOrderBean>)
        fun noMoreLoad()

        fun upateStatus(position: Int, newStatus: Int)
        fun isCompleteDialog(it: Boolean?, bean: GroupOrderBean, position: Int)
    }

    interface GroupBuyOrdersPresenter : BasePresenter<GroupBuyOrdersView>{
        fun requestAllOrder(b: Boolean, i: Int)
        fun cancelOrder(bean: GroupOrderBean, position: Int)
        fun sureTake(bean: GroupOrderBean, position: Int)
        fun isComplete(tId: GroupOrderBean, position: Int)

    }
}