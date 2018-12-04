package com.goodthings.app.base

import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/25
 * 修改内容：
 * 最后修改时间：
 */
interface BasePresenter<in V:BaseView>{
    /**
     * 绑定view
     */
    fun attachView(view:V)

    /**
     * 解除绑定
     */
    fun detachView()

    fun setLifeCycleProvider(v: LifecycleProvider<ActivityEvent>) {}
    fun setFragmentLifeCycleProvider(v: LifecycleProvider<FragmentEvent>)

    fun afterAttachView()
}