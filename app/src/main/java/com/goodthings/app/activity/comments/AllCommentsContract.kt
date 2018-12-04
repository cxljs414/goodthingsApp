package com.goodthings.app.activity.comments

import android.content.Intent
import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.AllCommentsBean
import com.goodthings.app.bean.CommentBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/18
 * 修改内容：
 * 最后修改时间：
 */
interface AllCommentsContract {

    interface AllCommentsView : BaseView{
        fun showAllComments(it: AllCommentsBean?)
        fun showComment(pageList: List<CommentBean>?)
        fun noMore()
        fun getIntent(): Intent

    }

    interface AllCommentsPresenter:BasePresenter<AllCommentsView>{
        fun start()
        fun loadMore()
        fun addFabulous(mId: Int)

    }

    interface OnCommentsCheckedListener{
        fun onCommentsChecked(id:Int)
    }
}