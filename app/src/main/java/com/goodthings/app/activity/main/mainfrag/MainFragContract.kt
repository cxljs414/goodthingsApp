package com.goodthings.app.activity.main.mainfrag

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.base.MainFragCateBean
import com.goodthings.app.bean.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/21
 * 修改内容：
 * 最后修改时间：
 */
interface MainFragContract {

    interface MainFragView : BaseView{
        fun showBanner(place1: List<HomeRecomSubBean>)
        fun columnList(place2: List<HomeRecomSubBean>)
        fun showCateList(cateList:List<MainFragCateBean>)
        fun updateProdList(pageList: MutableList<MainFragProdBean>?, tabIndex:Int,recomBean: HomeRecomBean?)
        fun noMoreData()
        //新闻推荐
        fun showRecommedNews(place5: List<HomeRecomSubBean>?)
        //首页预购推荐
        fun showRecommedCrowd(place6: List<HomeRecomSubBean>?)
        //推荐领袖IP
        fun showLeaderIP(place7: List<HomeRecomSubBean>?)
        //推荐行业IP
        fun showProfessionIP(place8: List<HomeRecomSubBean>?)

        fun notifyGroupBuyListUpdate(pageList: List<GroupListBean>)
        fun notifyColCount(it: MainCountBean?)
        fun notifyCrowCount(it: MainCountBean?)
        fun showRecommedGroups(groups: List<HomeRecomSubBean>?)
    }

    interface MainFragPresenter : BasePresenter<MainFragView>{
        fun start()
        fun reselectedTab(selectedTabPosition: Int)
        fun loadMoreData(position: Int)
        fun tyDailyRedEnvelopes()

    }

    interface OnProdClickListener{
        fun onProkClick(isAdver:Boolean,prodBean:MainFragProdBean)
    }

    interface OnTopicPersonClickListener{
        fun onTopicPersonClick(isTopic:Boolean,recBean:HomeRecomSubBean)
    }

    interface OnCollectPersonClickListener{
        fun onCollectPersonClick(isFocus:Boolean,position:Int,recBean:HomeRecomSubBean)
    }

    interface OnProdShareListener{
        fun onProdShare(item: MainFragProdBean?)
    }

    interface OnTopicClickListener{
        fun onTopicClick(url:String)
    }

}