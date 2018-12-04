package com.goodthings.app.activity.topnews

import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.MainFragProdBean
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.goodthings.app.util.SPUtil
import com.trello.rxlifecycle2.android.FragmentEvent
import org.joda.time.LocalDate

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/11
 * 修改内容：
 * 最后修改时间：
 */
class TopNewsPresenterImpl : BasePresenterImpl<TopNewsContract.TopNewsView>(),TopNewsContract.TopNewsPresenter {
    override fun getTopNews() {
        var cityCode = SPUtil.getStringValue(mView?.getContext()!!,"cityCode","110000")
        ApiManager.getNotFixContentList(9,cityCode, LocalDate.now().toString("yyyy-MM-dd"))
                .compose(RxUtil.hanlderBaseResult())
                .compose(fragmentLifecycle?.bindUntilEvent<List<MainFragProdBean>>(FragmentEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            it?.forEach {
                                it.isAdver = true
                            }
                            mView?.notifyTopNewsUpdate(it)
                        },
                        {
                            mView?.showMessage(it.message)
                        },
                        {
                        }
                )
    }

}