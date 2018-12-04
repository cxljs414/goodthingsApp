package com.goodthings.app.activity.crowdlist

import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.HomeRecomBean
import com.goodthings.app.bean.PageBean
import com.goodthings.app.bean.ProdCrowdBean
import com.goodthings.app.bean.User
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.goodthings.app.util.SPUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.LocalDate

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/25
 * 修改内容：
 * 最后修改时间：
 */
class ProductCrowdPresenterImpl :
        BasePresenterImpl<ProductCrowdContract.ProductCrowdView>(),
        ProductCrowdContract.ProductCrowdPresenter{
    private var page = 1
    private var crowdList:MutableList<ProdCrowdBean>? = null
    override fun start() {
        getCrowdBanner()
        crowdQuery(false)
        getCrowdBanner()
    }

    private fun getCrowdBanner() {
        var userId = -1
        var user: User? = SPUtil.getUserBean(mView?.getContext()!!)
        if(user != null)userId = user.id
        //检查是否登录
        var cityCode = SPUtil.getStringValue(mView?.getContext()!!,"cityCode","110000")
        ApiManager.getRecContentList(5, LocalDate.now().toString("yyyy-MM-dd"),cityCode,userId)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<HomeRecomBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            mView?.showBannerList(it.place1)
                        },
                        {
                            mView?.showMessage(it.message)
                        },
                        {
                            mView?.hideProgressDialog()
                        }
                )
    }

    override fun crowdQuery(isLoadMore:Boolean) {
        if(isLoadMore){
            page+=1
        }else{
            mView?.showProgressDialog("正在加载...")
        }
        ApiManager.crowdQuery(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<PageBean<ProdCrowdBean>>(ActivityEvent.DESTROY))
                .doOnError {
                    if(isLoadMore)page-=1
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it?.pageList == null || it?.pageList!!.isEmpty()){
                                mView?.loadMoreEnd()
                                return@subscribe
                            }
                            if(it?.pageList != null){
                                if(isLoadMore){
                                    crowdList?.addAll(it.pageList!!)
                                }else{
                                    crowdList = it.pageList as MutableList<ProdCrowdBean>?
                                }
                                mView?.notifyProdCrowdAdapterUpdate(this!!.crowdList)
                            }
                            mView?.hideProgressDialog()
                        },
                        {
                            if(isLoadMore)page-=1
                            mView?.showMessage(it.message)
                            mView?.hideProgressDialog()
                        },
                        {
                        }
                )

    }
}