package com.goodthings.app.activity.aftersaledetail.fragment.checking

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/23
 * 修改内容：
 * 最后修改时间：
 */
interface AfterSaleCheckingContract {

    interface AfterSaleCheckingView : BaseView{

    }

    interface AfterSaleCheckingPresenter : BasePresenter<AfterSaleCheckingView>{
        fun cancelApply(applyId: Int?)
        fun againApply(applyId: Int?)

    }
}