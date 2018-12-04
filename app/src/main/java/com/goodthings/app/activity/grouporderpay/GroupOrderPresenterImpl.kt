package com.goodthings.app.activity.grouporderpay

import android.content.Intent
import android.os.Handler
import android.os.Message
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.*
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/6/4
 * 修改内容：
 * 最后修改时间：
 */
class GroupOrderPresenterImpl :
        BasePresenterImpl<GroupOrderContract.GroupOrderView>(),
        GroupOrderContract.GroupOrderPresenter{

    private var orderNo:String = ""
    private var groupCount:Int = 1
    private var oId:Int = -1
    private var isAlone= false
    private var detailId:Int = -1
    private var teamId:Int = -1
    lateinit var api: IWXAPI
    override fun afterAttachView() {
        super.afterAttachView()
        api = WXAPIFactory.createWXAPI(mView?.getContext(),null)
        api.registerApp(Consts.WXAPPID)
    }
    /**
     * 支付页面有两个入口
     * 1、预购详情，未生成订单
     * 2、订单列表，已有订单,获取订单详情
     * 如果已经有订单，点击支付，不再生成订单，直接调用支付
     */
    override fun startLoadData() {
        var intent= mView?.getIntent()
        if(intent?.hasExtra("orderNo")!!){
            orderNo = intent.getStringExtra("orderNo")
            oId = intent.getIntExtra("oId",-1)
            //获取订单详情,orderNo
            getOrderById()
        }else{
            var groupId= intent.getIntExtra("groupId",0)
            groupCount= intent.getIntExtra("count",0)
            isAlone = intent.getBooleanExtra("isAlone",false)
            detailId = intent.getIntExtra("detailId",-1)
            teamId = intent.getIntExtra("teamId",-1)
            getCollageDetail(groupId)
            queryDefaultAddr()
        }

    }

    private fun getOrderById() {
        ApiManager.gerCollageOrderDetail(oId)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<GroupOrderDetailBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null){
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

    private fun getCollageDetail(groupId:Int){
        ApiManager.getCollageDetail(groupId)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<GroupBuyDetailBean>(ActivityEvent.DESTROY))
                .doOnError { mView?.hideProgressDialog() }
                .subscribe({
                    mView?.hideProgressDialog()
                    mView?.showGroupDetailContent(it,groupCount,isAlone)
                },{
                    mView?.hideProgressDialog()
                })
    }

    override fun queryDefaultAddr() {
        if(Consts.isLogined) {
            ApiManager.queryDefaultAddr(Consts.user?.id!!)
                    .compose(RxUtil.hanlderBaseResult())
                    .compose(lifecycle?.bindUntilEvent<AddressBean>(ActivityEvent.DESTROY))
                    .doOnError {
                        mView?.showMessage(it.message)
                        mView?.hideProgressDialog()
                    }
                    .subscribe(
                            {
                                if (it != null) {
                                    mView?.showDefaultAddress(it)
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
    }

    override fun addOrder(prodId: Int, price: Double, count: Int,adrId: Int) {
        if(orderNo.isEmpty()) {
            mView?.showProgressDialog("正在生成订单...")
            ApiManager.addGroupOrder(price * count, 0.00,
                    prodId, detailId, count, adrId, Consts.user?.id!!,teamId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                    .doOnError {
                        mView?.showMessage(it.message)
                        mView?.hideProgressDialog()
                    }
                    .subscribe(
                            {
                                if (it?.code == 2000) {
                                    if (it?.data != null) {
                                        orderNo = it.data!!
                                        mView?.setAddOrder(true,orderNo)
                                        getWxPayInit(it.data!!)
                                        return@subscribe
                                    }
                                } else if (it?.code == 4000) {
                                    mView?.showMessage(it.msg)
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
        }else{
            getWxPayInit(orderNo)
        }
    }

    private fun getWxPayInit(orderNo: String) {
        ApiManager.getWxPayInit(Consts.user?.id!!,orderNo,3)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<WxPayInitBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null){
                                callWX(it)
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

    /**
     * 调用微信
     */
    private fun callWX(content: WxPayInitBean) {
        Thread(Runnable {
            kotlin.run {
                /*val url = "http://wxpay.wxutil.com/pub_v2/app/app_pay.php"
                val buf = WXUtil.httpGet(url)*/
                val msg = Message()
                msg.what = 1
                try {
                    val req = PayReq()
                    req.appId = content.appid
                    req.partnerId = content.partnerid
                    req.prepayId = content.prepayid
                    req.timeStamp = content.timestamp
                    req.packageValue = "Sign=WXPay"
                    req.nonceStr = content.noncestr
                    req.sign = "WXPay"
                    req.extData = "app data" // optional
                    api!!.sendReq(req)
                    msg.obj = ""
                }catch (e:Exception){
                    msg.obj = "异常：" + e.message
                    // Toast.makeText(this@PayActivity, "异常：" + e.message, Toast.LENGTH_SHORT).show()
                }
                hanlder.sendMessage(msg)
            }
        }).start()
    }

    private var hanlder: Handler = object: Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                1-> if(msg.obj != null && msg.obj != ""){
                    mView!!.showMessage(msg.obj as String)
                }
            }
        }
    }

}