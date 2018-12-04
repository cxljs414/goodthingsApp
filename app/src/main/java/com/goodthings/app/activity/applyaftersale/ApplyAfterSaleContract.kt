package com.goodthings.app.activity.applyaftersale

import android.content.Intent
import android.net.Uri
import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.AfterSaleBean
import com.goodthings.app.bean.GroupOrderDetailBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/20
 * 修改内容：
 * 最后修改时间：
 */
interface ApplyAfterSaleContract {

    interface ApplyAfterSaleView : BaseView{
        fun getIntent(): Intent
        fun showGroupOrderContent(it: GroupOrderDetailBean?): Any
        fun addPic(data: String)
        fun commitSuccess()
        fun showApplyContent(it: AfterSaleBean?)

    }

    interface ApplyAfterSalePresenter : BasePresenter<ApplyAfterSaleView>{
        fun start()
        fun uploadImage(data: Uri?)
        fun commitApply(type: Int, status: Int, cause: String, explain: String, phone: String, picsList: MutableList<String>)

    }

    interface OnAddPicListener{
        fun onAddpic(item: Int)
    }

    interface OnSeePicListener{
        fun onSeePic(item: String?)
    }

}