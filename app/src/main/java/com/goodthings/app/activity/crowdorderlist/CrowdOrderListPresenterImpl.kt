package com.goodthings.app.activity.crowdorderlist

import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.CommonResult
import com.goodthings.app.bean.CrowdOrderBean
import com.goodthings.app.bean.PageBean
import com.goodthings.app.http.ApiManager
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
class CrowdOrderListPresenterImpl :
        BasePresenterImpl<CrowdOrderListContract.CrowdOrderListView>(),
        CrowdOrderListContract.CrowdOrderListPresenter{

    private var orderListMap:MutableMap<Int,MutableList<CrowdOrderBean>>? = HashMap()
    private var pageMap:MutableMap<Int,Int>? = HashMap()

    //0全部 1未支付 2已取消3已支付 4已发货 5已完成/未评价 6已评价 7已申请退款 8已退款 9退款拒绝',
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
        ApiManager.getCrowdOrderlist(Consts.user?.id!!,page,status)//378
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<PageBean<CrowdOrderBean>>(ActivityEvent.DESTROY))
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

    override fun cancelOrder(bean: CrowdOrderBean, position: Int) {

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

    override fun sureTake(bean: CrowdOrderBean, position: Int) {
        mView?.showProgressDialog("正在确认收货...")
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
}