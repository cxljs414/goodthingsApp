package com.goodthings.app.activity.addComment

import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.CommonResult
import com.goodthings.app.bean.GroupOrderDetailBean
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/18
 * 修改内容：
 * 最后修改时间：
 */
class AddCommentPresenterImpl :
        BasePresenterImpl<AddCommentContract.AddCommentView>(),
        AddCommentContract.AddCommentPresenter{

    var orderNo:String = ""
    var orderBean:GroupOrderDetailBean? = null
    var comment:String = ""
    override fun start() {
        orderNo = mView?.getIntent()!!.getStringExtra("orderNo")
        getOrderByOrderNo()
    }

    fun getOrderByOrderNo() {
        ApiManager.getCollageOrderDetailByOrderNo(orderNo)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<GroupOrderDetailBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.showMessage(it.message)
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            it.let {
                                orderBean = it
                                mView?.showGroupOrderContent(it)
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

    override fun setCommentContent(it: String) {
        comment = it
    }

    override fun commit() {
        if(comment.isEmpty()){
            mView?.showMessage("请填写评论内容")
            return
        }
        mView?.showProgressDialog("正在提交评论..")
        ApiManager.addUserComment(2,orderBean?.pid!!,comment,Consts.user?.id!!,orderNo)
                .compose(RxUtil.hanlderCommResult())
                .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }
                .subscribe({
                    if(it.code == 2000){
                        mView?.commitSuccess()
                    }
                },{

                },{ mView?.hideProgressDialog()})

    }

}