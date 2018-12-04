package com.goodthings.app.activity.aftersaledetail.fragment.writeexpress

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.LogisBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/24
 * 修改内容：
 * 最后修改时间：
 */
interface WriteExpressContract {

    interface WriteExPressView : BaseView{
        fun showLogisList(it: List<LogisBean>)

    }

    interface WriteExpressPresenter : BasePresenter<WriteExPressView>{
        fun start()
        fun expressCommit(selectLogis: LogisBean, expressNo: String, order_no: String)

    }
}