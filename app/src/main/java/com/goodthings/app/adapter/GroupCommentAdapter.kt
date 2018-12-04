package com.goodthings.app.adapter

import android.widget.RadioButton
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.activity.comments.AllCommentsContract
import com.goodthings.app.application.Consts
import com.goodthings.app.bean.CommentBean
import java.text.SimpleDateFormat
import java.util.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/18
 * 修改内容：
 * 最后修改时间：
 */
class GroupCommentAdapter(data:List<CommentBean>) :
        BaseQuickAdapter<CommentBean,BaseViewHolder>(R.layout.item_groupbuy_comment,data) {

    var listener:AllCommentsContract.OnCommentsCheckedListener?=null

    override fun convert(helper: BaseViewHolder?, item: CommentBean?) {

        Glide.with(mContext).load(Consts.IMAGEURL+item?.head_url).into(helper?.getView(R.id.item_groupcomment_image))
        helper?.setText(R.id.item_groupcomment_nickname,item?.nickname)
        helper?.setText(R.id.item_groupcomment_zan_rb,"${item?.fabulous}")
        item?.create_time.let {
            helper?.setText(R.id.item_groupcomment_date,SimpleDateFormat("yyyy-MM-dd hh:mm").format(Date(item?.create_time!!.toLong())))
        }
        helper?.setText(R.id.item_groupcomment_content,item?.content)

        if(item?.hf.isNullOrEmpty()){
            helper?.setVisible(R.id.item_groupcomment_answer,false)
        }else{
            helper?.setVisible(R.id.item_groupcomment_answer,true)
            item?.hf.let {
                helper?.setText(R.id.item_groupcomment_answer,"回复：${item?.hf}")
            }
        }


        var zanRB = helper?.getView<RadioButton>(R.id.item_groupcomment_zan_rb)
        zanRB?.setOnCheckedChangeListener({ _, _ ->
            listener?.onCommentsChecked(item?.mId!!)
            zanRB.text = "${(Integer.parseInt(zanRB.text.toString())+1)}"
        })
    }

}