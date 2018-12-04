package com.goodthings.app.activity.gold

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.GoldBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/3
 * 修改内容：
 * 最后修改时间：
 */
interface GoldContract {

    interface GoldView : BaseView{
        fun notifyGoldDataChange(beanList: List<GoldBean>)
        fun notifyRecordDataChange(pageList: List<GoldBean>)
        fun noMore(curSelectedType: Int)

    }

    interface GoldPresenter : BasePresenter<GoldView>{
        fun start()
        fun setCurSelectedType(type: Int)
        fun loadMore()

    }
}