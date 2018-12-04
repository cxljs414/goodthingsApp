package com.goodthings.app.base

import android.app.Activity
import android.content.Context
import android.support.annotation.StringRes

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/25
 * 修改内容：
 * 最后修改时间：
 */
interface BaseView {

    fun getActivity():Activity?
    /**
     * 获取上下文
     */
    fun getContext():Context?

    /**
     * 显示错误信息
     */
    fun showError(error:String?)

    /**
     * 显示错误信息
     */
    fun showError(@StringRes errorId:Int)

    /**
     * 显示信息
     */
    fun showMessage(msg:String?)

    /**
     * 显示信息，通过资源id
     */
    fun showMessage(@StringRes msgId:Int)

    fun onNetwokChange(isEnable:Boolean)
    fun wxPayResult(content: Int)

    /**
     * 显示进度框
     */
    fun showProgressDialog(content:String)
    fun showProgressDialog(content:Int)
    /**
     * 隐藏进度框
     */
    fun hideProgressDialog()

    fun shareResult(isSuccess:Boolean,msg:String)
}