package com.goodthings.app.activity.orderpay

import android.content.Intent
import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.AddressBean
import com.goodthings.app.bean.CrowdOrderBean
import com.goodthings.app.bean.ProdCrowdBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/2
 * 修改内容：
 * 最后修改时间：
 */
interface OrderPayContract {

    interface OrderPayView : BaseView{
        fun showDefaultAddress(it: AddressBean?)
        fun setAddOrder(addOrder: Boolean)
        fun showRedDialog(it: String)
        fun showRedSubMoney(it: String, isFirst: Boolean)
        fun showCrowdContent(it: ProdCrowdBean?,count:Int)
        fun getIntent(): Intent
        fun showCrowdOrderContent(it: CrowdOrderBean?)

    }

    interface OrderPayPresenter : BasePresenter<OrderPayView>{
        fun queryDefaultAddr()
        fun addOrder(prodId: Int, price: Double, count: Int, prodEid: Int, adrId: Int)

        fun shareCrowdRedEnvelopes()
        fun shareCrowdEnvelopes()
        fun queryCrowdProdById(prodId: Int)
        fun startLoadData()
        fun goOrderDetail()
    }
}