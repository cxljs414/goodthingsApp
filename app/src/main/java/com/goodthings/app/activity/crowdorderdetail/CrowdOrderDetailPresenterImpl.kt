package com.goodthings.app.activity.crowdorderdetail

import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.CommonResult
import com.goodthings.app.bean.CrowdOrderBean
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/8
 * 修改内容：
 * 最后修改时间：
 */
class CrowdOrderDetailPresenterImpl:
        BasePresenterImpl<CrowdOrderDetailContract.CrowdOrderDetailView>(),
        CrowdOrderDetailContract.CrowdOrderDetailPresenter{

    private var position:Int = -1
    private var orderBean:CrowdOrderBean? = null
    override fun startLoadData() {
        var orderNo = mView?.getIntent()?.getStringExtra("orderNo")!!
        position = mView?.getIntent()?.getIntExtra("position",-1)!!
        if(!orderNo.isNullOrEmpty()){
            //获取订单详情
            getOrderByOrderNo(orderNo)
        }
    }

    private fun getOrderByOrderNo(orderNo: String) {
        ApiManager.getCfOrderByOrderNo(orderNo)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<CrowdOrderBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null){
                                orderBean = it
                                mView?.showCrowdOrderDetail(it)
                            }
                            mView?.hideProgressDialog()

                        },
                        {
                            mView?.showMessage(it.message)
                            mView?.hideProgressDialog()
                        },
                        {
                        }
                )
    }

    override fun cancelOrder(bean: CrowdOrderBean) {
        mView?.showProgressDialog("正在取消订单...")
        ApiManager.qxCfOrder(bean.order_no, bean.count, bean.eId,bean.status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            mView?.hideProgressDialog()
                            if(it.code == 2000){
                                mView?.showMessage("订单取消成功")
                                bean.status = 2
                                mView?.showCrowdOrderDetail(bean)
                            }
                        },
                        {
                            mView?.showMessage(it.message)
                            mView?.hideProgressDialog()
                        },
                        {
                        }
                )
    }

    override fun successPay() {
        orderBean?.status = 3
        mView?.showCrowdOrderDetail(orderBean)
    }
    override fun sureTake() {
        if(orderBean != null){
            mView?.showProgressDialog("正在确认收货...")
            ApiManager.qxCfOrder(orderBean?.order_no!!, orderBean?.count!!, orderBean?.eId!!,orderBean?.status!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                    .doOnError {
                        mView?.showMessage(it.message)
                        mView?.hideProgressDialog()
                    }
                    .subscribe(
                            {
                                mView?.hideProgressDialog()
                                if(it.code == 2000){
                                    mView?.showMessage("确认收货成功")
                                    orderBean?.status = 5
                                    mView?.showCrowdOrderDetail(orderBean)
                                }
                            },
                            {
                                mView?.showMessage(it.message)
                                mView?.hideProgressDialog()
                            },
                            {
                            }
                    )
        }
    }
}