package com.goodthings.app.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.bean.GoldBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/4
 * 修改内容：
 * 最后修改时间：
 */
class GoldAdapter(beans:List<GoldBean>) : BaseQuickAdapter<GoldBean,BaseViewHolder>(R.layout.item_gold,beans) {

    private var curType= 0
    override fun convert(helper: BaseViewHolder?, item: GoldBean?) {
        if(curType == 0){
            helper?.setVisible(R.id.item_gold_balance,false)
            if(item?.type == 4){
                helper?.setText(R.id.item_gold_cash,"-${item?.coin}")
            }else{
                helper?.setText(R.id.item_gold_cash,"+${item?.coin}")
            }
            helper?.setText(R.id.item_gold_balance,"账户余额${item?.coin_balance}")
        }else{
            helper?.setVisible(R.id.item_gold_balance,true)
            if(item?.type == 4){
                helper?.setText(R.id.item_gold_cash,"-${item?.coin_change}")
            }else{
                helper?.setText(R.id.item_gold_cash,"+${item?.coin_change}")
            }
            helper?.setText(R.id.item_gold_balance,"账户余额${item?.coin_change_balance}")
        }
        helper?.setText(R.id.item_gold_type,item?.chanel)
        helper?.setText(R.id.item_gold_date,item?.create_time)
    }

    fun setType(type:Int){
        curType = type
    }
}