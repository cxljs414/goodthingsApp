package com.goodthings.app.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.bean.HomeGussLikeBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/5
 * 修改内容：
 * 最后修改时间：
 */
class HomeLikeAdapter(layoutResId: Int, data: MutableList<HomeGussLikeBean>?) : BaseQuickAdapter<HomeGussLikeBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: HomeGussLikeBean?) {
        Glide.with(mContext).load(Consts.IMAGEURL+item?.cover_url).into(helper?.getView(R.id.home_like_image))
        helper?.setText(R.id.home_like_title,item?.prd_title)
        Glide.with(mContext).load(Consts.IMAGEURL+item?.head_url).into(helper?.getView(R.id.home_like_headview))
        helper?.setText(R.id.home_like_nickname,item?.nickname)
        helper?.setText(R.id.home_like_likecount,"${item?.collectCount}")
        helper?.addOnClickListener(R.id.home_like_root)

    }
}