package com.goodthings.app.adapter

import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.bean.ShopBean
import com.goodthings.app.bean.ShopSpuBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/31
 * 修改内容：
 * 最后修改时间：
 */
class ShopProdAdapter(data:List<ShopSpuBean.Spu>,var imageWidth:Int):
        BaseQuickAdapter<ShopSpuBean.Spu,BaseViewHolder>(R.layout.item_shop_prod,data){
    override fun convert(helper: BaseViewHolder?, item: ShopSpuBean.Spu?) {
        var imageView = helper?.getView<ImageView>(R.id.item_shopprod_image)
        var imageheight= imageWidth*10/16
        imageView?.layoutParams = LinearLayout.LayoutParams(imageWidth,imageheight)
        imageView?.scaleType = ImageView.ScaleType.FIT_XY
        Glide.with(mContext).load(Consts.IMAGEURL+item?.coverurl).into(imageView)
        helper?.setText(R.id.item_shopprod_title,item?.prdtitle)
        helper?.setText(R.id.item_shopprod_likecount,"${item?.count!!}")
        helper?.setText(R.id.item_shopprod_price,"￥${item?.minprice}")
        helper?.addOnClickListener(R.id.item_shopprod_root)
    }
}