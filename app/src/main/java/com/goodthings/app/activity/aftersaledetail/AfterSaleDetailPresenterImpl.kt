package com.goodthings.app.activity.aftersaledetail

import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.AfterSaleBean
import com.goodthings.app.bean.GroupOrderDetailBean
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/23
 * 修改内容：
 * 最后修改时间：
 */
class AfterSaleDetailPresenterImpl:
        BasePresenterImpl<AfterSaleDetailContract.AfterSaleDetailView>(),
        AfterSaleDetailContract.AfterSaleDetailPresenter{
    private var type:Int = 1
    private var orderNo:String = ""

    override fun afterAttachView() {
        super.afterAttachView()
        mView?.getIntent().let {
            type = mView?.getIntent()!!.getIntExtra("type",-1)
            orderNo = mView?.getIntent()!!.getStringExtra("orderNo")
        }

    }
    override fun loadAfterSaleOrderDetail() {
        mView?.showProgressDialog("正在查询..")
        ApiManager.getReturnApply(type,orderNo)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<AfterSaleBean>(ActivityEvent.DESTROY))
                .doOnError { mView?.hideProgressDialog() }
                .subscribe({
                    mView?.showContent(it)
                },{
                    mView?.showMessage(it.message)
                },{
                    mView?.hideProgressDialog()
                })
    }
}