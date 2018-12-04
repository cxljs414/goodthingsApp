package com.goodthings.app.activity.addComment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.goodthings.app.R
import com.goodthings.app.activity.login.LoginActivity
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.GroupOrderDetailBean
import com.goodthings.app.util.onClick
import com.goodthings.app.util.textChange
import kotlinx.android.synthetic.main.activity_add_comment.*
import kotlinx.android.synthetic.main.top_bar.*

class AddCommentActivity :
        BaseActivity<AddCommentContract.AddCommentView,AddCommentContract.AddCommentPresenter>(),
        AddCommentContract.AddCommentView{

    override var presenter: AddCommentContract.AddCommentPresenter = AddCommentPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)
        toptitle.text = "评价"
        topback.onClick { onBackPressed() }
        presenter.start()
        addcomment_commit.onClick {
            if(Consts.isLogined){
                presenter.commit()
            }else{
                startActivity(Intent(this@AddCommentActivity,LoginActivity::class.java))
            }
        }
        addcomment_content.textChange {
            presenter.setCommentContent(it)
        }
    }

    override fun showGroupOrderContent(it: GroupOrderDetailBean?) {
        it.let {
            Glide.with(this@AddCommentActivity)
                    .load(Consts.IMAGEURL+it?.cover_url)
                    .into(addcomment_image)
            addcomment_title.text = it?.title
            addcomment_count.text = "商品规格 x${it?.buy_num}"
            addcomment_price.text = "数量: ${it?.buy_num}  总计：￥${it?.price!!*it?.buy_num!!}"
            addcomment_oneprice.text = "￥${it?.price}"
        }
    }

    override fun commitSuccess() {
        intent.putExtra("newStatus",6)
        setResult(Activity.RESULT_OK,intent)
        finish()
    }
}
