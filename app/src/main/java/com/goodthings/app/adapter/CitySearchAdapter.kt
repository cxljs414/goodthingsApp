package com.goodthings.app.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.bean.CityBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/19
 * 修改内容：
 * 最后修改时间：
 */
class CitySearchAdapter(data:List<CityBean>) :
        BaseQuickAdapter<CityBean,BaseViewHolder>(R.layout.item_city_search,data) {
    override fun convert(helper: BaseViewHolder?, item: CityBean?) {
        helper?.setText(R.id.item_city_search_name,item?.name)
        helper?.addOnClickListener(R.id.item_city_search_name)
    }
}