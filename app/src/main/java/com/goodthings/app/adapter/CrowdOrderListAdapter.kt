package com.goodthings.app.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.bean.CrowdOrderBean
import com.goodthings.app.util.DoubleUtil

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/8
 * 修改内容：
 * 最后修改时间：
 */
class CrowdOrderListAdapter(data:List<CrowdOrderBean>) :
    BaseQuickAdapter<CrowdOrderBean,BaseViewHolder>(R.layout.item_crowd_order,data){
    override fun convert(helper: BaseViewHolder?, item: CrowdOrderBean?) {
        helper?.setText(R.id.item_crowdorder_title,item?.title)
        Glide.with(mContext).load(Consts.IMAGEURL+item?.sub_cover_pic).into(helper?.getView(R.id.item_crowdorder_image))
        helper?.setText(R.id.item_crowdorder_count,"x${item?.count}")
        helper?.setText(R.id.item_crowdorder_price,"￥${item?.price}")
        var per= item?.sale_num!!.toDouble() /item?.stock!!.toDouble()
        var percen = DoubleUtil.round(per * 100,2)
        if(percen > percen.toInt()){
            helper?.setText(R.id.item_crowdorder_percent,"完成度：$percen%")
        }else{
            helper?.setText(R.id.item_crowdorder_percent,"完成度：${percen.toInt()}%")
        }

        when(item.status){
            1->{
                helper?.setText(R.id.item_crowdorder_status,"待付款")
                helper?.setVisible(R.id.item_crowdorder_status,true)
                helper?.setVisible(R.id.item_crowdorder_faile,false)
                helper?.setVisible(R.id.item_crowdorder_cancel, true)
                helper?.setVisible(R.id.item_crowdorder_pay, true)
                helper?.setVisible(R.id.item_crowdorder_detail_gray, false)
                helper?.setVisible(R.id.item_crowdorder_detail, true)
                helper?.setVisible(R.id.item_crowdorder_take, false)
            }
            2->{
                helper?.setText(R.id.item_crowdorder_status,"已取消")
                helper?.setVisible(R.id.item_crowdorder_status,true)
                helper?.setVisible(R.id.item_crowdorder_faile,false)
                helper?.setVisible(R.id.item_crowdorder_cancel, false)
                helper?.setVisible(R.id.item_crowdorder_pay, false)
                helper?.setVisible(R.id.item_crowdorder_detail_gray, false)
                helper?.setVisible(R.id.item_crowdorder_detail, true)
                helper?.setVisible(R.id.item_crowdorder_take, false)
            }
            3->{
                helper?.setText(R.id.item_crowdorder_status,"已支付")
                helper?.setVisible(R.id.item_crowdorder_status,true)
                helper?.setVisible(R.id.item_crowdorder_faile,false)
                helper?.setVisible(R.id.item_crowdorder_cancel, false)
                helper?.setVisible(R.id.item_crowdorder_pay, false)
                helper?.setVisible(R.id.item_crowdorder_detail_gray, false)
                helper?.setVisible(R.id.item_crowdorder_detail, true)
                helper?.setVisible(R.id.item_crowdorder_take, false)
            }
            4->{
                helper?.setText(R.id.item_crowdorder_status,"已发货")
                helper?.setVisible(R.id.item_crowdorder_status,true)
                helper?.setVisible(R.id.item_crowdorder_faile,false)
                helper?.setVisible(R.id.item_crowdorder_cancel, false)
                helper?.setVisible(R.id.item_crowdorder_pay, false)
                helper?.setVisible(R.id.item_crowdorder_detail_gray, true)
                helper?.setVisible(R.id.item_crowdorder_detail, false)
                helper?.setVisible(R.id.item_crowdorder_take, true)
            }
            5,6->{
                helper?.setText(R.id.item_crowdorder_status,"预购成功")
                helper?.setVisible(R.id.item_crowdorder_status,true)
                helper?.setVisible(R.id.item_crowdorder_faile,false)
                helper?.setVisible(R.id.item_crowdorder_cancel, false)
                helper?.setVisible(R.id.item_crowdorder_pay, false)
                helper?.setVisible(R.id.item_crowdorder_detail_gray, false)
                helper?.setVisible(R.id.item_crowdorder_detail, true)
                helper?.setVisible(R.id.item_crowdorder_take, false)
            }
            7->{//预购失败
                helper?.setText(R.id.item_crowdorder_status,"退款中")
                helper?.setVisible(R.id.item_crowdorder_status,false)
                helper?.setVisible(R.id.item_crowdorder_faile,false)
                helper?.setVisible(R.id.item_crowdorder_cancel, false)
                helper?.setVisible(R.id.item_crowdorder_pay, false)
                helper?.setVisible(R.id.item_crowdorder_detail_gray, false)
                helper?.setVisible(R.id.item_crowdorder_detail, true)
                helper?.setVisible(R.id.item_crowdorder_take, false)
            }
            8->{//预购失败
                helper?.setText(R.id.item_crowdorder_status,"")
                helper?.setVisible(R.id.item_crowdorder_status,false)
                helper?.setVisible(R.id.item_crowdorder_faile,true)
                helper?.setVisible(R.id.item_crowdorder_cancel, false)
                helper?.setVisible(R.id.item_crowdorder_pay, false)
                helper?.setVisible(R.id.item_crowdorder_detail_gray, false)
                helper?.setVisible(R.id.item_crowdorder_detail, true)
                helper?.setVisible(R.id.item_crowdorder_take, false)
            }
            9->{//预购失败
                helper?.setText(R.id.item_crowdorder_status,"已退款")
                helper?.setVisible(R.id.item_crowdorder_status,false)
                helper?.setVisible(R.id.item_crowdorder_faile,false)
                helper?.setVisible(R.id.item_crowdorder_cancel, false)
                helper?.setVisible(R.id.item_crowdorder_pay, false)
                helper?.setVisible(R.id.item_crowdorder_detail_gray, false)
                helper?.setVisible(R.id.item_crowdorder_detail, true)
                helper?.setVisible(R.id.item_crowdorder_take, false)
            }
        }

        helper?.addOnClickListener(R.id.item_crowdorder_cancel)
        helper?.addOnClickListener(R.id.item_crowdorder_pay)
        helper?.addOnClickListener(R.id.item_crowdorder_detail_gray)
        helper?.addOnClickListener(R.id.item_crowdorder_detail)
        helper?.addOnClickListener(R.id.item_crowdorder_take)
        helper?.addOnClickListener(R.id.item_crowdorder_root)
    }
}