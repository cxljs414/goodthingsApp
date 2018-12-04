package com.goodthings.app.activity.city

import com.goodthings.app.base.BasePresenter
import com.goodthings.app.base.BaseView
import com.goodthings.app.bean.CityBean
import com.goodthings.app.bean.HotCityBean
import com.goodthings.app.bean.SysCityArea

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/19
 * 修改内容：
 * 最后修改时间：
 */
interface CityContract {

    interface CityView : BaseView{
        fun notifyCityAdapter(it: List<CityBean>, letterMap: HashMap<String, Int>)
        fun notifySearchUpdate(searchList: MutableList<CityBean>)
        fun showCityArea(name: String, id: String,it: List<SysCityArea>?)

    }

    interface CityPresenter : BasePresenter<CityView>{
        fun getCitys()
        fun search(toString: String)
        fun getCitySon(name: String, id: String)

    }

    interface OnHotCityClickListener{
        fun onHotCityClick(cityBean:HotCityBean)
    }
}