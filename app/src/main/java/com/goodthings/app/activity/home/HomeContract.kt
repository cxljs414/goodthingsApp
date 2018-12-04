package com.goodthings.app.activity.home

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/2
 * 修改内容：
 * 最后修改时间：
 */
interface HomeContract {

    interface HomeView : BaseView{
        //显示banner
        fun showBanner(bannerList: List<HomeRecomSubBean>)
        //分类列表更新
        fun notifyClassRvUpdate(classList: MutableList<HomeClassBean>)
        //商城列表更新
        fun notifyBusinessRvUpdate(busList: List<HomeRecomSubBean>)
        //直播
        fun notifyLiveRvUpdate(liveList: MutableList<String>)
        //私藏珍品
        fun notifyFavoriteRvUpdate(favoriteList: List<HomeRecomSubBean>)
        //随时用
        fun notifyUseRvUpdate(useList: List<HomeRecomSubBean>)
        //猜你喜欢
        fun notifyLikeRvUpdate(likeList: List<HomeGussLikeBean>)
        //好事头条
        fun notifyNewsUpdate(data: List<HomeNewsBean>)
        //推荐单品
        fun notifyRecomedUpdate(homeRecomSubBean: HomeRecomSubBean)

    }

    interface HomePresenter : BasePresenter<HomeView>{
        //初始化数据
        fun initData()

    }
}