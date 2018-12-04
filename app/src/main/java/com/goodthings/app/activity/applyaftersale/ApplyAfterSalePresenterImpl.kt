package com.goodthings.app.activity.applyaftersale

import android.graphics.BitmapFactory
import android.net.Uri
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.AfterSaleBean
import com.goodthings.app.bean.BaseResult
import com.goodthings.app.bean.ExpressDetailBean
import com.goodthings.app.bean.GroupOrderDetailBean
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.AppUtil
import com.goodthings.app.util.RxUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/20
 * 修改内容：
 * 最后修改时间：
 */
class ApplyAfterSalePresenterImpl:
        BasePresenterImpl<ApplyAfterSaleContract.ApplyAfterSaleView>(),
        ApplyAfterSaleContract.ApplyAfterSalePresenter{
    private var type:Int = -1
    private var orderNo= ""
    private var applyId:Int= -1
    private var returnOrderNo=""
    private var orderBean:GroupOrderDetailBean?= null

    override fun afterAttachView() {
        super.afterAttachView()
        type = mView?.getIntent()!!.getIntExtra("type",-1)
        orderNo = mView?.getIntent()!!.getStringExtra("orderNo")
        applyId = mView?.getIntent()!!.getIntExtra("applyId",-1)
        if(mView?.getIntent()!!.hasExtra("returnOrderNo")){
            returnOrderNo= mView?.getIntent()!!.getStringExtra("returnOrderNo")
        }
    }

    override fun start() {
        getOrderByOrderNo()
        if(applyId != -1){
            //修改售后申请
            loadAfterSaleOrderDetail()
        }
    }

    private fun loadAfterSaleOrderDetail() {
        mView?.showProgressDialog("正在查询..")
        ApiManager.getReturnApply(type,orderNo)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<AfterSaleBean>(ActivityEvent.DESTROY))
                .doOnError { mView?.hideProgressDialog() }
                .subscribe({
                    it?.cstatus = 0
                    mView?.showApplyContent(it)
                },{
                    mView?.showMessage(it.message)
                },{
                    mView?.hideProgressDialog()
                })
    }

    fun getOrderByOrderNo() {
        ApiManager.getCollageOrderDetailByOrderNo(orderNo)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<GroupOrderDetailBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            it.let {
                                mView?.showGroupOrderContent(it)
                                orderBean = it
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

    override fun uploadImage(data: Uri?) {
        if(data == null)return
        val imgString= AppUtil.Bitmap2StrByBase64(BitmapFactory.decodeFile(data.path))
        mView?.showProgressDialog("正在上传图片..")
        ApiManager.uploadimage(imgString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<BaseResult<String>>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it.code == 2000){
                                mView?.addPic(it.data)
                            }else{
                                mView?.showMessage(it.msg)
                            }
                        },
                        {
                        },{
                    mView?.hideProgressDialog()
                }
                )
    }

    override fun commitApply(applyType: Int, status: Int, cause: String, explain: String, phone: String, picsList: MutableList<String>) {
        mView?.showProgressDialog("正在提交申请")
        var picStr = StringBuilder()
        picsList.forEach {
            if(it!="add") {
                if (picStr.isNotEmpty()) {
                    picStr.append(",")
                }
                picStr.append(it)
            }
        }
        var call:Call<String> = if(applyId != -1){
            ApiManager.updateOrderReturnApplyRes(type,orderNo,applyType,status,cause,
                    "${orderBean?.price!! * orderBean?.buy_num!!}",
                    explain,phone,picStr.toString(),Consts.user?.id!!,applyId,returnOrderNo)
        }else{
            ApiManager.addOrderReturnApplyRes(type,orderNo,applyType,status,cause,
                    "${orderBean?.price!! * orderBean?.buy_num!!}",
                    explain,phone,picStr.toString(),Consts.user?.id!!)
        }
        call.enqueue(object:Callback<String>{
                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        mView?.showMessage(t?.message)
                        mView?.hideProgressDialog()
                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        var result= response?.body().toString()
                        if(result.isNullOrEmpty()){
                            mView?.showMessage("申请失败")
                        }else{
                            try {
                                val bean = Gson().fromJson<BaseResult<AfterSaleBean>>(result, object : TypeToken<BaseResult<AfterSaleBean>>() {}.type)
                                bean.let {
                                    if(bean.code == 2000){
                                        mView?.commitSuccess()
                                    }else{
                                        mView?.showMessage(bean.msg)
                                    }
                                }
                            }catch (e:Exception){
                                mView?.showMessage("申请失败")
                            }
                            mView?.hideProgressDialog()
                        }
                    }
                })

    }
}