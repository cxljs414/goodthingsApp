package com.goodthings.app.activity.crowdlist

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.HomeRecomSubBean
import com.goodthings.app.bean.ProdCrowdBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/25
 * 修改内容：
 * 最后修改时间：
 */
interface ProductCrowdContract {

    interface ProductCrowdView : BaseView{
        fun notifyProdCrowdAdapterUpdate(pageList: MutableList<ProdCrowdBean>?)
        fun loadMoreEnd()
        fun showBannerList(place1: List<HomeRecomSubBean>?)

    }

    interface ProductCrowdPresenter: BasePresenter<ProductCrowdView>{
        /**
         * 获取预购列表
         */
        fun crowdQuery(isLoadMore:Boolean)

        fun start()

    }
}