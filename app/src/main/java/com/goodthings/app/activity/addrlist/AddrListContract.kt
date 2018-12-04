package com.goodthings.app.activity.addrlist

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.AddressBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/3
 * 修改内容：
 * 最后修改时间：
 */
interface AddrListContract {

    interface CityListView : BaseView{
        /**
         * 显示地址列表
         */
        fun showAddrList(it: List<AddressBean>?)

    }

    interface CityListPresenter : BasePresenter<CityListView>{
        /**
         * 请求地址列表
         */
        fun loadData()

    }


}