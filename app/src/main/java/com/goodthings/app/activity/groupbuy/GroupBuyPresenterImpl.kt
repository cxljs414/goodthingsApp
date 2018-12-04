package com.goodthings.app.activity.groupbuy

import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.CrowdOrderBean
import com.goodthings.app.bean.GroupListBean
import com.goodthings.app.bean.PageBean
import com.goodthings.app.http.ApiManager
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/29
 * 修改内容：
 * 最后修改时间：
 */
class GroupBuyPresenterImpl:BasePresenterImpl<GroupBuyContract.GroupBuyView>(),
        GroupBuyContract.GroupBuyPresenter {

    var page:Int = 1
    override fun loadData() {
        collageQuery(false)
    }

    override fun loadMore() {
        page+=1
        collageQuery(true)
    }

    private fun collageQuery(isMore:Boolean){
        mView?.showProgressDialog("正在加载数据...")
        ApiManager.collageQuery(page ,10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<PageBean<GroupListBean>>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                    if(isMore)page-=1
                }.subscribe({
                    mView?.hideProgressDialog()
                    if(it?.pageList == null || it?.pageList!!.isEmpty()){
                        mView?.noMoreData()
                        return@subscribe
                    }
                    mView?.notifyGroupBuyListUpdate(it.pageList!!)
                },{
                    if(isMore)page-=1
                    mView?.hideProgressDialog()
                })
    }
}