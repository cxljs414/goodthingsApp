package com.goodthings.app.activity.express

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.goodthings.app.R
import com.goodthings.app.adapter.ExpressAdapter
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.ExpressDetailBean
import com.goodthings.app.util.SpacesItemDecorationtwo
import com.goodthings.app.util.onClick
import kotlinx.android.synthetic.main.activity_express.*
import kotlinx.android.synthetic.main.top_bar.*

class ExpressActivity :
        BaseActivity<ExpressContract.ExpressView,ExpressContract.ExpressPresenter>(),
        ExpressContract.ExpressView{
    override var presenter: ExpressContract.ExpressPresenter= ExpressPresenterImpl()
    private var expressAdapter:ExpressAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_express)
        toptitle.text = "物流详情"
        topback.onClick { onBackPressed() }
        initRecyclerview()
        presenter.start()
    }

    private fun initRecyclerview() {
        express_recyclerview.layoutManager = LinearLayoutManager(this)
        express_recyclerview.addItemDecoration(SpacesItemDecorationtwo(0, 0))
        expressAdapter = ExpressAdapter(ArrayList())
        express_recyclerview.adapter = expressAdapter
    }

    override fun showExpressDetail(it: ExpressDetailBean?) {
        it?.let {
            Glide.with(this).load(Consts.IMAGEURL+it.coverPic).into(express_image)
            express_title.text = it.title
            express_company.text = "承运公司：${it.logisCompany}"
            express_billno.text = "订单编号：${it.loggisNo}"
            expressAdapter?.setNewData(it.traces.reversed())
            expressAdapter?.notifyDataSetChanged()
        }
    }
}
