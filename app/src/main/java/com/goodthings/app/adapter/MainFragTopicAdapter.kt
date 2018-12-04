package com.goodthings.app.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.bean.HomeRecomSubBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/27
 * 修改内容：
 * 最后修改时间：
 */
class MainFragTopicAdapter(data:MutableList<HomeRecomSubBean>) :
        BaseQuickAdapter<HomeRecomSubBean,BaseViewHolder>(R.layout.item_mainfrag_topic_item,data) {
    override fun convert(helper: BaseViewHolder?, item: HomeRecomSubBean?) {
        Glide.with(mContext).load(Consts.IMAGEURL+item?.coverPic).into(helper?.getView(R.id.item_mainfrag_topic_item_image))
        helper?.setText(R.id.item_mainfrag_topic_item_title,item?.title)
        helper?.addOnClickListener(R.id.item_mainfrag_topic_item_root)
    }

    override fun getItemCount(): Int {
        return 2
    }
}