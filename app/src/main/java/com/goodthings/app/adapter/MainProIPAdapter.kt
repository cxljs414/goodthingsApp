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
 * 创建时间：2018/5/3
 * 修改内容：
 * 最后修改时间：
 */
class MainProIPAdapter(data:List<HomeRecomSubBean>):
    BaseQuickAdapter<HomeRecomSubBean,BaseViewHolder>(R.layout.item_mainfrag_proip,data){
    override fun convert(helper: BaseViewHolder?, item: HomeRecomSubBean?) {
        Glide.with(mContext).load(Consts.IMAGEURL+item?.coverPic).into(helper?.getView(R.id.item_mainfrag_proip))
        helper?.addOnClickListener(R.id.item_mainfrag_proip)
    }
}