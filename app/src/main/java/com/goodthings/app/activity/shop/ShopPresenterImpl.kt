package com.goodthings.app.activity.shop

import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.*
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
 * 创建时间：2018/5/31
 * 修改内容：
 * 最后修改时间：
 */
class ShopPresenterImpl :
        BasePresenterImpl<ShopContract.ShopView>(),
        ShopContract.ShopPresenter{

    private var merId = -1
    private var shopGroupBean:ShopGroupBean? = null
    private var shopCrowdBean:ShopCrowdBean? = null
    private var shopSpuBean:ShopSpuBean? = null
    override fun start() {
        merId = mView?.getIntent()!!.getIntExtra("merId",-1)
        querySkillUser()
        queryBusUserCount()
        var type= mView?.getIntent()!!.getIntExtra("type",1)
        changeTab(type)
    }

    override fun changeTab(i: Int) {
        when(i){
            1->{
                if(shopGroupBean != null){
                    mView?.showShopGroups(shopGroupBean)
                }else{
                    queryGroups()
                }
            }
            2->{
                if(shopCrowdBean != null){
                    mView?.showShopCrowds(shopCrowdBean)
                }else{
                    queryCrowds()
                }
            }
            3->{
                if(shopSpuBean != null){
                    mView?.showShopSpus(shopSpuBean)
                }else{
                    queryBusUserSpu()
                }
            }
        }
    }

    fun queryGroups(){
        mView?.showProgressDialog("正在加载数据..")
        ApiManager.queryBusUserCollage(merId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<ShopGroupBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null ){
                                if(it.status == "200"){
                                    shopGroupBean = it
                                    mView?.showShopGroups(it)
                                }else{
                                    mView?.showMessage(it.msg)
                                }
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
    fun queryCrowds(){
        mView?.showProgressDialog("正在加载数据..")
        ApiManager.queryBusUserCrowd(merId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<ShopCrowdBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null ){
                                if(it.status == "200"){
                                    shopCrowdBean = it
                                    mView?.showShopCrowds(it)
                                }else{
                                    mView?.showMessage(it.msg)
                                }
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
    fun queryBusUserSpu(){
        mView?.showProgressDialog("正在加载数据..")
        ApiManager.queryBusUserSpu(merId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<ShopSpuBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null ){
                                if(it.status == "200"){
                                    shopSpuBean = it
                                    mView?.showShopSpus(it)
                                }else{
                                    mView?.showMessage(it.msg)
                                }
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
    fun queryBusUserCount(){
        ApiManager.queryBusUserCount(merId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<ShopCountBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null ){
                                if(it.status == "200"){
                                    mView?.showShopCounts(it)
                                }else{
                                    mView?.showMessage(it.msg)
                                }
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
    override fun querySkillUser() {
        var userId= 0
        if(Consts.isLogined){
            userId = Consts.user?.id!!
        }
        ApiManager.querySkillUser(userId,merId)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<ScrowdUserBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null){
                                mView?.showUserInfo(it)
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
    override fun changeUserFollow(isUserFollow: Boolean) {
        if(!Consts.isLogined){
            mView?.goLogin()
            return
        }
        var flowable: Flowable<CommonResult>
        if(!isUserFollow){
            flowable = ApiManager.addfollow(Consts.user?.id!!,merId)
        }else{
            flowable = ApiManager.delonefollow(Consts.user?.id!!,merId)
        }
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                .subscribe(
                        {
                            if(it.code == 2000){
                                //重新请求粉丝数
                                querySkillUser()
                                mView?.changeUserFollow(!isUserFollow)
                            }
                            mView?.showMessage(it.msg)
                        },
                        {
                            mView?.showMessage(it.message)
                        }
                )
    }
}