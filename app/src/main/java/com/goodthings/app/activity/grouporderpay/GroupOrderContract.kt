package com.goodthings.app.activity.grouporderpay

import android.content.Intent
import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.AddressBean
import com.goodthings.app.bean.GroupBuyDetailBean
import com.goodthings.app.bean.GroupOrderDetailBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/6/4
 * 修改内容：
 * 最后修改时间：
 */
interface GroupOrderContract {

    interface GroupOrderView : BaseView{
        fun getIntent(): Intent
        fun showDefaultAddress(it: AddressBean?)
        fun showGroupDetailContent(it: GroupBuyDetailBean?, groupCount: Int, alone: Boolean)
        fun setAddOrder(b: Boolean, orderNo: String)
        fun showGroupOrderContent(it: GroupOrderDetailBean?)

    }

    interface GroupOrderPresenter : BasePresenter<GroupOrderView>{
        fun startLoadData()
        fun queryDefaultAddr()
        fun addOrder(prodId: Int, price: Double, count: Int,adrId: Int)

    }
}