package com.goodthings.app.adapter

import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.bean.GroupListBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/31
 * 修改内容：
 * 最后修改时间：
 */
class GroupBuyDetailRecomAdapter(data:List<GroupListBean>,var imageWidth:Int):
BaseQuickAdapter<GroupListBean,BaseViewHolder>(R.layout.item_groupbuy_detail_recom,data){
    override fun convert(helper: BaseViewHolder?, item: GroupListBean?) {
        var imageView = helper?.getView<ImageView>(R.id.item_groupdetailrecom_image)
        var imageheight= imageWidth*10/16
        imageView?.layoutParams = LinearLayout.LayoutParams(imageWidth,imageheight)
        imageView?.scaleType = ImageView.ScaleType.FIT_XY
        Glide.with(mContext).load(Consts.IMAGEURL+item?.cover_url).into(imageView)
        helper?.setText(R.id.item_groupdetailrecom_title,item?.title)
        helper?.setText(R.id.item_groupdetailrecom_price,"￥${item?.price}")
        helper?.setText(R.id.item_groupdetailrecom_salenum,"已团${item?.virtual_num}件")
        helper?.addOnClickListener(R.id.item_crowdrecom_root)
    }
}