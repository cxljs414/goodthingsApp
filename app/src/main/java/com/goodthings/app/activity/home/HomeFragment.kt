package com.goodthings.app.activity.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.goodthings.app.R
import com.goodthings.app.activity.main.MainActivity
import com.goodthings.app.adapter.*
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseFragment
import com.goodthings.app.bean.*
import com.goodthings.app.inerfaces.OnScrollListener
import com.goodthings.app.util.*
import com.goodthings.app.view.AutoVerticalViewView
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/2
 * 修改内容：
 * 最后修改时间：
 */
class HomeFragment : BaseFragment<HomeContract.HomeView,HomeContract.HomePresenter>(),HomeContract.HomeView{

    override var presenter: HomeContract.HomePresenter = HomePresenterImpl()
    private var homeClassAdapter: HomeClassAdapter? = null
    private var homeBusinessAdapter: HomeBusinessAdapter? = null
    private var homeLiveAdapter: HomeLiveAdapter? = null
    private var homeFavoriteAdapter: HomeFavoriteAdapter? = null
    private var homeUseAdapter: HomeUseAdapter? = null
    private var homeLikeAdapter:HomeLikeAdapter? = null
    private var screenHeight:Int = 0
    private var mainActivity:MainActivity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity?
        if(activity != null){
            screenHeight = ScreenUtil.getScreenHeight(activity!!)
        }
        home_pulltorefreshlayout.setCanRefresh(false)
        home_pulltorefreshlayout.setCanLoadMore(false)
        home_scrollview.setScrollListener(object:OnScrollListener{
            override fun onScroll(scrollY: Int) {
                if(scrollY < screenHeight){
                    home_float_gotop.visibility = View.GONE
                }else{
                    home_float_gotop.visibility = View.VISIBLE
                }
            }
        })
        home_float_gotop.setOnClickListener {
            home_scrollview.smoothScrollTo(0,0)
        }
        home_class_more.setOnClickListener {
            mainActivity?.goNext("newsMore", "")
        }
        initClasses()
        initBusiness()
        initLive()
        initFavorite()
        initUse()
        initLike()
        presenter.initData()

    }

    private fun initClasses() {
        home_class_rv.layoutManager = NoScrollGridLayoutManager(context!!,3)
        home_class_rv.addItemDecoration(GridSpacingItemDecoration(3,2,false))
        homeClassAdapter = HomeClassAdapter(R.layout.item_home_class,ArrayList())
        home_class_rv.adapter = homeClassAdapter
    }

    private fun initBusiness(){
        home_business_rv.layoutManager = NoScrollGridLayoutManager(context!!,2)
        home_business_rv.addItemDecoration(GridSpacingItemDecoration(2,10,false))
        homeBusinessAdapter = HomeBusinessAdapter(R.layout.item_home_business,ArrayList())
        home_business_rv.adapter = homeBusinessAdapter
    }

    private fun initLive(){
        home_live_rv.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL,false)
        home_live_rv.addItemDecoration(SpacesItemDecorationtwo(ScreenUtil.dip2px(context!!,15f),0))
        homeLiveAdapter = HomeLiveAdapter(R.layout.item_home_live,ArrayList())
        home_live_rv.adapter = homeLiveAdapter
    }

    private fun initFavorite(){
        home_favorite_rv.layoutManager = NoScrollGridLayoutManager(context!!,2)
        home_favorite_rv.addItemDecoration(GridSpacingItemDecoration(2,ScreenUtil.dip2px(context!!,15f),true))
        homeFavoriteAdapter = HomeFavoriteAdapter(R.layout.item_home_favorite,ArrayList())
        home_favorite_rv.adapter = homeFavoriteAdapter
    }

    private fun initUse() {
        home_use_rv.layoutManager = NoScrollLinearLayoutManager(context!!)
        home_use_rv.addItemDecoration(SpacesItemDecorationtwo(ScreenUtil.dip2px(context!!,2f),0))
        homeUseAdapter = HomeUseAdapter(R.layout.item_home_use,ArrayList())
        home_use_rv.adapter = homeUseAdapter
    }

    private fun initLike() {
        home_like_rv.layoutManager = NoScrollGridLayoutManager(context!!,2)
        home_like_rv.addItemDecoration(GridSpacingItemDecoration(2,ScreenUtil.dip2px(context!!,15f),false))
        homeLikeAdapter = HomeLikeAdapter(R.layout.item_home_like,ArrayList())
        home_like_rv.adapter = homeLikeAdapter
    }

    //--------------------重载方法--------------------------//

    override fun showBanner(banners: List<HomeRecomSubBean>) {
        var bannerList:MutableList<String> = ArrayList()
        banners.forEach {
            bannerList.add(Consts.IMAGEURL+it.coverPic)
        }
        home_banner.setImages(bannerList)
        home_banner.setImageLoader(GlideImageLoader())
        home_banner.setOnBannerListener {
            mainActivity?.goNext("banner","${banners[it].recId}")
        }
        home_banner.start()
    }

    override fun notifyClassRvUpdate(classList: MutableList<HomeClassBean>) {
        homeClassAdapter?.setNewData(classList)
        homeClassAdapter?.notifyDataSetChanged()
        homeBusinessAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if(view.id == R.id.item_class_root) {
                mainActivity?.goNext("class", "${classList[position].title}")
            }
        }
    }

    override fun notifyBusinessRvUpdate(busList: List<HomeRecomSubBean>) {
        homeBusinessAdapter?.setNewData(busList)
        homeBusinessAdapter?.notifyDataSetChanged()
        homeBusinessAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if(view.id == R.id.item_business_coverimage){
                mainActivity?.goNext("business","${busList[position].linkUrl}")
            }
        }
    }

    override fun notifyLiveRvUpdate(liveList: MutableList<String>) {
        homeLiveAdapter?.setNewData(liveList)
        homeLiveAdapter?.notifyDataSetChanged()
        homeLiveAdapter?.setOnItemChildClickListener { adapter, view, position ->
            //mainActivity?.goNext("business","${liveList[position].recId}")
        }
    }

    override fun notifyFavoriteRvUpdate(favoriteList: List<HomeRecomSubBean>) {
        homeFavoriteAdapter?.setNewData(favoriteList)
        homeFavoriteAdapter?.notifyDataSetChanged()
        homeFavoriteAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if(view.id == R.id.home_favorite_root) {
                mainActivity?.goNext("favorite", "${favoriteList[position].linkUrl}")
            }
        }
    }

    override fun notifyUseRvUpdate(useList: List<HomeRecomSubBean>) {
        homeUseAdapter?.setNewData(useList)
        homeUseAdapter?.notifyDataSetChanged()
        homeUseAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if(view.id == R.id.item_home_use_root){
                mainActivity?.goNext("use", "${useList[position].linkUrl}")
            }
        }
    }

    override fun notifyLikeRvUpdate(likeList: List<HomeGussLikeBean>) {
        homeLikeAdapter?.setNewData(likeList)
        homeLikeAdapter?.notifyDataSetChanged()
        homeLikeAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if(view.id == R.id.home_like_root) {
                mainActivity?.goNext("like", "${likeList[position].id}")
            }
        }
    }

    override fun notifyNewsUpdate(data: List<HomeNewsBean>) {
        val newList = java.util.ArrayList<AutoVerticalViewDataData>()
        data.forEach {
            newList.add(AutoVerticalViewDataData(it.cate_name, it.title, ""))
        }
        home_news_view.setViews(newList)
        home_news_view.setOnItemClickListener(object:AutoVerticalViewView.OnItemClickListener{
            override fun onItemClick(position: Int) {
                mainActivity?.goNext("news","${data[position].id}")
            }
        })
    }

    override fun notifyRecomedUpdate(bean: HomeRecomSubBean) {
        Glide.with(this)
                .load(Consts.IMAGEURL+bean.coverPic)
                .into(home_remommend_image)
        Glide.with(this)
                .load(Consts.IMAGEURL+bean.headUrl)
                .into(home_remommend_headview)
        home_remommend_nickname.text = bean.nickName
        home_remommend_likecount.text = "${bean.collectNum}"
        home_remommend_goodname.text = bean.title
        home_remommend_image.setOnClickListener {
            mainActivity?.goNext("recommend","${bean.linkUrl}")
        }

    }

}