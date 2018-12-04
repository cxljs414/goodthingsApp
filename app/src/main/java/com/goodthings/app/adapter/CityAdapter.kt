package com.goodthings.app.adapter

import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.activity.city.CityContract
import com.goodthings.app.bean.CityBean
import com.goodthings.app.bean.HotCityBean
import com.goodthings.app.util.GridSpacingItemDecoration
import com.goodthings.app.util.NoScrollGridLayoutManager
import com.goodthings.app.util.ScreenUtil

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/19
 * 修改内容：
 * 最后修改时间：
 */
class CityAdapter(
        data:MutableList<CityBean>) :
        BaseMultiItemQuickAdapter<CityBean,BaseViewHolder>(data) {

    private var onHotClickListener: CityContract.OnHotCityClickListener? = null
    init {
        addItemType(0, R.layout.item_city_curlocation)
        addItemType(1, R.layout.item_city_hot)
        addItemType(2, R.layout.item_city_city)
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> 0
            1 -> 1
            else -> 2
        }
    }

    fun setOnHotClickListener(listener:CityContract.OnHotCityClickListener){
        this.onHotClickListener = listener
    }

    override fun convert(helper: BaseViewHolder?, item: CityBean?) {
        when(helper?.itemViewType){
            0 -> {
                helper.setText(R.id.city_curcity,item?.name)
            }

            1 -> {
                var hotRecyclerview = helper.getView<RecyclerView>(R.id.city_hot_recyclerview)
                hotRecyclerview.layoutManager = NoScrollGridLayoutManager(mContext,4)
                if(hotRecyclerview.itemDecorationCount == 0){
                    hotRecyclerview.addItemDecoration(GridSpacingItemDecoration(4, ScreenUtil.dip2px(mContext,15f),true))
                }
                var iteHotAdapter= CityHotAdapter(item?.hotcitys)
                hotRecyclerview.adapter = iteHotAdapter
                iteHotAdapter.setOnItemChildClickListener{
                    adapter,view,position ->
                    if(view.id == R.id.item_city_hot_name){
                        onHotClickListener?.onHotCityClick(adapter.getItem(position) as HotCityBean)
                    }
                }
            }

            2 -> {
                var lastBean= data[helper.adapterPosition-1]
                if(item?.word != lastBean.word){
                    helper.setVisible(R.id.tv_item_city_listview_letter, true)
                }else{
                    helper.setVisible(R.id.tv_item_city_listview_letter, false)
                }
                helper.setText(R.id.tv_item_city_listview_letter,item?.word)
                helper.setText(R.id.tv_item_city_listview_name,item?.name)
                helper.addOnClickListener(R.id.tv_item_city_listview_name)
            }
        }
    }

}