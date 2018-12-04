package com.goodthings.app.activity.crowddetail

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
 * 创建时间：2018/4/25
 * 修改内容：
 * 最后修改时间：
 */
class ProdCrowdDetailPresenterImpl :
        BasePresenterImpl<ProdCrowdDetailContract.ProdCrowdDetailView>(),
        ProdCrowdDetailContract.ProdCrowdDetailPresenter{
    private var prodCrowdBean:ProdCrowdBean? = null
    override fun startLoadData(prodId: Int) {
        queryCrowdById(prodId)
        crowdQuery(prodId)
    }

    fun queryCrowdById(prodId: Int) {
        mView?.showProgressDialog("正在加载...")
        ApiManager.crowdQueryById(prodId)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<ProdCrowdBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null){
                                prodCrowdBean = it
                                mView?.showCrowdContent(it)
                                querySkillUser()
                                getCollectCount(it.pId)
                                getShareCount(it.pId)
                                if(Consts.isLogined){
                                    payQueryByid()
                                    getIsCollect(false)
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

    override fun payQueryByid(){
        //如果限购是0 则表示不限购
        if(prodCrowdBean?.status !=0 || !Consts.isLogined)return
        ApiManager.payQueryByid(Consts.user?.id!!,prodCrowdBean?.pId!!)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<Int>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            //mView?.showMessage("已经购买过"+it+"次")
                            //是否购买过，
                            //是：判断is_more_buy
                            mView?.showCanBuy(prodCrowdBean?.pId!!,prodCrowdBean?.is_more_buy!!,it)
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

    /**
     * 请求是否已经喜欢
     */
    override fun getIsCollect(needAdd:Boolean) {
        ApiManager.getallmsg(Consts.user?.id!!,prodCrowdBean?.pId!!,6,prodCrowdBean?.pId!!,prodCrowdBean?.mer_id!!)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<FollowBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null){
                                if(it.code_collect == "2000"){
                                    mView?.changeCollect(true,needAdd)
                                }else{
                                    mView?.changeCollect(false,needAdd)
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

    private fun getShareCount(pId: Int) {
        ApiManager.shareCount(3,pId)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<Int>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null){
                                mView?.showShareCount(it)
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

    private fun getCollectCount(pId: Int) {
        ApiManager.collectCount(6,pId)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<Int>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null){
                                mView?.showCollectCount(it)
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

    override fun querySkillUser(){
        var userId= 0
        if(Consts.isLogined){
            userId = Consts.user?.id!!
        }
        ApiManager.querySkillUser(userId,prodCrowdBean?.mer_id!!)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<ScrowdUserBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it != null){
                                mView?.showCrowdUserInfo(it)
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

    override fun addShare(id: Int) {
        if(Consts.isLogined) {
            ApiManager.share(Consts.user?.id!!, id,"2")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                    .subscribe(
                            {
                                mView?.shareCountChange()
                            },
                            {
                            }
                    )
        }
    }

    override fun requestChangeCollect(collect: Boolean) {
        if(!Consts.isLogined){
            mView?.goLogin()
            return
        }
        var flowable:Flowable<CommonResult>
        if(!collect){
            flowable = ApiManager.addCollect(Consts.user?.id!!,6,prodCrowdBean?.pId!!)
        }else{
            flowable = ApiManager.deloneCollect(Consts.user?.id!!,6,prodCrowdBean?.pId!!)
        }
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                .subscribe(
                        {
                            if(it.code == 2000){
                                mView?.changeCollect(!collect, true)
                            }
                        },
                        {
                            mView?.showMessage(it.message)
                        }
                )
    }

    override fun requestChangeUserFollow(isUserFollow: Boolean) {
        if(!Consts.isLogined){
            mView?.goLogin()
            return
        }
        var flowable:Flowable<CommonResult>
        if(!isUserFollow){
            flowable = ApiManager.addfollow(Consts.user?.id!!,prodCrowdBean?.mer_id!!)
        }else{
            flowable = ApiManager.delonefollow(Consts.user?.id!!,prodCrowdBean?.mer_id!!)
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

    private fun crowdQuery(curProdId: Int) {
        ApiManager.crowdQuery(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<PageBean<ProdCrowdBean>>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it?.pageList != null){
                                var list:MutableList<ProdCrowdBean> = ArrayList()
                                it?.pageList!!.forEach {
                                    if(it.pId != curProdId
                                            && it.status == 0
                                            && list.size<4
                                            ){
                                        list.add(it)
                                    }
                                }
                                mView?.notifyRecomListUpdate(list)
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

    override fun cfDailyGoldEnvelopes() {
        if(Consts.isLogined) {
            ApiManager.cfDailyGoldEnvelopes(Consts.user?.id!!,prodCrowdBean?.pId!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycle?.bindUntilEvent<BaseResult<Int>>(ActivityEvent.DESTROY))
                    .doOnError {
                        mView?.showMessage(it.message)
                        mView?.hideProgressDialog()
                    }
                    .subscribe(
                            {
                                mView?.hideProgressDialog()
                                if(it.code == 2000) {
                                    mView?.showReadGold(it.data)
                                }
                                addBrowseLog()
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

    fun addBrowseLog(){
        ApiManager.addBrowseLog(Consts.user?.id!!,prodCrowdBean?.pId!!)
                .onErrorResumeNext(RxUtil.ErrorHandlerFun())
                .subscribeOn(Schedulers.io())
                .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                        },
                        {
                        },
                        {
                        }
                )
    }

}