package com.goodthings.app.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.activity.applyaftersale.ApplyAfterSaleContract
import com.goodthings.app.application.Consts

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/23
 * 修改内容：
 * 最后修改时间：
 */
class ApplyPicAdapter(data:List<String>):BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_apply_pics,data) {
    var addListener:ApplyAfterSaleContract.OnAddPicListener? = null
    var seeListener:ApplyAfterSaleContract.OnSeePicListener? = null
    override fun convert(helper: BaseViewHolder?, item: String?) {
        var image= helper?.getView<ImageView>(R.id.item_applypics_image)

        if(item == "add"){
            Glide.with(mContext).load(R.mipmap.other_pic_bg).into(image)
            image?.setOnClickListener{
                addListener?.onAddpic(helper?.adapterPosition!!)
            }
            helper?.setVisible(R.id.item_applypics_close,false)
        }else{
            Glide.with(mContext).load(Consts.IMAGEURL+item).asBitmap().into(image)
            image?.setOnClickListener {
                seeListener?.onSeePic(item)
            }
            helper?.setVisible(R.id.item_applypics_close,true)
        }

        helper?.addOnClickListener(R.id.item_applypics_close)
    }
}