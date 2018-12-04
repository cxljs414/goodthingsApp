package com.goodthings.app.activity.groupbuyorders

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.goodthings.app.R
import com.goodthings.app.activity.addComment.AddCommentActivity
import com.goodthings.app.activity.groupbuydetail.GroupBuyDetailActivity
import com.goodthings.app.activity.groupbuyordersdetail.GroupBuyOrderDetailActivity
import com.goodthings.app.activity.grouporderpay.GroupOrderPayActivity
import com.goodthings.app.adapter.GroupBuyOrdersAdapter
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.GroupOrderBean
import com.goodthings.app.util.ScreenUtil
import com.goodthings.app.util.SpacesItemDecorationtwo
import kotlinx.android.synthetic.main.activity_group_buy_order_detail.*
import kotlinx.android.synthetic.main.activity_group_buy_orders.*
import kotlinx.android.synthetic.main.top_bar.*

class GroupBuyOrdersActivity :
        BaseActivity<GroupBuyOrdersContract.GroupBuyOrdersView,GroupBuyOrdersContract.GroupBuyOrdersPresenter>(),
        GroupBuyOrdersContract.GroupBuyOrdersView, BaseQuickAdapter.RequestLoadMoreListener {

    override var presenter: GroupBuyOrdersContract.GroupBuyOrdersPresenter=GroupBuyOrdersPresenterImpl()
    private var adapter: GroupBuyOrdersAdapter? = null
    private var curStatus= 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_buy_orders)
        toptitle.text = "我的拼团订单"
        topback.setOnClickListener { onBackPressed() }
        initTabLayout()
        initGroupBuyOrderList()
        presenter.requestAllOrder(false,1)
    }

    private fun initGroupBuyOrderList() {
        grouporders_recyclerview.layoutManager = LinearLayoutManager(this)
        grouporders_recyclerview.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this,15f)))
        adapter = GroupBuyOrdersAdapter(ArrayList())
        grouporders_recyclerview.adapter = adapter
        adapter?.setOnItemChildClickListener{ adapter, view, position ->
            var bean: GroupOrderBean = adapter?.getItem(position) as GroupOrderBean
            when(view.id){
                R.id.item_grouporders_root ->{
                    var intent= Intent(this@GroupBuyOrdersActivity,GroupBuyDetailActivity::class.java)
                    intent.putExtra("groupBuyId",bean.col_id)
                    startActivity(intent)
                }
                R.id.item_grouporders_detail -> {
                    var intent = Intent(this@GroupBuyOrdersActivity, GroupBuyOrderDetailActivity::class.java)
                    intent.putExtra("orderNo",bean.order_no)
                    intent.putExtra("position",position)
                    startActivityForResult(intent,Consts.REQUEST_GPORDER_DETAIL)
                }
                R.id.item_grouporders_cancel -> {
                    presenter.cancelOrder(bean,position)
                }
                R.id.item_grouporders_pay -> {
                    presenter.isComplete(bean,position)
                }
                R.id.item_grouporders_take -> {
                    presenter.sureTake(bean,position)
                }
                R.id.item_grouporders_comment ->{
                    var intent= Intent(this@GroupBuyOrdersActivity, AddCommentActivity::class.java)
                    intent.putExtra("orderNo",bean.order_no)
                    intent.putExtra("position",position)
                    startActivityForResult(intent,Consts.REQUEST_GPORDER_DETAIL)
                }
            }
        }
    }

    private fun goPay(bean: GroupOrderBean, position: Int) {
        var intent = Intent(this@GroupBuyOrdersActivity, GroupOrderPayActivity::class.java)
        intent.putExtra("orderNo",bean.order_no)
        intent.putExtra("oId",bean.oId)
        intent.putExtra("position",position)
        startActivityForResult(intent,Consts.REQUEST_ORDER_PAY)
    }

    private fun initTabLayout() {
        var array:MutableList<String> = ArrayList()
        array.add("全部")
        array.add("待付款")
        array.add("待分享")
        array.add("待发货")
        array.add("待收货")
        array.add("已完成")
        array.forEach {
            var tab = grouporders_tablayout.newTab()
            tab.text = it
            grouporders_tablayout.addTab(tab)
        }
        grouporders_tablayout.isSmoothScrollingEnabled = true
        grouporders_tablayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                curStatus = grouporders_tablayout.selectedTabPosition+1
                presenter.requestAllOrder(false,curStatus)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    override fun noMoreLoad() {
        adapter?.loadMoreComplete()
        adapter?.loadMoreEnd(true)
    }

    override fun showSelectStatusOrderList(orderList: List<GroupOrderBean>) {
        if(orderList?.size!! < 10){
            adapter?.setEnableLoadMore(false)
            adapter?.setOnLoadMoreListener(null)
        }else{
            adapter?.setEnableLoadMore(true)
            adapter?.setOnLoadMoreListener(this)
        }
        adapter?.setNewData(orderList)
        adapter?.notifyDataSetChanged()
        adapter?.loadMoreComplete()
    }

    override fun upateStatus(position: Int,newStatus:Int) {
        if(adapter?.getItem(position)?.status == newStatus)return
        if(curStatus == 1){
            adapter?.getItem(position)?.status = newStatus
            adapter?.notifyDataSetChanged()
        }else{
            adapter?.remove(position)
        }
    }

    override fun onLoadMoreRequested() {
        grouporders_recyclerview.post {
            presenter.requestAllOrder(true,curStatus)
            adapter?.loadMoreComplete()
        }
    }

    override fun isCompleteDialog(it: Boolean?, bean: GroupOrderBean, position: Int) {
        if(it!!){//true 未满可以支付
            goPay(bean,position)
        }else{//满了，弹框
            grouporder_dialog_root.visibility = View.GONE
            grouporder_dialog_root.alpha = 0f
            grouporder_dialog_root.visibility = View.VISIBLE
            grouporder_dialog_root.animate().alpha(1f).setDuration(300).start()
            grouporder_dialog_close.setOnClickListener {
                grouporder_dialog_root.visibility = View.GONE
            }
            grouporder_dialog_cancel.setOnClickListener {
                grouporder_dialog_root.visibility = View.GONE
            }
            grouporder_dialog_sure.setOnClickListener {
                grouporder_dialog_root.visibility = View.GONE
                goPay(bean,position)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Consts.REQUEST_ORDER_PAY){//支付成功
                var position:Int = data?.getIntExtra("position",-1)!!
                if(position != -1){
                    if(curStatus == 0){
                        adapter?.getItem(position)?.status = 3
                        adapter?.notifyDataSetChanged()
                    }else{
                        adapter?.remove(position)
                    }
                }
            }
            else if(requestCode == Consts.REQUEST_GPORDER_DETAIL){
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
