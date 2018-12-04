package com.goodthings.app.activity.aftersaledetail.fragment.checking

import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.CommonResult
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.trello.rxlifecycle2.android.FragmentEvent

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/23
 * 修改内容：
 * 最后修改时间：
 */
class AfterSaleCheckingPresenterImpl :
        BasePresenterImpl<AfterSaleCheckingContract.AfterSaleCheckingView>(),
        AfterSaleCheckingContract.AfterSaleCheckingPresenter {
    override fun cancelApply(applyId: Int?) {
        mView?.showProgressDialog("正在取消..")
        ApiManager.cancelOrderReturnApply(applyId!!)
                .compose(RxUtil.hanlderCommResult())
                .compose(fragmentLifecycle?.bindUntilEvent<CommonResult>(FragmentEvent.DESTROY))
                .doOnError { mView?.hideProgressDialog() }
                .subscribe({
                    mView?.showMessage("取消成功")
                    mView?.getActivity()!!.finish()
                },{
                    mView?.showMessage(it.message)
                },{
                    mView?.hideProgressDialog()
                })
    }

    override fun againApply(applyId: Int?) {
        applyId?.let {
            mView?.showProgressDialog("正在提交..")
            ApiManager.againOrderReturnApply(applyId)
                    .compose(RxUtil.hanlderCommResult())
                    .compose(fragmentLifecycle?.bindUntilEvent<CommonResult>(FragmentEvent.DESTROY))
                    .doOnError { mView?.hideProgressDialog() }
                    .subscribe({
                        mView?.showMessage("提交成功")
                        mView?.getActivity()!!.finish()
                    },{
                        mView?.showMessage(it.message)
                    },{
                        mView?.hideProgressDialog()
                    })
        }
    }
}