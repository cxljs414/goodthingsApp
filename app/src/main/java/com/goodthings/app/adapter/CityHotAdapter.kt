package com.goodthings.app.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.bean.CityBean
import com.goodthings.app.bean.HotCityBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/19
 * 修改内容：
 * 最后修改时间：
 */
class CityHotAdapter(data:List<HotCityBean>?) :
        BaseQuickAdapter<HotCityBean,BaseViewHolder>(R.layout.item_city_hot_item,data) {
    override fun convert(helper: BaseViewHolder?, item: HotCityBean?) {
        helper?.setText(R.id.item_city_hot_name,item?.name)
        helper?.addOnClickListener(R.id.item_city_hot_name)
    }

}