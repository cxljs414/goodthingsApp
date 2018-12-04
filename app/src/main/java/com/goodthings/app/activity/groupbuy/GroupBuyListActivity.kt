package com.goodthings.app.activity.groupbuy

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.goodthings.app.R
import com.goodthings.app.activity.groupbuydetail.GroupBuyDetailActivity
import com.goodthings.app.activity.groupbuyorders.GroupBuyOrdersActivity
import com.goodthings.app.activity.login.LoginActivity
import com.goodthings.app.adapter.GroupBuyListAdapter
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.GroupListBean
import com.goodthings.app.util.ScreenUtil
import com.goodthings.app.util.SpacesItemDecorationtwo
import kotlinx.android.synthetic.main.activity_group_buy_list.*
import kotlinx.android.synthetic.main.top_bar.*

class GroupBuyListActivity :
        BaseActivity<GroupBuyContract.GroupBuyView,GroupBuyContract.GroupBuyPresenter>(),
        GroupBuyContract.GroupBuyView, BaseQuickAdapter.RequestLoadMoreListener {

    override var presenter: GroupBuyContract.GroupBuyPresenter = GroupBuyPresenterImpl()
    private var adapter: GroupBuyListAdapter? = null
    private var nextOption:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_buy_list)
        toptitle.text = "拼团"
        topback.setOnClickListener { onBackPressed() }
        initGroupRecyclerview()
        presenter.loadData()
        groupbuy_bottom_bar.setOnClickListener {
            if(Consts.isLogined){
                startActivity(Intent(this@GroupBuyListActivity,GroupBuyOrdersActivity::class.java))
            }else{
                nextOption = 1//登录
                startActivity(Intent(this@GroupBuyListActivity,LoginActivity::class.java))
            }
        }
    }

    private fun initGroupRecyclerview() {
        groupbuy_recyclerview.layoutManager = LinearLayoutManager(this)
        groupbuy_recyclerview.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this,15f)))
        var imageWidth= ScreenUtil.getScreenWidth(this) - ScreenUtil.dip2px(this,30f)
        adapter = GroupBuyListAdapter(ArrayList(),imageWidth,false)
        groupbuy_recyclerview.adapter = adapter
        adapter?.setOnItemChildClickListener { adapter, view, position ->
            var groupBean:GroupListBean = adapter.getItem(position) as GroupListBean
            var intent = Intent(this@GroupBuyListActivity,GroupBuyDetailActivity::class.java)
            intent.putExtra("groupBuyId",groupBean.cId)
            startActivity(intent)
        }
        adapter?.setEnableLoadMore(true)
        adapter?.setOnLoadMoreListener(this)
    }

    override fun notifyGroupBuyListUpdate(pageList: List<GroupListBean>) {
        adapter?.setNewData(pageList)
        adapter?.notifyDataSetChanged()
        adapter?.loadMoreComplete()
    }

    override fun onResume() {
        super.onResume()
        if(nextOption == 1){
            nextOption = -1
            if(Consts.isLogined){
                startActivity(Intent(this@GroupBuyListActivity,GroupBuyOrdersActivity::class.java))
            }
        }
    }

    override fun onLoadMoreRequested() {
        presenter.loadMore()
    }

    override fun noMoreData() {
        adapter?.loadMoreComplete()
        adapter?.loadMoreEnd()
    }

}
