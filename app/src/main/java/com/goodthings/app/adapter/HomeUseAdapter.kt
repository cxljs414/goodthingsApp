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
 * 创建时间：2018/3/5
 * 修改内容：
 * 最后修改时间：
 */
class HomeUseAdapter(layoutResId: Int, data: MutableList<HomeRecomSubBean>?) : BaseQuickAdapter<HomeRecomSubBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: HomeRecomSubBean?) {
        Glide.with(mContext).load(Consts.IMAGEURL+item?.coverPic).into(helper?.getView(R.id.item_home_use_image))
        helper?.setText(R.id.item_home_use_title,item?.title)
        helper?.setText(R.id.home_use_likecount,"${item?.collectNum}")
        Glide.with(mContext).load(Consts.IMAGEURL+item?.headUrl).into(helper?.getView(R.id.home_use_headview))
        helper?.setText(R.id.home_use_nickname,item?.nickName)
        helper?.addOnClickListener(R.id.item_home_use_root)
    }
}