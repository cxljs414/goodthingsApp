package com.goodthings.app.activity.shop

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.goodthings.app.R
import com.goodthings.app.activity.groupbuydetail.GroupBuyDetailActivity
import com.goodthings.app.activity.crowddetail.ProdCrowdDetailActivity
import com.goodthings.app.activity.login.LoginActivity
import com.goodthings.app.activity.web.WebActivity
import com.goodthings.app.adapter.GroupBuyListAdapter
import com.goodthings.app.adapter.ProdCrowdAdapter
import com.goodthings.app.adapter.ShopProdAdapter
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.*
import com.goodthings.app.util.ScreenUtil
import com.goodthings.app.util.SpacesItemDecorationtwo
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_group_buy_detail.*
import kotlinx.android.synthetic.main.activity_group_shop.*
import kotlinx.android.synthetic.main.activity_poi_keyword_search.*
import kotlinx.android.synthetic.main.top_bar.*
import org.jetbrains.anko.backgroundResource

class ShopActivity :
        BaseActivity<ShopContract.ShopView, ShopContract.ShopPresenter>(),
        ShopContract.ShopView {

    override var presenter: ShopContract.ShopPresenter = ShopPresenterImpl()
    private var groupAdapter:GroupBuyListAdapter? = null
    private var crowdAdapter:ProdCrowdAdapter? = null
    private var prodAdapter:ShopProdAdapter? = null
    private var isUserFollowBean:Boolean = false
    private var resumFromLogin:Boolean = false
    private var isNeedReload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_shop)
        topback.setOnClickListener { onBackPressed() }
        toptitle.text = "店铺"

        shop_recyclerview.layoutManager = LinearLayoutManager(this)
        shop_recyclerview.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this,15f)))
        initTabLayout()
        initAdapter()
        shop_recyclerview.adapter = groupAdapter
        presenter.start()
    }

    private fun initAdapter() {

        var imageWidth= ScreenUtil.getScreenWidth(this) - ScreenUtil.dip2px(this,30f)
        groupAdapter = GroupBuyListAdapter(ArrayList(),imageWidth,false)
        groupAdapter?.setOnItemChildClickListener { adapter, view, position ->
            var groupBean: GroupListBean = adapter.getItem(position) as GroupListBean
            var intent = Intent(this@ShopActivity, GroupBuyDetailActivity::class.java)
            intent.putExtra("groupBuyId",groupBean.cId)
            startActivity(intent)

        }
        groupAdapter?.setFooterView(View.inflate(this,R.layout.footer_nodata,null))

        crowdAdapter = ProdCrowdAdapter(ArrayList())
        crowdAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if(view.id == R.id.item_prodcrowd_root){
                var prodCrowdBean: ProdCrowdBean = adapter.getItem(position) as ProdCrowdBean
                Consts.prodCrowdId = prodCrowdBean.pId
                var intent = Intent(this@ShopActivity, ProdCrowdDetailActivity::class.java)
                startActivity(intent)
            }
        }
        crowdAdapter?.setFooterView(View.inflate(this,R.layout.footer_nodata,null))

        prodAdapter = ShopProdAdapter(ArrayList(),imageWidth)
        prodAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if(view.id == R.id.item_shopprod_root){
                isNeedReload = !Consts.isLogined
                var spu = adapter.getItem(position) as ShopSpuBean.Spu
                var url = Consts.URL + "wechat/toPrdDetail?id=" + spu.pid
                var intent = Intent(this@ShopActivity, WebActivity::class.java)
                intent.putExtra("url", url)
                intent.putExtra("isNeedReload", isNeedReload)
                startActivity(intent)
                /* }else{
                     resumFromLogin = true
                     goLogin()
                 }*/
            }
        }
        prodAdapter?.setFooterView(View.inflate(this,R.layout.footer_nodata,null))

    }

    private fun initTabLayout() {
        var array:MutableList<String> = ArrayList()
        array.add("拼团")
        array.add("预购")
        array.add("商品")
        array.forEach {
            var tab = shop_tablayout.newTab()
            tab.text = it
            shop_tablayout.addTab(tab)
        }
        shop_tablayout.isSmoothScrollingEnabled = true
        var position= intent.getIntExtra("type",1)
        shop_tablayout.setScrollPosition(position-1,0f,false)
        shop_tablayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                changeTab(shop_tablayout.selectedTabPosition)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                changeTab(shop_tablayout.selectedTabPosition)
            }

        })
    }

    private fun changeTab(position: Int) {
        presenter.changeTab(position+1)
    }

    override fun showUserInfo(userBean: ScrowdUserBean?) {
        Glide.with(this).load(Consts.IMAGEURL+userBean?.img).into(shop_coverpic)
        shop_nickname.text = userBean?.userName
        shop_count3.text = "粉丝数${userBean?.count}"
        changeUserFollow(userBean?.browse!!)
        shop_follow.setOnClickListener {
            presenter.changeUserFollow(isUserFollowBean)
        }

        top_right_btn.visibility = View.VISIBLE
        top_right_image.setImageResource(R.mipmap.fenxiang4)
        top_right_image.setOnClickListener {
            var url = Consts.URL + "issue/unauth/busUserissue?userId=" + intent.getIntExtra("merId",-1)
            openShareWindow(this, shop_root, url,
                    userBean.userName,userBean.userName, userBean.img, object : UMShareListener {
                override fun onCancel(p0: SHARE_MEDIA?) {

                }

                override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                }

                override fun onStart(p0: SHARE_MEDIA?) {
                }

                override fun onResult(p0: SHARE_MEDIA?) {
                }
            })
        }
    }

    override fun changeUserFollow(isFollow: Boolean) {
        isUserFollowBean = isFollow
        if(!isFollow){
            shop_follow.text = "关注"
            shop_follow.backgroundResource = R.drawable.bg_redff4949_corner_5
        }else{
            shop_follow.text = "取消关注"
            shop_follow.backgroundResource = R.drawable.bg_grey_corner_5
        }
    }

    override fun goLogin() {
        resumFromLogin = true
        startActivity(Intent(this@ShopActivity, LoginActivity::class.java))
    }

    override fun showShopGroups(it: ShopGroupBean?) {
        shop_recyclerview.adapter = groupAdapter
        var list:MutableList<GroupListBean> = ArrayList()
        it?.data!!.forEach {
            var coverUrl= it.cover_url
            if(coverUrl ==null)coverUrl = ""
            list.add(GroupListBean(it.title,coverUrl,it.id,it.virtual_num,"${it.price}","${it.old_price}"))
        }
        groupAdapter?.setNewData(list)
        groupAdapter?.notifyDataSetChanged()
        groupAdapter?.loadMoreEnd(false)
    }

    override fun showShopCrowds(it: ShopCrowdBean?) {
        shop_recyclerview.adapter = crowdAdapter
        //预购列表
        crowdAdapter?.setNewData(it?.data)
        crowdAdapter?.notifyDataSetChanged()
        crowdAdapter?.loadMoreEnd(false)
    }

    override fun showShopSpus(it: ShopSpuBean?) {
        shop_recyclerview.adapter = prodAdapter
        //产品列表
        prodAdapter?.setNewData(it?.data)
        prodAdapter?.notifyDataSetChanged()
        prodAdapter?.loadMoreEnd(false)
    }

    override fun showShopCounts(it: ShopCountBean?) {
        shop_count1.text = "商品数量${it?.data?.total_prd_num}件"
        shop_count2.text = "已售${it?.data?.total_sale_num}件"
    }

    override fun onResume() {
        super.onResume()
        isNeedReload = false
        if(resumFromLogin){
            if(Consts.isLogined){
                isNeedReload = true
            }
        }
        presenter.querySkillUser()
    }
}
