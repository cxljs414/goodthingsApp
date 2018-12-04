package com.goodthings.app.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.bean.ExpressDetailBean
import java.text.SimpleDateFormat
import java.util.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/24
 * 修改内容：
 * 最后修改时间：
 */
class ExpressAdapter(data:List<ExpressDetailBean.Trace>):
        BaseQuickAdapter<ExpressDetailBean.Trace,BaseViewHolder>(R.layout.item_express,data){
    override fun convert(helper: BaseViewHolder?, item: ExpressDetailBean.Trace?) {
        helper?.setText(R.id.item_express_content,item?.acceptStation)
        when {
            helper?.adapterPosition == 0 -> {
                helper.setTextColor(R.id.item_express_content,mContext.resources.getColor(R.color.orange_239))
                helper.setTextColor(R.id.item_express_time,mContext.resources.getColor(R.color.orange_239))
                helper.setVisible(R.id.item_express_helf1,false)
                helper.setVisible(R.id.item_express_helf2,true)
                helper.setImageResource(R.id.item_express_image,R.mipmap.express_orange)
            }
            helper?.adapterPosition == data.size-1 -> {
                helper.setTextColor(R.id.item_express_content,mContext.resources.getColor(R.color.grey_102))
                helper.setTextColor(R.id.item_express_time,mContext.resources.getColor(R.color.grey_102))
                helper.setVisible(R.id.item_express_helf1,true)
                helper.setVisible(R.id.item_express_helf2,false)
                helper.setImageResource(R.id.item_express_image,R.mipmap.express_grey)
            }
            else -> {
                helper?.setTextColor(R.id.item_express_content,mContext.resources.getColor(R.color.grey_102))
                helper?.setTextColor(R.id.item_express_time,mContext.resources.getColor(R.color.grey_102))
                helper?.setVisible(R.id.item_express_helf1,true)
                helper?.setVisible(R.id.item_express_helf2,true)
                helper?.setImageResource(R.id.item_express_image,R.mipmap.express_grey)
            }
        }
        var date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(item?.acceptTime)
        var time:String = SimpleDateFormat("MM-dd\nhh:mm").format(date)
        helper?.setText(R.id.item_express_time,time)
    }
}