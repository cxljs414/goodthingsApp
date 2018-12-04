package com.goodthings.app.activity.aftersaledetail.fragment.writeexpress

import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.CommonResult
import com.goodthings.app.bean.LogisBean
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/24
 * 修改内容：
 * 最后修改时间：
 */
class WriteExpressPresenterImpl :
        BasePresenterImpl<WriteExpressContract.WriteExPressView>(),
        WriteExpressContract.WriteExpressPresenter{

    override fun start() {
        mView?.showProgressDialog("正在请求数据..")
        ApiManager.getLogisList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(fragmentLifecycle?.bindUntilEvent<List<LogisBean>>(FragmentEvent.DESTROY))
                .doOnError { mView?.hideProgressDialog() }
                .subscribe({
                    mView?.showLogisList(it)
                },{
                    mView?.showMessage(it.message)
                },{
                    mView?.hideProgressDialog()
                })
    }

    override fun expressCommit(selectLogis: LogisBean, expressNo: String, order_no: String) {
        mView?.showProgressDialog("正在提交..")
        ApiManager.addOrderLogis(selectLogis.name,selectLogis.no,order_no,expressNo)
                .compose(RxUtil.hanlderCommResult())
                .compose(fragmentLifecycle?.bindUntilEvent<CommonResult>(FragmentEvent.DESTROY))
                .doOnError { mView?.hideProgressDialog() }
                .subscribe({
                    if(it.code == 2000){
                        mView?.showMessage("提交成功")
                        mView?.getActivity()!!.finish()
                    }else{
                        mView?.showMessage(it.msg)
                    }
                },{
                    mView?.showMessage(it.message)
                },{
                    mView?.hideProgressDialog()
                })
    }

}