package com.goodthings.app.activity.comments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.goodthings.app.R
import com.goodthings.app.adapter.GroupCommentAdapter
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.AllCommentsBean
import com.goodthings.app.bean.CommentBean
import com.goodthings.app.util.NoScrollLinearLayoutManager
import com.goodthings.app.util.ScreenUtil
import com.goodthings.app.util.SpacesItemDecorationtwo
import com.goodthings.app.util.onClick
import kotlinx.android.synthetic.main.activity_all_comments.*
import kotlinx.android.synthetic.main.top_bar.*

class AllCommentsActivity :
        BaseActivity<AllCommentsContract.AllCommentsView,AllCommentsContract.AllCommentsPresenter>(),
        AllCommentsContract.AllCommentsView, BaseQuickAdapter.RequestLoadMoreListener {
    override var presenter: AllCommentsContract.AllCommentsPresenter=AllCommentsPresenterImpl()
    private var goodAdapter: GroupCommentAdapter? = null
    private var newAdapter: GroupCommentAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_comments)
        toptitle.text = "全部评论"
        topback.onClick { onBackPressed() }

        initRecyclercview()
        presenter.start()
    }

    private fun initRecyclercview() {
        allcomments_good_recyclerview.layoutManager = NoScrollLinearLayoutManager(this)
        allcomments_good_recyclerview.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this,1f)))
        goodAdapter = GroupCommentAdapter(ArrayList())
        allcomments_good_recyclerview.adapter = goodAdapter
        goodAdapter?.setOnItemChildClickListener { adapter, view, position ->

        }

        goodAdapter?.listener = object:AllCommentsContract.OnCommentsCheckedListener{
            override fun onCommentsChecked(mId: Int) {
                presenter.addFabulous(mId)
            }

        }

        allcomments_new_recyclerview.layoutManager = LinearLayoutManager(this)
        allcomments_new_recyclerview.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this,1f)))
        newAdapter = GroupCommentAdapter(ArrayList())
        allcomments_new_recyclerview.adapter = newAdapter
        newAdapter?.setOnItemChildClickListener { adapter, view, position ->

        }

        newAdapter?.setEnableLoadMore(true)
        newAdapter?.setOnLoadMoreListener(this)
    }

    override fun showAllComments(it: AllCommentsBean?) {
        it.let {
            goodAdapter?.setNewData(it?.goodComments)
            goodAdapter?.notifyDataSetChanged()

            newAdapter?.setNewData(it?.newComments)
            newAdapter?.notifyDataSetChanged()
        }
    }

    override fun showComment(pageList: List<CommentBean>?) {
        newAdapter?.setNewData(pageList)
        newAdapter?.notifyDataSetChanged()
        newAdapter?.loadMoreComplete()
    }

    override fun onLoadMoreRequested() {
        presenter.loadMore()
    }

    override fun noMore() {
        newAdapter?.loadMoreComplete()
        newAdapter?.loadMoreEnd()
    }

}
