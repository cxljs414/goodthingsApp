package com.goodthings.app.activity.webfrag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goodthings.app.R
import com.goodthings.app.base.BaseFragment

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/8
 * 修改内容：
 * 最后修改时间：
 */
class WebFragment : BaseFragment<WebContract.WebFragView,WebContract.WebFragPresenter>(),WebContract.WebFragView {
    override var presenter: WebContract.WebFragPresenter = WebFragPresenterImpl()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_webfrag,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}