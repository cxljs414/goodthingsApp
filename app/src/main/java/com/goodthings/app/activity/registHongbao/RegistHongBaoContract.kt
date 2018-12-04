package com.goodthings.app.activity.registHongbao

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/8
 * 修改内容：
 * 最后修改时间：
 */
interface RegistHongBaoContract {

    interface RegistHongBaoView:BaseView{
        fun shareGoldSuccess()

    }

    interface RegistHongBaoPresenter : BasePresenter<RegistHongBaoView>{
        fun registShareGoldEnvelopes()

    }
}