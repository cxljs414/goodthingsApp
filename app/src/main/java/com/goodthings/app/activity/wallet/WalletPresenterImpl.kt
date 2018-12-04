package com.goodthings.app.activity.wallet

import android.util.Log
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.CommonResult
import com.goodthings.app.bean.HomeNewsBean
import com.goodthings.app.bean.WalletBean
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.goodthings.app.util.SPUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/3
 * 修改内容：
 * 最后修改时间：
 */
class WalletPresenterImpl : BasePresenterImpl<WalletContract.WalletView>(),WalletContract.WalletPresenter{

    override fun start() {
        getMyWallet()
    }

    private fun getMyWallet() {
        var user = SPUtil.getUserBean(mView?.getContext()!!)
        if(user != null){
            mView?.showProgressDialog("正在请求数据...")
            ApiManager.getMyWallet(user.id)
                    .compose(RxUtil.hanlderBaseResult())
                    .compose(lifecycle?.bindUntilEvent<WalletBean>(ActivityEvent.DESTROY))
                    .doOnError {
                        mView?.hideProgressDialog()
                    }
                    .subscribe(
                            {
                                Log.i("wallet","获取成功"+it.coinBalance)
                                if(it != null){
                                    mView?.updateData(it)
                                }
                                mView?.hideProgressDialog()
                            },
                            {
                                Log.i("wallet","获取失败"+it.message)
                            },
                            {
                                mView?.hideProgressDialog()
                            }
                    )
        }

    }
}