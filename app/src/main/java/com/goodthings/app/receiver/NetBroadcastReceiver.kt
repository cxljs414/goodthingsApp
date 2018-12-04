package com.goodthings.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.greenrobot.eventbus.EventBus

/**
 * 功能：监听网络变化
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/27
 * 修改内容：
 * 最后修改时间：
 */
class NetBroadcastReceiver :BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        EventBus.getDefault().post("网络变化了")
    }

}