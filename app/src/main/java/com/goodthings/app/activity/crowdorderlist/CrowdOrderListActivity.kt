package com.goodthings.app.activity.crowdorderlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.goodthings.app.R
import com.goodthings.app.activity.crowdorderdetail.CrowdOrderDetailActivity
import com.goodthings.app.activity.orderpay.OrderPayActivity
import com.goodthings.app.activity.crowddetail.ProdCrowdDetailActivity
import com.goodthings.app.adapter.CrowdOrderListAdapter
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.CrowdOrderBean
import com.goodthings.app.util.ScreenUtil
import com.goodthings.app.util.SpacesItemDecorationtwo
import kotlinx.android.synthetic.main.activity_crowd_order_list.*
import kotlinx.android.synthetic.main.top_bar.*

class CrowdOrderListActivity :
        BaseActivity<CrowdOrderListContract.CrowdOrderListView,CrowdOrderListContract.CrowdOrderListPresenter>(),
        CrowdOrderListContract.CrowdOrderListView, BaseQuickAdapter.RequestLoadMoreListener {

    override var presenter: CrowdOrderListContract.CrowdOrderListPresenter = CrowdOrderListPresenterImpl()
    private var crowdAdapter:CrowdOrderListAdapter? = null
    private var curStatus= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crowd_order_list)
        topback.setOnClickListener { onBackPressed() }
        toptitle.text = "预购订单"
        initTabLayout()
        initRecyclerview()
        presenter.requestAllOrder(false,0)
    }

    private fun initRecyclerview() {
        crorderlist_recyclerview.layoutManager = LinearLayoutManager(this)
        crorderlist_recyclerview.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this,15f)))
        crowdAdapter = CrowdOrderListAdapter(ArrayList())
        crorderlist_recyclerview.adapter = crowdAdapter
        crowdAdapter?.setOnItemChildClickListener { adapter, view, position ->
            var bean:CrowdOrderBean= adapter?.getItem(position) as CrowdOrderBean
            when(view.id){
                R.id.item_crowdorder_cancel -> {
                    presenter.cancelOrder(bean,position)
                }
                R.id.item_crowdorder_pay ->{
                    var intent = Intent(this@CrowdOrderListActivity, OrderPayActivity::class.java)
                    intent.putExtra("orderNo",bean.order_no)
                    intent.putExtra("position",position)
                    startActivityForResult(intent,Consts.REQUEST_ORDER_PAY)
                }
                R.id.item_crowdorder_detail,R.id.item_crowdorder_detail_gray ->{
                    var intent = Intent(this@CrowdOrderListActivity, CrowdOrderDetailActivity::class.java)
                    intent.putExtra("orderNo",bean.order_no)
                    intent.putExtra("position",position)
                    startActivityForResult(intent,Consts.REQUEST_CFORDER_DETAIL)
                }
                R.id.item_crowdorder_take ->{
                    presenter.sureTake(bean,position)
                }
                R.id.item_crowdorder_root ->{
                    Consts.prodCrowdId = bean.pId
                    var intent = Intent(this@CrowdOrderListActivity, ProdCrowdDetailActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun initTabLayout() {
        var array:MutableList<String> = ArrayList()
        array.add("全部")
        array.add("待付款")
        array.add("已支付")
        array.add("已发货")
        array.add("已收货")
        array.forEach {
            var tab = crorderlist_tablayout.newTab()
            tab.text = it
            crorderlist_tablayout.addTab(tab)
        }
        crorderlist_tablayout.isSmoothScrollingEnabled = true
        crorderlist_tablayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(crorderlist_tablayout.selectedTabPosition){
                    0->curStatus =0
                    1->curStatus = 1
                    2->curStatus = 3
                    3->curStatus = 4
                    4->curStatus = 5
                }
                presenter.requestAllOrder(false,curStatus)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    override fun showSelectStatusOrderList(orderList: List<CrowdOrderBean>?) {
        if(orderList?.size!! < 10){
            crowdAdapter?.setEnableLoadMore(false)
            crowdAdapter?.setOnLoadMoreListener(null)
        }else{
            crowdAdapter?.setEnableLoadMore(true)
            crowdAdapter?.setOnLoadMoreListener(this)
        }
        crowdAdapter?.setNewData(orderList)
        crowdAdapter?.notifyDataSetChanged()
        crowdAdapter?.loadMoreComplete()
    }

    override fun onLoadMoreRequested() {
        crorderlist_recyclerview.post {
            presenter.requestAllOrder(true,curStatus)
            crowdAdapter?.loadMoreComplete()
        }
    }

    override fun noMoreLoad() {
        crowdAdapter?.loadMoreComplete()
        crowdAdapter?.loadMoreEnd(true)
    }

    override fun upateStatus(position: Int,newStatus:Int) {
        if(crowdAdapter?.getItem(position)?.status == newStatus)return
        if(curStatus == 0){
            crowdAdapter?.getItem(position)?.status = newStatus
            crowdAdapter?.notifyDataSetChanged()
        }else{
            crowdAdapter?.remove(position)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Consts.REQUEST_ORDER_PAY){//支付成功
                var position:Int = data?.getIntExtra("position",-1)!!
                if(position != -1){
                    if(curStatus == 0){
                        crowdAdapter?.getItem(position)?.status = 3
                        crowdAdapter?.notifyDataSetChanged()
                    }else{
                        crowdAdapter?.remove(position)
                    }
                }
            }
            else if(requestCode == Consts.REQUEST_CFORDER_DETAIL){
                if(data != null){
                    var position= data.getIntExtra("position",-1)
                    var newStatus= data.getIntExtra("newStatus",-1)
                    if(position != -1 && newStatus != -1){
                        upateStatus(position,newStatus)
                    }
                }
            }
        }
    }
}
