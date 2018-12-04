package com.goodthings.app.activity.shopmall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goodthings.app.R
import com.goodthings.app.base.BaseFragment
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener
import kotlinx.android.synthetic.main.fragment_shopmall.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/7
 * 修改内容：
 * 最后修改时间：
 */
class ShopMallFragment : BaseFragment<ShopMallContract.ShopMallView,ShopMallContract.ShopMallPresenter>(),ShopMallContract.ShopMallView {
    override var presenter: ShopMallContract.ShopMallPresenter = ShopMallPresenterImpl()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shopmall,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shopmall_pulltorefreshlayout.setCanRefresh(false)
        shopmall_pulltorefreshlayout.setRefreshListener(object:BaseRefreshListener{
            override fun loadMore() {
                shopmall_pulltorefreshlayout.finishLoadMore()
            }

            override fun refresh() {
            }

        })

        initClasses()
    }

    private fun initClasses() {
    }

}