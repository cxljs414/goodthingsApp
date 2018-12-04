package com.goodthings.app.activity.addaddr

import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.AddressBean
import com.goodthings.app.bean.CommonResult
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/2
 * 修改内容：
 * 最后修改时间：
 */
class AddAddrPresenterImpl :
        BasePresenterImpl<AddAddrContract.AddAddrView>(),
        AddAddrContract.AddAddrPresenter{
    private var addrid = -1
    override fun getAddrInfo(addrId: Int) {
        addrid = addrId
        ApiManager.getAddress(addrId)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<AddressBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null){
                                mView?.showAddrInfo(it)
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

    override fun addAddress(name: String,
                            phone: String,
                            city: String,
                            city_code: String,
                            addr: String,
                            is_default: Boolean) {

        mView?.showProgressDialog("正在保存...")
        var default:Int = 1
        if(is_default)default = 1 else default = 2
        var flowable: Flowable<CommonResult>
        flowable = if(addrid == -1){
            ApiManager.addAddress(Consts.user?.id!!,name,phone,city,addr,city_code,default)
        }else{
            ApiManager.updateAddress(addrid,Consts.user?.id!!,name,phone,city,addr,city_code,default)
        }
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it.code == 2000){
                                mView?.initResult()
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