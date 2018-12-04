package com.goodthings.app.activity.topnews

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goodthings.app.R
import com.goodthings.app.activity.main.MainActivity
import com.goodthings.app.activity.main.mainfrag.MainFragContract
import com.goodthings.app.adapter.MainFragProdListAdapter
import com.goodthings.app.base.BaseFragment
import com.goodthings.app.bean.MainFragProdBean
import com.goodthings.app.util.ScreenUtil
import com.goodthings.app.util.SpacesItemDecorationtwo
import kotlinx.android.synthetic.main.fragment_topnews.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/11
 * 修改内容：
 * 最后修改时间：
 */
class TopNewsFragment : BaseFragment<TopNewsContract.TopNewsView, TopNewsContract.TopNewsPresenter>(), TopNewsContract.TopNewsView{

    override var presenter: TopNewsContract.TopNewsPresenter= TopNewsPresenterImpl()
    var topNewAdapter: MainFragProdListAdapter? = null
    private var mainActivity: MainActivity? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_topnews,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity?
        topnews_recyclerview.layoutManager = LinearLayoutManager(context)
        topnews_recyclerview.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(context!!,10f)))
        topNewAdapter = MainFragProdListAdapter(ArrayList())
        topNewAdapter?.setProdClickListener(object: MainFragContract.OnProdClickListener{
            override fun onProkClick(isAdver: Boolean, prodBean: MainFragProdBean) {
                if(isAdver){
                    mainActivity?.goNext("adver", "${prodBean.linkUrl}")
                }
            }
        })
        topnews_recyclerview.adapter = topNewAdapter
        reLoadData()
    }

    override fun notifyTopNewsUpdate(it: List<MainFragProdBean>?) {
        topNewAdapter?.setNewData(it)
        topNewAdapter?.notifyDataSetChanged()
    }

    fun reLoadData() {
        presenter.getTopNews()
    }
}