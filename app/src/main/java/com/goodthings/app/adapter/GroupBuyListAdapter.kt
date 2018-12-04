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
 * 创建时间：2018/5/29
 * 修改内容：
 * 最后修改时间：
 */
class GroupBuyListAdapter(data: List<GroupListBean>, var imageWidth: Int,var isMain:Boolean):
        BaseQuickAdapter<GroupListBean,BaseViewHolder>(R.layout.item_groupbuylist,data) {
    override fun convert(helper: BaseViewHolder?, item: GroupListBean?) {
        var imageView = helper?.getView<ImageView>(R.id.item_groupbuy_image)
        var imageheight= imageWidth*10/16
        imageView?.layoutParams = LinearLayout.LayoutParams(imageWidth,imageheight)
        imageView?.scaleType = ImageView.ScaleType.FIT_XY
        Glide.with(mContext).load(Consts.IMAGEURL+item?.cover_url).into(imageView)
        helper?.addOnClickListener(R.id.item_groupbuy_root)
        helper?.setText(R.id.item_groupbuy_title,item?.title)
        if(isMain){
            helper?.setVisible(R.id.item_groupbuy_type1,true)
            helper?.setVisible(R.id.item_groupbuy_type0,false)
            helper?.setText(R.id.item_groupbuy_main_oldprice,"零售价：￥${item?.old_price}")
            helper?.setText(R.id.item_groupbuy_main_price,"￥${item?.price}")
            helper?.setText(R.id.item_groupbuy_main_count,"已团${item?.virtual_num}件")
        }else{
            helper?.setVisible(R.id.item_groupbuy_type1,false)
            helper?.setVisible(R.id.item_groupbuy_type0,true)
            helper?.setText(R.id.item_groupbuy_price,"￥${item?.price}")
            helper?.setText(R.id.item_groupbuy_count,"已团${item?.virtual_num}件")
        }

    }
}