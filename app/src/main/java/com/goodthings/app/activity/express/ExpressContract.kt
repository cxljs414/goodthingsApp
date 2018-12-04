package com.goodthings.app.activity.express

import android.content.Intent
import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.ExpressDetailBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/18
 * 修改内容：
 * 最后修改时间：
 */
interface ExpressContract {

    interface ExpressView : BaseView {
        fun getIntent(): Intent
        fun showExpressDetail(it: ExpressDetailBean?)

    }

    interface ExpressPresenter : BasePresenter<ExpressView>{
        fun start()

    }
}