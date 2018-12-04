package com.goodthings.app.activity.addrlist

import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.AddressBean
import com.goodthings.app.bean.CrowdOrderBean
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/3
 * 修改内容：
 * 最后修改时间：
 */
class AddrListPresenterImpl :
        BasePresenterImpl<AddrListContract.CityListView>(),
        AddrListContract.CityListPresenter{
    override fun loadData() {
        if(!Consts.isLogined)return
        ApiManager.queryMyAddress(Consts.user!!.id)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<List<AddressBean>>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null){
                                mView?.showAddrList(it)
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