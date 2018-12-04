package com.goodthings.app.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.bean.SysCityArea

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/10
 * 修改内容：
 * 最后修改时间：
 */
class CityAreaAdapter(data:List<SysCityArea>):
        BaseQuickAdapter<SysCityArea,BaseViewHolder>(R.layout.item_city_area,data){
    override fun convert(helper: BaseViewHolder?, item: SysCityArea?) {
        helper?.setText(R.id.item_city_area_name,item?.name)
        helper?.addOnClickListener(R.id.item_city_area_name)
    }
}