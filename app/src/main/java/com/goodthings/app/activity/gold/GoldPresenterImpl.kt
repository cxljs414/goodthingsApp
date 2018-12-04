package com.goodthings.app.activity.gold

import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.GoldBean
import com.goodthings.app.bean.PageBean
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.SPUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/3
 * 修改内容：
 * 最后修改时间：
 */
class GoldPresenterImpl : BasePresenterImpl<GoldContract.GoldView>(),GoldContract.GoldPresenter {

    private var pageGold= 1
    private var pageRecord= 1
    private var curSelectedType= 0 // 0金币 1 奖励记录
    private var goldList:MutableList<GoldBean> = ArrayList()
    private var recordList:MutableList<GoldBean> = ArrayList()
    private var userId = 0

    override fun start() {
        var user = SPUtil.getUserBean(mView?.getContext()!!)
        if(user != null) {
            userId = user.id
            //userId = 289
            /*queryCoinRecord(user.id,1)
            queryCoinSeleRecord(user.id,1)*/
            queryCoinRecord(false)
            queryCoinSeleRecord(false)
        }
    }

    private fun queryCoinRecord(isMore:Boolean){
        mView?.showProgressDialog("正在请求数据...")
        ApiManager.queryCoinRecord(userId,pageGold)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<PageBean<GoldBean>>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                    if(isMore)pageGold-=1
                }
                .subscribe(
                        {
                            if (it?.pageList != null){
                                if(!isMore) {
                                    goldList.clear()
                                }
                                if(it.pageList!!.isEmpty()){
                                    mView?.noMore(curSelectedType)
                                }else{
                                    goldList.addAll(it.pageList!!)
                                    mView?.notifyGoldDataChange(goldList)
                                }

                            }
                            mView?.hideProgressDialog()
                        },
                        {
                            if(isMore)pageGold-=1
                            mView?.showMessage(it.message)
                            mView?.hideProgressDialog()
                        },
                        {
                            mView?.hideProgressDialog()
                        }
                )
    }

    fun queryCoinSeleRecord(isMore:Boolean){
        ApiManager.queryCoinSeleRecord(userId,pageRecord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<PageBean<GoldBean>>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                    if(isMore)pageRecord-=1
                }
                .subscribe(
                        {
                            if(it?.pageList != null){
                                if (!isMore)recordList.clear()
                                if(it.pageList!!.isEmpty()){
                                    mView?.noMore(curSelectedType)
                                }else{
                                    recordList.addAll(it.pageList!!)
                                    mView?.notifyRecordDataChange(recordList)
                                }
                            }
                        },
                        {
                            if(isMore)pageRecord-=1
                            mView?.showMessage(it.message)
                        },
                        {
                        }
                )
    }

    override fun setCurSelectedType(type: Int) {
        curSelectedType = type
    }
    override fun loadMore() {
        if(curSelectedType == 0){
            pageGold+=1
            queryCoinRecord(true)
        }else{
            pageRecord +=1
            queryCoinSeleRecord(true)
        }
    }
}