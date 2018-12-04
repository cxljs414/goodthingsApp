package com.goodthings.app.activity.gold

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.goodthings.app.R
import com.goodthings.app.adapter.GoldAdapter
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.GoldBean
import com.goodthings.app.util.ScreenUtil
import com.goodthings.app.util.SpacesItemDecorationtwo
import kotlinx.android.synthetic.main.activity_gold.*
import kotlinx.android.synthetic.main.top_bar.*

class GoldActivity :
        BaseActivity<GoldContract.GoldView,GoldContract.GoldPresenter>(),
        GoldContract.GoldView, BaseQuickAdapter.RequestLoadMoreListener {

    override var presenter: GoldContract.GoldPresenter = GoldPresenterImpl()
    private var goldAdapter: GoldAdapter? = null
    private var goldRecordAdapter:GoldAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold)
        topback.setOnClickListener { onBackPressed() }
        toptitle.text = "金币账户"
        gold_rbgroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.gold_rb_gold ->{
                    gold_rv.visibility = View.VISIBLE
                    gold_record_rv.visibility = View.GONE
                    presenter?.setCurSelectedType(0)
                }
                R.id.gold_rb_record ->{
                    gold_rv.visibility = View.GONE
                    gold_record_rv.visibility = View.VISIBLE
                    presenter?.setCurSelectedType(1)
                }
            }
        }
        gold_rv.layoutManager = LinearLayoutManager(this!!, LinearLayoutManager.VERTICAL,false)
        gold_rv.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this!!,10f)))
        goldAdapter = GoldAdapter(ArrayList())
        goldAdapter?.setType(0)
        goldAdapter?.setEnableLoadMore(true)
        goldAdapter?.setOnLoadMoreListener(this)
        gold_rv.adapter = goldAdapter

        gold_record_rv.layoutManager = LinearLayoutManager(this!!, LinearLayoutManager.VERTICAL,false)
        gold_record_rv.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this!!,10f)))
        goldRecordAdapter = GoldAdapter(ArrayList())
        goldRecordAdapter?.setType(1)
        gold_record_rv.adapter = goldRecordAdapter

        presenter.start()
    }

    override fun notifyGoldDataChange(beanList: List<GoldBean>) {
        goldAdapter?.loadMoreComplete()
        if(beanList.isNotEmpty()){
            goldAdapter?.setNewData(beanList)
            goldAdapter?.notifyDataSetChanged()
        }else{
            goldAdapter?.loadMoreEnd(true)
        }
    }

    override fun notifyRecordDataChange(pageList: List<GoldBean>) {
        goldRecordAdapter?.loadMoreComplete()
        if(pageList.isNotEmpty()) {
            goldRecordAdapter?.setNewData(pageList)
            goldRecordAdapter?.notifyDataSetChanged()
        }else{
            goldRecordAdapter?.loadMoreEnd(true)
        }
    }

    override fun onLoadMoreRequested() {
        presenter?.loadMore()
    }

    override fun noMore(curSelectedType: Int) {
        if(curSelectedType == 0){
            goldAdapter?.loadMoreComplete()
            goldAdapter?.loadMoreEnd(true)
        }else{
            goldRecordAdapter?.loadMoreComplete()
            goldRecordAdapter?.loadMoreEnd(true)
        }
    }
}
