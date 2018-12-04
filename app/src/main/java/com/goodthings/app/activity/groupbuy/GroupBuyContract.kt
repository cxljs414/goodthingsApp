package com.goodthings.app.activity.groupbuy

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.GroupListBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/29
 * 修改内容：
 * 最后修改时间：
 */
interface GroupBuyContract {

    interface GroupBuyView : BaseView{
        fun notifyGroupBuyListUpdate(pageList: List<GroupListBean>)
        fun noMoreData()

    }

    interface GroupBuyPresenter : BasePresenter<GroupBuyView>{
        /**
         * 获取拼团商品列表
         */
        fun loadData()

        fun loadMore()

    }
}