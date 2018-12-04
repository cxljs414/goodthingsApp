package com.goodthings.app.activity.comments

import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.AllCommentsBean
import com.goodthings.app.bean.CommentBean
import com.goodthings.app.bean.CommonResult
import com.goodthings.app.bean.PageBean
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/18
 * 修改内容：
 * 最后修改时间：
 */
class AllCommentsPresenterImpl :
        BasePresenterImpl<AllCommentsContract.AllCommentsView>(),AllCommentsContract.AllCommentsPresenter{

    private var id= -1
    private var page = 1
    private var newCommentsList:MutableList<CommentBean> = ArrayList()

    override fun afterAttachView() {
        super.afterAttachView()
        id = mView?.getIntent()!!.getIntExtra("id",-1)
    }
    override fun start() {
        loadDatas()
    }

    private fun loadDatas() {
        mView?.showProgressDialog("正在获取数据..")
        var flowable1= ApiManager.getHostComment(id,2).compose(RxUtil.hanlderBaseResult())
        var flowable2= ApiManager.getUserComment(1,10,id,2,0)
        Flowable.zip(flowable1, flowable2, BiFunction<List<CommentBean>, PageBean<CommentBean>, AllCommentsBean> {
            list1, pagebean -> AllCommentsBean(list1,pagebean.pageList!!)
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<AllCommentsBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                    mView?.showAllComments(null)
                }
                .subscribe({
                    mView?.showAllComments(it)
                    newCommentsList.addAll(it.newComments)
                },{
                    mView?.showAllComments(null)
                },{
                    mView?.hideProgressDialog()
                })
    }

    override fun loadMore() {
        mView?.showProgressDialog("正在获取数据..")
        ApiManager.getUserComment(++page,10,id,2,0)//detailBean?.cId!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<PageBean<CommentBean>>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                    mView?.showComment(null)
                    page--
                }
                .subscribe({
                    if(it?.pageList == null || it?.pageList!!.isEmpty()){
                        mView?.noMore()
                        return@subscribe
                    }
                    it?.pageList.let {
                        newCommentsList.addAll(it!!)
                    }
                    mView?.showComment(newCommentsList)
                },{
                    mView?.showComment(null)
                    page--
                },{
                    mView?.hideProgressDialog()
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
                       // mView?.showMessage("点赞成功")
                    }
                })
    }
}