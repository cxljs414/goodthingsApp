package com.goodthings.app.activity.groupbuyordersdetail

import android.content.Intent
import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.GroupOrderDetailBean
import com.goodthings.app.bean.ShareParamBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/29
 * 修改内容：
 * 最后修改时间：
 */
interface GroupBuyOrderDetailContract {

    interface  GroupBuyOrderDetailView : BaseView {
        fun getIntent(): Intent
        fun showGroupOrderContent(it: GroupOrderDetailBean?)
        fun isCompleteDialog(it: Boolean?)
        fun notifyShare(it: ShareParamBean?)

    }

    interface  GroupBuyOrderDetailPresenter : BasePresenter<GroupBuyOrderDetailView>{
        fun startLoadData()
        fun cancelOrder(orderBean: GroupOrderDetailBean)
        fun sureTake()
        fun successPay()
        fun isComplete(tId: Int)
        fun shareParam(pid: Int)

    }
}