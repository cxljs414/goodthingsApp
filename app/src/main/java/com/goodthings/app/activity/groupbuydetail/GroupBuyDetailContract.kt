package com.goodthings.app.activity.groupbuydetail

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/29
 * 修改内容：
 * 最后修改时间：
 */
interface GroupBuyDetailContract {

    interface GroupBuyDetailView : BaseView{
        fun showGroupDetailContent(bean: GroupBuyDetailBean)
        fun shareCountChange()
        fun showCollectCount(it: Int?)
        fun showShareCount(it: Int?)
        fun changeCollect(b: Boolean, needAdd: Boolean)
        fun goLogin()
        fun notifyGroupBuyListUpdate(list: MutableList<GroupListBean>)
        fun notifyTeamsListUpdate(it: List<GroupingBean>?)
        fun updateIsBuy(it: Boolean?)
        fun showProdBuyDetail(it: List<ProdBuyDetailBean>?)
        fun notifyShare(it: ShareParamBean?)
        fun showComment(pageList: List<CommentBean>?)
    }

    interface GroupBuyDetailPresenter : BasePresenter<GroupBuyDetailView>{
        fun start(groupBuyId: Int)
        fun addShare(id: Int)
        fun requestChangeCollect(collect: Boolean)
        fun getIsCollect(needAdd:Boolean)
        fun getIsBuyed()
        fun colTeamQuery()
        fun tjCollageQuery()
        fun collageQuery()
        fun shareParam(cId: Int?)
        fun getPrdDetail(spu_id: Int?)
        fun addFabulous(mId: Int)
    }
}