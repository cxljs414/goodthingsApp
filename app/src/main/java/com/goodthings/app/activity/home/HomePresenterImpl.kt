package com.goodthings.app.activity.home

import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.HomeClassBean
import com.goodthings.app.bean.HomeGussLikeBean
import com.goodthings.app.bean.HomeNewsBean
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.goodthings.app.util.SPUtil
import com.trello.rxlifecycle2.android.FragmentEvent

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/2
 * 修改内容：
 * 最后修改时间：
 */
class HomePresenterImpl : BasePresenterImpl<HomeContract.HomeView>(),HomeContract.HomePresenter {
    override fun initData() {
        initClasses()
        initBuiness()
        initLive()
        initLike()
        initNews()
        requestRecContentList()
    }

    private fun requestRecContentList() {
        /*mView?.showProgressDialog("正在请求数据...")
        var cityCode = SPUtil.getStringValue(mView?.getContext()!!,"cityCode","110000")
        ApiManager.getRecContentList(1,LocalDate.now().toString("yyyy-MM-dd"),cityCode)
                .compose(RxUtil.hanlderBaseResult())
                .compose(fragmentLifecycle?.bindUntilEvent<HomeRecomBean>(FragmentEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            mView?.showBanner(it.place1)
                            mView?.notifyBusinessRvUpdate(it.place5)
                            mView?.notifyFavoriteRvUpdate(it.place7)
                            mView?.notifyUseRvUpdate(it.place8)
                            mView?.notifyRecomedUpdate(it.place4[0])
                        },
                        {
                            mView?.showMessage(it.message)
                        },
                        {
                            mView?.hideProgressDialog()
                        }
                )*/
    }

    private fun initNews() {
        ApiManager.getNewsList()
                .compose(RxUtil.hanlderBaseResult())
                .compose(fragmentLifecycle?.bindUntilEvent<List<HomeNewsBean>>(FragmentEvent.DESTROY))
                .subscribe(
                        {
                            mView?.notifyNewsUpdate(it)
                        },
                        {
                            mView?.showMessage(it.message)
                        },
                        {
                        }
                )
    }

    private fun initClasses() {
        var classList:MutableList<HomeClassBean> = ArrayList()
        mView?.notifyClassRvUpdate(classList)
    }

    private fun initBuiness() {
        var busList:MutableList<Int> = ArrayList()
        //mView?.notifyBusinessRvUpdate(busList)
    }

    private fun initLive(){
        var liveList:MutableList<String> = ArrayList()
        liveList.add("玩就玩些刺激的")
        liveList.add("玩就玩些刺激的")
        liveList.add("玩就玩些刺激的")
        liveList.add("玩就玩些刺激的")
        mView?.notifyLiveRvUpdate(liveList)
    }

    private fun initLike() {
        var user = SPUtil.getUserBean(mView?.getContext()!!)
        //检查是否登录
        var isLogined = (user != null)
        var userId= -1
        if(user != null)userId = user.id
        ApiManager.guessYouLike(isLogined,userId)
                .compose(RxUtil.hanlderBaseResult())
                .compose(fragmentLifecycle?.bindUntilEvent<List<HomeGussLikeBean>>(FragmentEvent.DESTROY))
                .subscribe(
                        {
                            mView?.notifyLikeRvUpdate(it)
                        },
                        {
                            mView?.showMessage(it.message)
                        },
                        {
                        }
                )
    }

}