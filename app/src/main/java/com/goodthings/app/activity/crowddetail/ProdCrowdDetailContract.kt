package com.goodthings.app.activity.crowddetail

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.ProdCrowdBean
import com.goodthings.app.bean.ScrowdUserBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/25
 * 修改内容：
 * 最后修改时间：
 */
interface ProdCrowdDetailContract {

    interface ProdCrowdDetailView : BaseView{
        fun showCrowdContent(prod:ProdCrowdBean)
        fun showCrowdUserInfo(it: ScrowdUserBean?)
        fun shareCountChange()
        fun showCollectCount(it: Int?)
        fun showShareCount(it: Int?)
        fun changeCollect(b: Boolean, needAdd: Boolean)
        fun changeUserFollow(b: Boolean)
        fun goLogin()
        fun notifyRecomListUpdate(list: MutableList<ProdCrowdBean>)
        /**
         * 是否还能在买
         */
        fun showCanBuy(prodId: Int,canMoreBuy:Int, buycount: Int)

        /**
         * 显示阅读奖励金币
         */
        fun showReadGold(it: Int?)
    }

    interface ProdCrowdDetailPresenter : BasePresenter<ProdCrowdDetailView>{
        fun startLoadData(prodId: Int)
        fun addShare(id: Int)
        fun requestChangeCollect(collect: Boolean)
        fun requestChangeUserFollow(userFollowBean: Boolean)
        fun payQueryByid()
        fun getIsCollect(needAdd:Boolean)
        /**
         * 阅读10秒并且滑动到底部时有金币奖励
         */
        fun cfDailyGoldEnvelopes()

        /**
         * 获取预购发起人信息
         */
        fun querySkillUser()
    }
    
}