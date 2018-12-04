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
abstract class BasePresenterImpl<V:BaseView>:BasePresenter<V> {
    protected var lifecycle:LifecycleProvider<ActivityEvent>? = null
    protected var fragmentLifecycle:LifecycleProvider<FragmentEvent>? = null
    protected var mView:V? = null
    override fun attachView(view: V) {
        mView = view
        afterAttachView()
    }

    override fun detachView() {
        mView = null
    }

    override fun setLifeCycleProvider(v: LifecycleProvider<ActivityEvent>) {
        super.setLifeCycleProvider(v)
        lifecycle = v
    }

    override fun setFragmentLifeCycleProvider(v: LifecycleProvider<FragmentEvent>) {
        fragmentLifecycle  = v
    }

    override fun afterAttachView(){

    }
}