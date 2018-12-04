package com.goodthings.app.activity.groupbuydetail

import android.util.Log
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
 * 创建时间：2018/5/29
 * 修改内容：
 * 最后修改时间：
 */
class GroupBuyDetailPresenterImpl :
        BasePresenterImpl<GroupBuyDetailContract.GroupBuyDetailView>(),
        GroupBuyDetailContract.GroupBuyDetailPresenter{

    private var detailBean:GroupBuyDetailBean? = null
    override fun start(groupBuyId: Int) {
        mView?.showProgressDialog("正在加载数据...")
        ApiManager.getCollageDetail(groupBuyId)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<GroupBuyDetailBean>(ActivityEvent.DESTROY))
                .doOnError { mView?.hideProgressDialog() }
                .subscribe({
                    mView?.hideProgressDialog()
                    detailBean = it
                    mView?.showGroupDetailContent(it)
                    getCollectCount(it.cId)
                    getShareCount(it.cId)
                    getIsCollect(false)
                    collageQuery()
                    colTeamQuery()
                    getIsBuyed()
                    getPrdDetail(it.spu_id)
                    getUserComment()
                },{
                    mView?.hideProgressDialog()
                })
    }


    override fun getPrdDetail(spu_id: Int?) {
        ApiManager.getPrdDetail(spu_id!!)//121
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<List<ProdBuyDetailBean>>(ActivityEvent.DESTROY))
                .doOnError {
                    Log.i("getPrdDetail","异常："+it.message)
                }.subscribe({
                    //解析

                    it.forEach {
                        var subList:MutableList<ProdBuyDetailSubBean> = ArrayList()
                        if(it.detailStr.isNotEmpty()) {
                            var details = it.detailStr.split(",")
                            details.forEach {
                                var subDetail= it.split(";")
                                subList.add(ProdBuyDetailSubBean(subDetail[0].toInt(),
                                        subDetail[1].split("|")[1],
                                        subDetail[2],subDetail[3],subDetail[4]))
                            }
                        }
                        it.subDetailList = subList
                    }
                    mView?.showProdBuyDetail(it)
                })
    }

    override fun getIsBuyed() {
        if(!Consts.isLogined)return
        ApiManager.isBuy(Consts.user?.id!!,detailBean?.cId!!)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<Boolean>(ActivityEvent.DESTROY))
                .doOnError {
                }.subscribe({
                    mView?.updateIsBuy(it)
                })
    }

    override fun colTeamQuery(){
        ApiManager.colTeamQuery(detailBean?.cId!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<List<GroupingBean>>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.notifyTeamsListUpdate(null)
                }.subscribe({
                    mView?.notifyTeamsListUpdate(it)
                })
    }
    /**
     * 请求是否已经喜欢
     */
    override fun getIsCollect(needAdd:Boolean) {
        if(!Consts.isLogined)return
        mView?.changeCollect(true,needAdd)
        ApiManager.getallmsg(Consts.user?.id!!,detailBean?.cId!!,7,detailBean?.cId!!,detailBean?.mer_id!!)
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
        ApiManager.shareCount(4,pId)
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
        ApiManager.collectCount(7,pId)
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

    override fun addShare(id: Int) {
        if(Consts.isLogined) {
            ApiManager.share(Consts.user?.id!!, id,"4")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                    .subscribe(
                            {
                                getShareCount(id)
                                //mView?.shareCountChange()
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
        var flowable: Flowable<CommonResult>
        if(!collect){
            flowable = ApiManager.addCollect(Consts.user?.id!!,7,detailBean?.cId!!)
        }else{
            flowable = ApiManager.deloneCollect(Consts.user?.id!!,7,detailBean?.cId!!)
        }
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                .subscribe(
                        {
                            if(it.code == 2000){
                                mView?.changeCollect(!collect, true)
                            }else{
                                mView?.showMessage(it.msg)
                            }
                        },
                        {
                            mView?.showMessage(it.message)
                        }
                )
    }

    override fun collageQuery(){
        mView?.showProgressDialog("正在加载数据...")
        ApiManager.collageQuery(1 ,10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<PageBean<GroupListBean>>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }.subscribe({
                    mView?.hideProgressDialog()
                    if(it?.pageList == null || it?.pageList!!.isEmpty()){
                        return@subscribe
                    }
                    var list:MutableList<GroupListBean> = ArrayList()
                    it.pageList!!.forEach {
                        if(list.size < 6 && it.cId != detailBean?.cId){
                            list.add(it)
                        }
                    }
                    mView?.notifyGroupBuyListUpdate(list)
                },{
                    mView?.hideProgressDialog()
                })
    }

    override fun tjCollageQuery(){
        ApiManager.tjCollageQuery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<List<GroupListBean>>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }.subscribe({
                    mView?.hideProgressDialog()
                    if(it == null || it.isEmpty()){
                        return@subscribe
                    }
                    var list:MutableList<GroupListBean> = ArrayList()
                    it.forEach {
                        if(list.size < 6 && it.cId != detailBean?.cId){
                            list.add(it)
                        }
                    }
                    mView?.notifyGroupBuyListUpdate(list)
                },{
                    mView?.hideProgressDialog()
                })
    }

    override fun shareParam(cId: Int?) {
        mView?.showProgressDialog("正在请求数据...")
        ApiManager.shareParam(cId!!,Consts.user?.id!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<ShareParamBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }.subscribe({
                    mView?.hideProgressDialog()
                    mView?.notifyShare(it)
                },{
                    mView?.hideProgressDialog()
                })
    }

    /**
     * 获取评论
     */
    private fun getUserComment() {
        ApiManager.getHostComment(detailBean?.cId!!,2)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<List<CommentBean>>(ActivityEvent.DESTROY))
                .doOnError {  mView?.showComment(null) }
                .subscribe({
                    if(it.size>2){
                        mView?.showComment(it.subList(0,2))
                    }else{
                        mView?.showComment(it)
                    }

                },{
                    mView?.showComment(null)
                })
    }

    override fun addFabulous(mId: Int) {
        ApiManager.addFabulous(mId)
                .compose(RxUtil.hanlderCommResult())
                .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                .doOnError {

                }
                .subscribe({
                    if(it.code == 2000){
                        //mView?.showMessage("点赞成功")
                    }
                })
    }

}