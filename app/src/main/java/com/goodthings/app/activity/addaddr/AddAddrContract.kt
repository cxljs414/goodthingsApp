package com.goodthings.app.activity.addaddr

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.AddressBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/2
 * 修改内容：
 * 最后修改时间：
 */
interface AddAddrContract {
    interface AddAddrView : BaseView{
        fun initResult()
        fun showAddrInfo(it: AddressBean?)

    }

    interface AddAddrPresenter : BasePresenter<AddAddrView>{
        fun addAddress(name: String,
                       phone: String,
                       city: String,
                       city_code: String,
                       addr: String,
                       is_default: Boolean)

        /**
         * 获取地址信息
         */
        fun getAddrInfo(intExtra: Int)

    }

}