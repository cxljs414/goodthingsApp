package com.goodthings.app.activity.express

import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.BaseResult
import com.goodthings.app.bean.ExpressDetailBean
import com.goodthings.app.bean.GroupOrderDetailBean
import com.goodthings.app.http.ApiManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/18
 * 修改内容：
 * 最后修改时间：
 */
class ExpressPresenterImpl :
        BasePresenterImpl<ExpressContract.ExpressView>(),ExpressContract.ExpressPresenter {

    private var shipperCode:String = ""
    private var billNo:String = ""
    private var orderNo:String= ""
    private var loginCompany:String = ""

    override fun afterAttachView() {
        super.afterAttachView()
        shipperCode = mView?.getIntent()!!.getStringExtra("shipperCode")
        billNo = mView?.getIntent()!!.getStringExtra("billNo")
        orderNo = mView?.getIntent()!!.getStringExtra("orderNo")
        loginCompany = mView?.getIntent()!!.getStringExtra("loginCompany")
    }

    override fun start() {
        mView?.showProgressDialog("正在查询..")
        var flowable1:Flowable<ExpressDetailBean> = Flowable.create( {
            ApiManager.queryLogisDetail(shipperCode,billNo)
                    .enqueue(object:Callback<String> {
                        override fun onFailure(call: Call<String>?, t: Throwable?) {
                            it.onError(t)
                            it.onComplete()
                        }

                        override fun onResponse(call: Call<String>?, response: Response<String>?) {
                            var result= response?.body().toString()
                            val bean = Gson().fromJson<ExpressDetailBean>(result, object : TypeToken<ExpressDetailBean>() {}.type)
                            if(bean != null){
                                it.onNext(bean)
                            }else{
                                it.onError(Throwable("获取失败"))
                            }
                            it.onComplete()
                        }

                    })
        },BackpressureStrategy.BUFFER)
        var flowable2:Flowable<BaseResult<GroupOrderDetailBean>> =  ApiManager.getCollageOrderDetailByOrderNo(orderNo)
        Flowable.zip(flowable1,flowable2, BiFunction<ExpressDetailBean, BaseResult<GroupOrderDetailBean>,ExpressDetailBean> {
            bean, baseResult -> joinBean(bean,baseResult) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<ExpressDetailBean>(ActivityEvent.DESTROY))
                .doOnError { mView?.hideProgressDialog() }
                .subscribe({
                    mView?.showExpressDetail(it)
                },{
                    mView?.showMessage(it.message)
                },{
                    mView?.hideProgressDialog()
                })

    }

    private fun joinBean(express:ExpressDetailBean, baseResult:BaseResult<GroupOrderDetailBean>):ExpressDetailBean{
        express.coverPic = baseResult.data.cover_url
        express.title = baseResult.data.title
        express.logisCompany = loginCompany
        express.loggisNo = billNo
        return express
    }

}

