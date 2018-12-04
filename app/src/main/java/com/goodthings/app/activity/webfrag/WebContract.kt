package com.goodthings.app.activity.webfrag

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/8
 * 修改内容：
 * 最后修改时间：
 */
interface WebContract {

    interface WebFragView : BaseView{

    }

    interface WebFragPresenter : BasePresenter<WebFragView>{

    }
}