package com.goodthings.app.adapter

import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.bean.ProdCrowdBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/3
 * 修改内容：
 * 最后修改时间：
 */
class CrowdDetailRecomAdapter(data: List<ProdCrowdBean>, var imageWidth: Int):
    BaseQuickAdapter<ProdCrowdBean,BaseViewHolder>(R.layout.item_crowddetail_recomd,data){
    override fun convert(helper: BaseViewHolder?, item: ProdCrowdBean?) {
        var imageView = helper?.getView<ImageView>(R.id.item_crowdrecom_image)
        var imageheight= imageWidth*10/16
        imageView?.layoutParams = LinearLayout.LayoutParams(imageWidth,imageheight)
        imageView?.scaleType = ImageView.ScaleType.FIT_XY
        Glide.with(mContext).load(Consts.IMAGEURL+item?.cover_pic).into(imageView)
        helper?.setText(R.id.item_crowdrecom_title,item?.title)
        helper?.setText(R.id.item_crowdrecom_price,"￥${item?.price}")
        helper?.setText(R.id.item_crowdrecom_salenum,"${item?.userCount}个支持者")
        helper?.addOnClickListener(R.id.item_crowdrecom_root)
    }
}