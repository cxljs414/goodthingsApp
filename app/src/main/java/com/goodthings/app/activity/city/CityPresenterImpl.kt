package com.goodthings.app.activity.city

import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.CityBean
import com.goodthings.app.bean.HotCityBean
import com.goodthings.app.bean.SysCityArea
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.goodthings.app.util.SPUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/19
 * 修改内容：
 * 最后修改时间：
 */
class CityPresenterImpl : BasePresenterImpl<CityContract.CityView>(),CityContract.CityPresenter {

    private var cityList:MutableList<CityBean> = ArrayList()
    private var cityResults:MutableList<CityBean> = ArrayList()
    override fun getCitys() {
        handlerCitys()
        if (Consts.cityList != null){
            handlerCityResult(Consts.cityList!!)
        }else{
            mView?.showProgressDialog("正在请求城市数据...")
            ApiManager.getCitys()
                    .compose(RxUtil.hanlderBaseResult())
                    .compose(lifecycle?.bindUntilEvent<List<CityBean>>(ActivityEvent.DESTROY))
                    .subscribe(
                            {
                                if(it != null){
                                    handlerCityResult(it)
                                    cityResults.clear()
                                    cityResults.addAll(it)
                                    Consts.cityList = cityResults
                                }
                                mView?.hideProgressDialog()
                            },
                            {
                                mView?.hideProgressDialog()
                                var letterMap= HashMap<String,Int>()
                                letterMap.put("定位",0)
                                letterMap.put("热门",1)
                                mView?.notifyCityAdapter(cityList, letterMap)
                            }
                    )
        }
    }

    private fun handlerCityResult(it:List<CityBean>){
        var size= cityList.size
        var index= size -1
        while (index > 1){
            cityList.removeAt(index)
            index-=1
        }

        //排序
        Collections.sort<CityBean>(it) { lhs, rhs ->
            if(lhs.word != null && rhs.word != null){
                return@sort lhs.word.compareTo(rhs.word)
            }else if(lhs.word != null && rhs.word == null){
                return@sort -1
            }else if(lhs.word == null && rhs.word != null){
                return@sort 1
            }else{
                return@sort 0
            }

        }

        //字母列表位置
        var letterMap= HashMap<String,Int>()
        letterMap.put("定位",0)
        letterMap.put("热门",1)
        var prviousCityBean = cityList[0]
        var letterIndex:Int=0
        it.forEach {
            if (it.word != prviousCityBean.word){
                letterMap.put(it.word,letterIndex+2)
            }
            prviousCityBean = it
            letterIndex+=1
            it.type = 2
            if(!it.word.isNullOrEmpty()){
                cityList.add(it)
            }
        }
        mView?.notifyCityAdapter(cityList,letterMap)
    }

    private fun handlerCitys() {
        var hotCityList:MutableList<HotCityBean> = ArrayList()
        hotCityList.add(HotCityBean("110000","北京"))
        hotCityList.add(HotCityBean("310000","上海"))
        hotCityList.add(HotCityBean("440300","深圳"))
        hotCityList.add(HotCityBean("440100","广州"))
        hotCityList.add(HotCityBean("120100","天津"))
        hotCityList.add(HotCityBean("330100","杭州"))
        hotCityList.add(HotCityBean("320100","南京"))
        hotCityList.add(HotCityBean("410100","郑州"))
        hotCityList.add(HotCityBean("350200","厦门"))
        hotCityList.add(HotCityBean("420100","武汉"))
        hotCityList.add(HotCityBean("610100","西安"))
        hotCityList.add(HotCityBean("130100","石家庄"))

        var hotCityBean = CityBean("","","","","热门",1,hotCityList)
        var curCity= SPUtil.getStringValue(mView?.getContext()!!,"city","")
        if(mView?.getActivity()?.intent!!.hasExtra("curCity")){
            curCity = mView?.getActivity()?.intent!!.getStringExtra("curCity")
        }
        cityList.add(0, CityBean("",curCity,"","","定位",0,null))
        cityList.add(1, hotCityBean)
    }

    override fun search(keyword: String) {
        var searchList:MutableList<CityBean> = ArrayList()
        cityResults.forEach {
            if(it.name.contains(keyword)){
                searchList.add(it)
            }
        }
        mView?.notifySearchUpdate(searchList)
    }

    override fun getCitySon(name: String, id: String) {
        mView?.showProgressDialog("正在请求二级城市数据...")
        ApiManager.cityson(id.toInt())
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<List<SysCityArea>>(ActivityEvent.DESTROY))
                .subscribe(
                        {
                            if(it != null){
                                var all= SysCityArea()
                                all.name = "全城"
                                var list:MutableList<SysCityArea> = it.toMutableList()
                                list.add(0,all)
                                mView?.showCityArea(name,id,list)
                            }
                            mView?.hideProgressDialog()
                        },
                        {
                            mView?.hideProgressDialog()
                        }
                )
    }

}