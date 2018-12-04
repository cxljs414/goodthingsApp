package com.goodthings.app.activity.wallet

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.WalletBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/3
 * 修改内容：
 * 最后修改时间：
 */
interface WalletContract {

    interface WalletView : BaseView{
        fun updateData(it: WalletBean?)

    }

    interface WalletPresenter : BasePresenter<WalletView>{
        fun start()

    }
}