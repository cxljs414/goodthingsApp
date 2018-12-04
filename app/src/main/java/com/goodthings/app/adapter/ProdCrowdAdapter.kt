package com.goodthings.app.adapter

import android.graphics.Paint
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.bean.ProdCrowdBean
import com.goodthings.app.util.DoubleUtil
import kotlinx.android.synthetic.main.activity_prod_crowd_detail.*


/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/25
 * 修改内容：
 * 最后修改时间：
 */
class ProdCrowdAdapter(data:List<ProdCrowdBean>):
        BaseQuickAdapter<ProdCrowdBean,BaseViewHolder>(R.layout.item_prodcrowd ,data) {
    override fun convert(helper: BaseViewHolder?, item: ProdCrowdBean?) {
        Glide.with(mContext).load(Consts.IMAGEURL+item?.cover_pic).into(helper?.getView(R.id.item_prodcrowd_prodimage))
        var stateStr= "进行中"
        when(item?.status){0->stateStr = "进行中" 1->stateStr = "已成功" 2->stateStr = "已结束" }
        helper?.setText(R.id.item_prodcrowd_state,stateStr)
        helper?.setText(R.id.item_prodcrowd_title,item?.title)
        helper?.setText(R.id.item_prodcrowd_sale_amt,"￥${item?.sale_amt}")
        helper?.setText(R.id.item_prodcrowd_describe,item?.intro)
        var progressBar= helper?.getView<ProgressBar>(R.id.item_prodcrowd_progressbar)
        var per= item?.sale_num!!.toDouble() /item?.stock!!.toDouble()
        var percen = DoubleUtil.round(per*100,2)
        if(item?.is_manual_finish == 1){
            progressBar?.progress = 100
            helper?.setText(R.id.item_prodcrowd_progress_percent,"100%")
            helper?.setText(R.id.item_prodcrowd_state,"已成功")
        }else {
            progressBar?.progress = percen.toInt()
        }
        if(percen > percen.toInt()){
            helper?.setText(R.id.item_prodcrowd_progress_percent,"$percen%")
        }else{
            helper?.setText(R.id.item_prodcrowd_progress_percent,"${percen.toInt()}%")
        }

        var marketPrice= helper?.getView<TextView>(R.id.item_prodcrowd_market_price)
        marketPrice?.paint?.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
        marketPrice?.text = "市场价：${item?.oldPrice}元"
        helper?.setText(R.id.item_prodcrowd_corwd_price,"预购价：${item?.price}元")
        helper?.setText(R.id.item_prodcrowd_crowd_count,"预购数量：${item?.stock}件")
        helper?.addOnClickListener(R.id.item_prodcrowd_root)
    }
}