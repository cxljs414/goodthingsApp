package com.goodthings.app.activity.crowdlist

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.goodthings.app.R
import com.goodthings.app.activity.crowdorderlist.CrowdOrderListActivity
import com.goodthings.app.activity.login.LoginActivity
import com.goodthings.app.activity.crowddetail.ProdCrowdDetailActivity
import com.goodthings.app.adapter.ProdCrowdAdapter
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.HomeRecomSubBean
import com.goodthings.app.bean.ProdCrowdBean
import com.goodthings.app.util.GlideImageLoader
import com.goodthings.app.util.ScreenUtil
import com.goodthings.app.util.SpacesItemDecorationtwo
import kotlinx.android.synthetic.main.activity_crowd.*
import kotlinx.android.synthetic.main.top_bar.*

class ProductCrowdActivity :
        BaseActivity<ProductCrowdContract.ProductCrowdView,ProductCrowdContract.ProductCrowdPresenter>(),
        ProductCrowdContract.ProductCrowdView, BaseQuickAdapter.RequestLoadMoreListener {

    override var presenter: ProductCrowdContract.ProductCrowdPresenter = ProductCrowdPresenterImpl()
    private var crowdAdapter:ProdCrowdAdapter? = null
    private var nextOption:Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crowd)
        toptitle.text = "产品预购"
        topback.setOnClickListener { onBackPressed() }
        initCorwdList()
        prodcrowd_bottom_bar.setOnClickListener {
            if(Consts.isLogined) {
                startActivity(Intent(this@ProductCrowdActivity, CrowdOrderListActivity::class.java))
            }else{
                nextOption = 1
                startActivity(Intent(this@ProductCrowdActivity,LoginActivity::class.java))
            }
        }
        presenter.start()
    }

    private fun initCorwdList(){
        prodcrowd_recyclerview.layoutManager = LinearLayoutManager(this)
        prodcrowd_recyclerview.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this,15f)))
        crowdAdapter = ProdCrowdAdapter(ArrayList())
        prodcrowd_recyclerview.adapter = crowdAdapter
        crowdAdapter?.setOnLoadMoreListener(this)
        crowdAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if(view.id == R.id.item_prodcrowd_root){
                var prodCrowdBean:ProdCrowdBean = adapter.getItem(position) as ProdCrowdBean
                Consts.prodCrowdId = prodCrowdBean.pId
                var intent = Intent(this@ProductCrowdActivity,ProdCrowdDetailActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun showBannerList(place: List<HomeRecomSubBean>?) {
        if(place != null){
            var bannerList:MutableList<String> = ArrayList()
            place!!.forEach {
                bannerList.add(Consts.IMAGEURL+it.coverPic)
            }
            prodcrowd_banner.setImages(bannerList)
            prodcrowd_banner.setImageLoader(GlideImageLoader())
            prodcrowd_banner.setOnBannerListener {

            }
            prodcrowd_banner.start()
        }else{
            prodcrowd_banner.visibility= View.GONE
        }
    }

    override fun notifyProdCrowdAdapterUpdate(pageList: MutableList<ProdCrowdBean>?) {
        crowdAdapter?.setNewData(pageList)
        crowdAdapter?.notifyDataSetChanged()
        crowdAdapter?.loadMoreComplete()
    }

    override fun onLoadMoreRequested() {
        prodcrowd_recyclerview.post {
            presenter.crowdQuery(true)
            crowdAdapter?.loadMoreComplete()
        }
    }

    override fun loadMoreEnd() {
        crowdAdapter?.loadMoreComplete()
        crowdAdapter?.loadMoreEnd(true)
    }

    override fun onResume() {
        super.onResume()
        if(nextOption == 1) {
            nextOption = -1
            if (Consts.isLogined) {
                startActivity(Intent(this@ProductCrowdActivity, CrowdOrderListActivity::class.java))
            }
        }
    }

}
