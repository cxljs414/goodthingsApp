package com.goodthings.app.activity.shop

import android.content.Intent
import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/31
 * 修改内容：
 * 最后修改时间：
 */
interface ShopContract {

    interface ShopView : BaseView{
        fun getIntent(): Intent
        fun showUserInfo(it: ScrowdUserBean?)

        fun changeUserFollow(isFollow: Boolean)
        fun goLogin()
        fun showShopGroups(it: ShopGroupBean?)
        fun showShopCrowds(it: ShopCrowdBean?)
        fun showShopSpus(it: ShopSpuBean?)
        fun showShopCounts(it: ShopCountBean?)
    }

    interface ShopPresenter : BasePresenter<ShopView>{
        fun start()
        fun changeUserFollow(userFollowBean: Boolean)
        fun querySkillUser()
        fun changeTab(i: Int)

    }
}