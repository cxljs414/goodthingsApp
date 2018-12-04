package com.goodthings.app.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.bean.GroupOrderBean
import com.goodthings.app.util.visible

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/29
 * 修改内容：
 * 最后修改时间：
 */
class GroupBuyOrdersAdapter(data:List<GroupOrderBean>):
BaseQuickAdapter<GroupOrderBean,BaseViewHolder>(R.layout.item_groupbuy_orders,data){

    override fun convert(helper: BaseViewHolder?, item: GroupOrderBean?) {
        helper?.setText(R.id.item_grouporders_title,item?.title)
        Glide.with(mContext).load(Consts.IMAGEURL+item?.cover_url).into(helper?.getView(R.id.item_grouporders_image))
        helper?.setText(R.id.item_grouporders_count,"x${item?.buy_num}")
        helper?.setText(R.id.item_grouporders_price,"￥${item?.trans_amt}")

        when(item?.status){
            0->{
                helper?.setText(R.id.item_grouporders_status,"待付款")
                helper?.setVisible(R.id.item_grouporders_status,true)
                helper?.setVisible(R.id.item_grouporders_cancel, true)
                helper?.setVisible(R.id.item_grouporders_pay, true)
                helper?.setVisible(R.id.item_grouporders_detail, true)
                helper?.setVisible(R.id.item_grouporders_take, false)
                helper?.setVisible(R.id.item_grouporders_comment,false)
            }
            1->{
                helper?.setText(R.id.item_grouporders_status,"已取消")
                helper?.setVisible(R.id.item_grouporders_status,true)
                helper?.setVisible(R.id.item_grouporders_cancel, false)
                helper?.setVisible(R.id.item_grouporders_pay, false)
                helper?.setVisible(R.id.item_grouporders_detail, true)
                helper?.setVisible(R.id.item_grouporders_take, false)
                helper?.setVisible(R.id.item_grouporders_comment,false)
            }
            2->{
                helper?.setText(R.id.item_grouporders_status,"待分享")
                helper?.setVisible(R.id.item_grouporders_status,true)
                helper?.setVisible(R.id.item_grouporders_cancel, false)
                helper?.setVisible(R.id.item_grouporders_pay, false)
                helper?.setVisible(R.id.item_grouporders_detail, true)
                helper?.setVisible(R.id.item_grouporders_take, false)
                helper?.setVisible(R.id.item_grouporders_comment,false)
            }
            3->{
                helper?.setText(R.id.item_grouporders_status,"待发货")
                helper?.setVisible(R.id.item_grouporders_status,true)
                helper?.setVisible(R.id.item_grouporders_cancel, false)
                helper?.setVisible(R.id.item_grouporders_pay, false)
                helper?.setVisible(R.id.item_grouporders_detail, true)
                helper?.setVisible(R.id.item_grouporders_take, false)
                helper?.setVisible(R.id.item_grouporders_comment,false)
            }
            4->{
                helper?.setText(R.id.item_grouporders_status,"待收货")
                helper?.setVisible(R.id.item_grouporders_status,true)
                helper?.setVisible(R.id.item_grouporders_cancel, false)
                helper?.setVisible(R.id.item_grouporders_pay, false)
                helper?.setVisible(R.id.item_grouporders_detail, true)
                helper?.setVisible(R.id.item_grouporders_take, true)
                helper?.setVisible(R.id.item_grouporders_comment,false)
            }
            5->{
                helper?.setText(R.id.item_grouporders_status,"已完成")
                helper?.setVisible(R.id.item_grouporders_status,true)
                helper?.setVisible(R.id.item_grouporders_cancel, false)
                helper?.setVisible(R.id.item_grouporders_pay, false)
                helper?.setVisible(R.id.item_grouporders_detail, true)
                helper?.setVisible(R.id.item_grouporders_take, false)
                helper?.setVisible(R.id.item_grouporders_comment,true)
            }
            6->{
                helper?.setText(R.id.item_grouporders_status,"已评价")
                helper?.setVisible(R.id.item_grouporders_status,true)
                helper?.setVisible(R.id.item_grouporders_cancel, false)
                helper?.setVisible(R.id.item_grouporders_pay, false)
                helper?.setVisible(R.id.item_grouporders_detail, true)
                helper?.setVisible(R.id.item_grouporders_take, false)
                helper?.setVisible(R.id.item_grouporders_comment,false)
            }
            7->{
                helper?.setText(R.id.item_grouporders_status,"未拼成")
                helper?.setVisible(R.id.item_grouporders_status,false)
                helper?.setVisible(R.id.item_grouporders_cancel, false)
                helper?.setVisible(R.id.item_grouporders_pay, false)
                helper?.setVisible(R.id.item_grouporders_detail, true)
                helper?.setVisible(R.id.item_grouporders_take, false)
                helper?.setVisible(R.id.item_grouporders_comment,false)
            }
            8->{
                helper?.setText(R.id.item_grouporders_status,"未拼成")
                helper?.setVisible(R.id.item_grouporders_status,false)
                helper?.setVisible(R.id.item_grouporders_cancel, false)
                helper?.setVisible(R.id.item_grouporders_pay, false)
                helper?.setVisible(R.id.item_grouporders_detail, true)
                helper?.setVisible(R.id.item_grouporders_take, false)
                helper?.setVisible(R.id.item_grouporders_comment,false)
            }
            9->{
                helper?.setText(R.id.item_grouporders_status,"未拼成")
                helper?.setVisible(R.id.item_grouporders_status,false)
                helper?.setVisible(R.id.item_grouporders_cancel, false)
                helper?.setVisible(R.id.item_grouporders_pay, false)
                helper?.setVisible(R.id.item_grouporders_detail, true)
                helper?.setVisible(R.id.item_grouporders_take, false)
                helper?.setVisible(R.id.item_grouporders_comment,false)
            }
        }

        helper?.addOnClickListener(R.id.item_grouporders_cancel)
        helper?.addOnClickListener(R.id.item_grouporders_pay)
        helper?.addOnClickListener(R.id.item_grouporders_detail)
        helper?.addOnClickListener(R.id.item_grouporders_take)
        helper?.addOnClickListener(R.id.item_grouporders_root)
        helper?.addOnClickListener(R.id.item_grouporders_comment)
    }
}