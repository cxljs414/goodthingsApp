package com.goodthings.app.activity.addComment

import android.content.Intent
import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.GroupOrderDetailBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/18
 * 修改内容：
 * 最后修改时间：
 */
interface AddCommentContract {

    interface AddCommentView : BaseView{
        fun getIntent(): Intent
        fun showGroupOrderContent(it: GroupOrderDetailBean?): Any
        fun commitSuccess()

    }

    interface AddCommentPresenter : BasePresenter<AddCommentView>{
        fun start()
        fun setCommentContent(it: String)
        fun commit()

    }

}