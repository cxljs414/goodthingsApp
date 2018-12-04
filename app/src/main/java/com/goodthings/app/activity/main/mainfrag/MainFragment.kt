package com.goodthings.app.activity.main.mainfrag

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.goodthings.app.R
import com.goodthings.app.activity.groupbuy.GroupBuyListActivity
import com.goodthings.app.activity.login.LoginActivity
import com.goodthings.app.activity.main.MainActivity
import com.goodthings.app.activity.crowddetail.ProdCrowdDetailActivity
import com.goodthings.app.activity.crowdlist.ProductCrowdActivity
import com.goodthings.app.activity.groupbuydetail.GroupBuyDetailActivity
import com.goodthings.app.activity.web.WebActivity
import com.goodthings.app.adapter.*
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseFragment
import com.goodthings.app.base.MainFragCateBean
import com.goodthings.app.bean.*
import com.goodthings.app.util.*
import com.goodthings.app.view.SwipeCardsView
import kotlinx.android.synthetic.main.fragment_mainfrag1.*
import kotlinx.android.synthetic.main.include_mainfragment_top.*
import kotlinx.android.synthetic.main.popupwindow_share.view.*


/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/21
 * 修改内容：
 * 最后修改时间：
 */
class MainFragment :
        BaseFragment<MainFragContract.MainFragView,MainFragContract.MainFragPresenter>(),
        MainFragContract.MainFragView, BaseQuickAdapter.RequestLoadMoreListener {

    override var presenter: MainFragContract.MainFragPresenter = MainFragPresenterImpl()
    private var mainFragColumnListAdapter: MainFragColumnListAdapter? = null
    private var mainFragProdListAdapter: MainFragProdListAdapter? = null
    private var proIPAdapter:MainProIPAdapter? = null
    private var mainActivity: MainActivity? = null
    private var isReload = false
    private var mList: MutableList<HomeRecomSubBean>? = java.util.ArrayList()
    private var adapter: MeiziAdapter? = null
    private var groupAdapter: GroupBuyListAdapter? = null
    private var curIndex: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mainfrag1,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity?
        initBanner()
        initcolumnList()
        initProdList()
        initProfessIPs()
        initGroupList()
        initswipeCardsView()
        presenter.start()
    }

    private fun initBanner() {
        var imageWidth=  ScreenUtil.getScreenWidth(activity!!)
        var imageheight= imageWidth*10/16
        main_banner?.layoutParams = LinearLayout.LayoutParams(imageWidth,imageheight)

    }

    private fun initGroupList() {
        main_group_layout.setOnClickListener {
            mainActivity?.startActivity(Intent(context, GroupBuyListActivity::class.java))
        }
        main_group_recyclerview.layoutManager = NoScrollLinearLayoutManager(context!!)
        main_group_recyclerview.addItemDecoration(SpacesItemDecorationtwo(0,ScreenUtil.dip2px(context!!,10f)))
        var imageWidth= ScreenUtil.getScreenWidth(activity!!) - ScreenUtil.dip2px(activity!!,30f)
        groupAdapter = GroupBuyListAdapter(ArrayList(),imageWidth,true)
        main_group_recyclerview.adapter = groupAdapter
        groupAdapter?.setOnItemChildClickListener { adapter, view, position ->
            var bean= adapter.getItem(position) as GroupListBean
            if(view.id == R.id.item_groupbuy_root){
                var intent = Intent(context, GroupBuyDetailActivity::class.java)
                intent.putExtra("groupBuyId",bean.cId)
                startActivity(intent)
            }
        }
    }

    private fun initswipeCardsView() {
        main_crowd_layout.setOnClickListener {
            startActivity(Intent(context, ProductCrowdActivity::class.java))
        }
        swipeCardsView.retainLastCard(true)
        swipeCardsView.enableSwipe(true)
    }

    private fun initcolumnList() {
        mainfrag_recommend_classes.layoutManager = NoScrollGridLayoutManager(context!!,4)
        mainfrag_recommend_classes.addItemDecoration(GridSpacingItemDecoration(4,ScreenUtil.dip2px(context!!,15f),false))
        mainFragColumnListAdapter = MainFragColumnListAdapter(ArrayList())
        mainfrag_recommend_classes.adapter = mainFragColumnListAdapter
    }

    private fun initProdList(){
        mainfrag_recommend_prodlist.layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.VERTICAL,false)
        mainfrag_recommend_prodlist.addItemDecoration(SpacesItemDecorationtwo(0,ScreenUtil.dip2px(context!!,10f)))
        mainFragProdListAdapter = MainFragProdListAdapter(ArrayList())
        mainfrag_recommend_prodlist.adapter = mainFragProdListAdapter
        mainFragProdListAdapter?.setProdClickListener(object:MainFragContract.OnProdClickListener{
            override fun onProkClick(isAdver: Boolean, prodBean: MainFragProdBean) {
                if(isAdver){
                    mainActivity?.goNext("adver", "${prodBean.linkUrl}")
                }else{
                    mainActivity?.goNext("prod", "${prodBean.id}")
                }
            }
        })
        mainFragProdListAdapter?.topicPersonClickListener = object:MainFragContract.OnTopicPersonClickListener{
            override fun onTopicPersonClick(isTopic:Boolean,recomSubBean: HomeRecomSubBean) {
                if (isTopic) {
                    mainActivity?.goNext("topic", "${recomSubBean.linkUrl}")
                }else{
                    mainActivity?.goNext("theyHereGroom", "${recomSubBean.linkUrl}")
                }
            }
        }
        mainFragProdListAdapter?.collectPersonListener = object :MainFragContract.OnCollectPersonClickListener{
            override fun onCollectPersonClick(isFocus: Boolean, position: Int, recBean: HomeRecomSubBean) {
                if(Consts.isLogined) {
                    //presenter.changeFocusState(isFocus, position, recBean.userId)
                    if(isFocus){
                        mainActivity?.goNext("delonefollow", "${recBean.userId}")
                    }else{
                        mainActivity?.goNext("addfollow", "${recBean.userId}")
                    }
                }else{
                    mainActivity?.goLogin()
                }
            }
        }
        mainFragProdListAdapter?.onShareListener = object :MainFragContract.OnProdShareListener{
            override fun onProdShare(item: MainFragProdBean?) {
                if(item?.prdTitle != null && item.prdName!= null) {
                    mainActivity?.share(item.prdName, item.prdTitle, "${Consts.URL}/wechat/toPrdDetail?id=${item.id}",item.coverUrl,"${item.id}")
                }
            }

        }
        mainFragProdListAdapter?.onTopicClickListener = object :MainFragContract.OnTopicClickListener{
            override fun onTopicClick(url: String) {
                mainActivity?.goNext("topic", url)
            }

        }
        mainFragProdListAdapter?.setOnLoadMoreListener(this)
    }

    private fun initProfessIPs() {
        main_leaderip_layout.setOnClickListener {
            mainActivity?.goNext("leaderIp","")
        }

        main_businessip_layout.setOnClickListener {
            mainActivity?.goNext("leaderIp","")
        }
        main_profess_recyclerview.layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.HORIZONTAL,false)
        main_profess_recyclerview.addItemDecoration(SpacesItemDecorationtwo(ScreenUtil.dip2px(context!!,10f),ScreenUtil.dip2px(context!!,10f)))
        proIPAdapter = MainProIPAdapter(ArrayList())
        main_profess_recyclerview.adapter = proIPAdapter
        proIPAdapter?.setOnItemChildClickListener { adapter, view, position ->
            var item:HomeRecomSubBean= adapter.getItem(position) as HomeRecomSubBean
            mainActivity?.goNext("leaderIPList",item.linkUrl)
        }
    }

    //---------------------------重载方法-------------------------------//

    override fun showBanner(banners: List<HomeRecomSubBean>) {
        var bannerList:MutableList<String> = ArrayList()
        banners.forEach {
            bannerList.add(Consts.IMAGEURL+it.coverPic)
        }
        main_banner.setImages(bannerList)
        main_banner.setImageLoader(GlideImageLoader())
        main_banner.setOnBannerListener {
            mainActivity?.goNext("banner", "${banners[it].linkUrl}")
        }
        main_banner.start()
    }

    override fun columnList(place2: List<HomeRecomSubBean>) {
        mainFragColumnListAdapter?.setNewData(place2)
        mainFragColumnListAdapter?.notifyDataSetChanged()
        mainFragColumnListAdapter?.setOnItemChildClickListener {
            adapter, view, position ->
            if(place2[position].linkUrl.contains("redPacket")){//赚红包
                if(!Consts.isLogined){
                    var intent = Intent(context, LoginActivity::class.java)
                    intent.putExtra("isFromHongBao",true)
                    mainActivity?.startActivityForResult(intent, Consts.REQUEST_LOGIN)
                }else{
                    presenter.tyDailyRedEnvelopes()
                }
            } else if(place2[position].linkUrl.contains("crowdinfo/unauth/crowdIndex")){
                mainActivity?.startActivity(Intent(context, ProductCrowdActivity::class.java))
            }else if(place2[position].linkUrl.contains("collage/unauth/collageIndex")){
                mainActivity?.startActivity(Intent(context, GroupBuyListActivity::class.java))
            }else{
                if (view.id == R.id.item_mainfrag_column_root) {
                    mainActivity?.goNext("column", "${place2[position].linkUrl}")
                }
            }

        }
    }

    override fun showRecommedNews(place5: List<HomeRecomSubBean>?) {
        if(place5 != null && place5.isNotEmpty()){
            mainfrag_news_layout.visibility = View.VISIBLE
            var bean = place5[0]
            Glide.with(this).load(Consts.IMAGEURL+bean.coverPic).into(mainfrag_news_image)
            mainfrag_news_content.text = bean.title
            mainfrag_news_website.text = bean.content
            mainfrag_news_layout.setOnClickListener {
                var intent = Intent(context,WebActivity::class.java)
                intent.putExtra("url",bean.linkUrl)
                startActivity(intent)
            }
            mainfrag_news_more.setOnClickListener {
                var intent = Intent(context,WebActivity::class.java)
                intent.putExtra("url","http://m.hao123.com/news")
                startActivity(intent)
            }
        }else{
            mainfrag_news_layout.visibility = View.GONE
        }

    }

    override fun notifyColCount(it: MainCountBean?) {
        main_group_top_title.text = "${it?.sum}"
        main_group_top_personcount.text = "正在购买人数 ${it?.count}人"
        main_group_top_successcount.text = "已成团 ${it?.max}件"
    }

    override fun showRecommedGroups(groups: List<HomeRecomSubBean>?) {
        if(groups != null && groups.isNotEmpty()){
            main_group_root_layout.visibility = View.VISIBLE
            var groupList: MutableList<GroupListBean> = ArrayList()
            groups.forEach {
                groupList.add(GroupListBean(it.title,it.coverPic,it.relateId,it.colSaleNum,it.colPrice,it.colOldPrice))
            }
            groupAdapter?.setNewData(groupList)
            groupAdapter?.notifyDataSetChanged()
        }else{
            main_group_root_layout.visibility = View.GONE
        }
    }

    override fun notifyGroupBuyListUpdate(pageList: List<GroupListBean>) {
        groupAdapter?.setNewData(pageList)
        groupAdapter?.notifyDataSetChanged()
    }

    override fun notifyCrowCount(it: MainCountBean?) {
        main_crowd_top_title.text = "${it?.sum}"
        main_crowd_top_personcount.text = "参与预购 ${it?.count}人"
        main_crowd_top_successcount.text = "预购成功产品 ${it?.max}件"
    }
    override fun showRecommedCrowd(place6: List<HomeRecomSubBean>?) {
        if(place6 != null && place6.isNotEmpty()){
            main_crowd_root_layout.visibility = View.VISIBLE
            mList?.addAll(place6)
            adapter = MeiziAdapter(mList, context)
            swipeCardsView.setAdapter(adapter)


            //设置滑动监听
            swipeCardsView.setCardsSlideListener(object : SwipeCardsView.CardsSlideListener {
                override fun onShow(index: Int) {
                }

                override fun onCardVanish(index: Int, type: SwipeCardsView.SlideType) {
                    curIndex+=1
                    if(index == (mList?.size?.minus(2))){
                        mList?.addAll(place6)
                        adapter?.setData(mList)
                        swipeCardsView.notifyDatasetChanged(curIndex-1)
                    }
                }

                override fun onItemClick(cardImageView: View, index: Int) {
                    Consts.prodCrowdId = mList!![curIndex].relateId
                    var intent = Intent(context, ProdCrowdDetailActivity::class.java)
                    startActivity(intent)
                }
            })
        }else{
            main_crowd_root_layout.visibility = View.GONE
        }
    }

    override fun showProfessionIP(place8: List<HomeRecomSubBean>?) {
        if(place8== null || place8.isEmpty()){
            main_businessip_layout.visibility = View.GONE
        }else{
            main_businessip_layout.visibility = View.VISIBLE
            proIPAdapter?.setNewData(place8)
            proIPAdapter?.notifyDataSetChanged()
        }

    }

    override fun showLeaderIP(place7: List<HomeRecomSubBean>?) {
        if(place7!= null && place7.isNotEmpty()){
            main_leaderip_root_layout.visibility = View.VISIBLE
            Glide.with(this).load(Consts.IMAGEURL+place7[0].coverPic).into(mainfrag_leadip_1)
            mainfrag_leadip_1.setOnClickListener {
                mainActivity?.goNext("leaderIPList",place7[0].linkUrl)
            }

            if(place7.size >= 2){
                Glide.with(this).load(Consts.IMAGEURL+place7[1].coverPic).into(mainfrag_leadip_2)
                mainfrag_leadip_2.setOnClickListener {
                    mainActivity?.goNext("leaderIPList",place7[1].linkUrl)
                }
            }else{
                mainfrag_leadip_2.visibility = View.GONE
            }
            if(place7.size >= 3){
                Glide.with(this).load(Consts.IMAGEURL+place7[2].coverPic).into(mainfrag_leadip_3)
                mainfrag_leadip_3.setOnClickListener {
                    mainActivity?.goNext("leaderIPList",place7[2].linkUrl)
                }
            }else{
                mainfrag_leadip_3.visibility = View.GONE
            }
        }else{
            main_leaderip_root_layout.visibility = View.GONE
        }
    }

    override fun showCateList(cateList: List<MainFragCateBean>) {
        mainfrag_tablayout.removeAllTabs()
        cateList.forEach {
            var tab = mainfrag_tablayout.newTab()
            tab.text = it.cateName
            mainfrag_tablayout.addTab(tab)
        }
        mainfrag_tablayout.isSmoothScrollingEnabled = true
        mainfrag_tablayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(!isReload) {
                    presenter.reselectedTab(mainfrag_tablayout.selectedTabPosition)
                    mainFragProdListAdapter?.loadMoreEnd(false)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

    }

    override fun updateProdList(pageList: MutableList<MainFragProdBean>?,
                                tabIndex:Int,
                                recomBean: HomeRecomBean?) {
        isReload = false
        mainFragProdListAdapter?.setNewData(pageList)
        mainFragProdListAdapter?.notifyDataSetChanged()
        mainFragProdListAdapter?.loadMoreComplete()
    }

    override fun onLoadMoreRequested() {
        mainfrag_recommend_prodlist.post {
            presenter.loadMoreData(mainfrag_tablayout.selectedTabPosition)
            mainFragProdListAdapter?.loadMoreComplete()
        }
    }

    override fun noMoreData() {
        mainFragProdListAdapter?.loadMoreComplete()
        mainFragProdListAdapter?.loadMoreEnd(true)
    }

    fun reloadData() {
        isReload = true
        mainfrag_tablayout.setScrollPosition(0,0f,false)
        presenter.start()
    }

}