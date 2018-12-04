package com.goodthings.app.activity.groupbuyorders

import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.CommonResult
import com.goodthings.app.bean.GroupOrderBean
import com.goodthings.app.bean.PageBean
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
class GroupBuyOrdersPresenterImpl :
        BasePresenterImpl<GroupBuyOrdersContract.GroupBuyOrdersView>(),
        GroupBuyOrdersContract.GroupBuyOrdersPresenter{

    private var orderListMap:MutableMap<Int,MutableList<GroupOrderBean>>? = HashMap()
    private var pageMap:MutableMap<Int,Int>? = HashMap()

    //筛选码：1全部 2待付款 3待分享 4待发货 5已发货 6已完成 ,
    override fun requestAllOrder(isMore:Boolean,status:Int) {
        var page = 1
        if(isMore) {
            if(pageMap?.containsKey(status)!!){
                page = pageMap?.get(status)!!
            }
            page += 1
        }else{
            page = 1
            pageMap?.put(status,1)
            mView?.showProgressDialog("正在加载订单列表...")
        }
        ApiManager.myCollageOrderlist(Consts.user?.id!!,page,10,status)//378
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<PageBean<GroupOrderBean>>(ActivityEvent.DESTROY))
                .doOnError {
                    if(isMore)page -=1
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            mView?.hideProgressDialog()
                            if(it?.pageList != null && it.pageList!!.isNotEmpty()){
                                if(isMore){
                                    pageMap?.put(status,page)
                                    orderListMap?.get(status)?.addAll(it?.pageList!!)
                                }else{
                                    orderListMap?.remove(status)
                                    orderListMap?.put(status,it?.pageList!!.toMutableList())
                                }
                                mView?.showSelectStatusOrderList(this.orderListMap?.get(status)!!)
                            }else{
                                if(isMore){
                                    mView?.noMoreLoad()
                                }else{
                                    orderListMap?.put(status,ArrayList())
                                    mView?.showSelectStatusOrderList(this.orderListMap?.get(status)!!)
                                }
                            }

                        },
                        {
                            if(isMore)page -=1
                            mView?.showMessage(it.message)
                            mView?.hideProgressDialog()
                        },
                        {
                        }
                )
    }

    override fun cancelOrder(bean: GroupOrderBean, position: Int) {
        mView?.showProgressDialog("正在取消订单...")
        ApiManager.qxCollageOrder(bean.status,bean.oId)
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
                                mView?.upateStatus(position,2)
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

    override fun sureTake(bean: GroupOrderBean, position: Int) {
        mView?.showProgressDialog("正在确认收货...")
        ApiManager.qxCollageOrder(bean.status,bean.oId)
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
                                mView?.upateStatus(position,5)
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

    override fun isComplete(bean: GroupOrderBean, position: Int) {
        ApiManager.isComplete(bean.tId)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<Boolean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                }
                .subscribe(
                        {
                            mView?.isCompleteDialog(it,bean,position)
                        },
                        {
                            mView?.showMessage(it.message)
                        },
                        {
                        }
                )
    }
}