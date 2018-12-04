package com.goodthings.app.activity.groupbuyordersdetail

import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.CommonResult
import com.goodthings.app.bean.GroupOrderDetailBean
import com.goodthings.app.bean.ShareParamBean
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/29
 * 修改内容：
 * 最后修改时间：
 */
class GroupBuyOrderDetailPresenterImpl :
        BasePresenterImpl<GroupBuyOrderDetailContract.GroupBuyOrderDetailView>(),
        GroupBuyOrderDetailContract.GroupBuyOrderDetailPresenter{
    private var position:Int = -1
    private var orderBean: GroupOrderDetailBean? = null
    override fun startLoadData() {
        var orderNo = mView?.getIntent()?.getStringExtra("orderNo")!!
        position = mView?.getIntent()?.getIntExtra("position",-1)!!
        if(orderNo.isNotEmpty()){
            //获取订单详情
            getOrderByOrderId(orderNo)
        }
    }

    fun getOrderByOrderId(orderNo: String) {
        ApiManager.getCollageOrderDetailByOrderNo(orderNo)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<GroupOrderDetailBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null){
                                orderBean = it!!
                                mView?.showGroupOrderContent(it)
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

    override fun sureTake() {
        if(orderBean == null)return
        mView?.showProgressDialog("正在确认收货...")
        ApiManager.qxCollageOrder(orderBean?.status!!,orderBean?.id!!)
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
                                mView?.showGroupOrderContent(orderBean)
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

    override fun cancelOrder(orderBean: GroupOrderDetailBean) {
        mView?.showProgressDialog("正在取消订单...")
        ApiManager.qxCollageOrder(orderBean.status,orderBean.id)
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
                                orderBean.status = 1
                                mView?.showGroupOrderContent(orderBean)
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
        orderBean?.status = 2
        mView?.showGroupOrderContent(orderBean)
    }

    override fun isComplete(tId: Int) {
        ApiManager.isComplete(tId)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<Boolean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                }
                .subscribe(
                        {
                            mView?.isCompleteDialog(it)
                        },
                        {
                            mView?.showMessage(it.message)
                        },
                        {
                        }
                )
    }

    override fun shareParam(pid: Int) {
        mView?.showProgressDialog("正在请求数据...")
        ApiManager.shareParam(pid, Consts.user?.id!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<ShareParamBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }.subscribe({
                    mView?.hideProgressDialog()
                    mView?.notifyShare(it)
                },{
                    mView?.hideProgressDialog()
                })
    }
}