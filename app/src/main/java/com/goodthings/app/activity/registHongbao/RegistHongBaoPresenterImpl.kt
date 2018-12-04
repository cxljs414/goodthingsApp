package com.goodthings.app.activity.registHongbao

import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.GoldBean
import com.goodthings.app.bean.PageBean
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.goodthings.app.util.SPUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/8
 * 修改内容：
 * 最后修改时间：
 */
class RegistHongBaoPresenterImpl:
        BasePresenterImpl<RegistHongBaoContract.RegistHongBaoView>(),
        RegistHongBaoContract.RegistHongBaoPresenter{
    override fun registShareGoldEnvelopes() {
        var user = SPUtil.getUserBean(mView?.getContext()!!)
        if(user != null) {
            ApiManager.registShareGoldEnvelopes(user.id)
                    .compose(RxUtil.hanlderBaseResult())
                    .compose(lifecycle?.bindUntilEvent<Int>(ActivityEvent.DESTROY))
                    .subscribe(
                            {
                                mView?.showMessage("分享成功，奖励${it}金币")
                                mView?.shareGoldSuccess()
                            },
                            {
                                mView?.showMessage(it.message)
                            }
                    )
        }
    }

}