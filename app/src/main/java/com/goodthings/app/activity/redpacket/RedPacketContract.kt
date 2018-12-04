package com.goodthings.app.activity.redpacket

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/4
 * 修改内容：
 * 最后修改时间：
 */
interface RedPacketContract {

    interface RedPacketView : BaseView{
        fun showHongBao(b: Boolean, msg: String)

    }

    interface RedPacketPresenter : BasePresenter<RedPacketView>{
        fun dailyRedEnvelopes()

    }
}