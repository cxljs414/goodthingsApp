package com.goodthings.app.activity.city

import android.animation.Animator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import com.chad.library.adapter.base.BaseQuickAdapter
import com.goodthings.app.R
import com.goodthings.app.adapter.CityAdapter
import com.goodthings.app.adapter.CityAreaAdapter
import com.goodthings.app.adapter.CitySearchAdapter
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.CityBean
import com.goodthings.app.bean.HotCityBean
import com.goodthings.app.bean.SysCityArea
import com.goodthings.app.util.ScreenUtil
import com.goodthings.app.util.SpacesItemDecorationtwo
import kotlinx.android.synthetic.main.activity_city.*
import kotlinx.android.synthetic.main.top_bar.*

class CityActivity :
        BaseActivity<CityContract.CityView,CityContract.CityPresenter>(),
        CityContract.CityView {

    override var presenter: CityContract.CityPresenter = CityPresenterImpl()
    private var cityAdapter:CityAdapter? = null
    private var searchAdapter: CitySearchAdapter? = null
    private var cityAreaAdapter: CityAreaAdapter? = null
    private var needDetail:Boolean = false
    private var screenWidth:Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)
        toptitle.text = "选择服务区域"
        side_letter_bar.setOverlay(tv_letter_overlay)
        screenWidth = ScreenUtil.getScreenWidth(this).toFloat()
        needDetail = intent.getBooleanExtra("needDetail",false)
        topback.setOnClickListener {
            goBack()
        }
        city_recyclerview.layoutManager = LinearLayoutManager(this)
        city_recyclerview.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this,10f)))
        cityAdapter = CityAdapter(ArrayList())
        city_recyclerview.adapter = cityAdapter
        cityAdapter?.setOnHotClickListener(object:CityContract.OnHotCityClickListener{
            override fun onHotCityClick(cityBean: HotCityBean) {
                if(needDetail){
                    presenter.getCitySon(cityBean.name,cityBean.id)
                }else{
                    setCityResult(cityBean.name,cityBean.id,"")
                }
            }
        })


        search_recyclerview.layoutManager = LinearLayoutManager(this)
        search_recyclerview.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this,10f)))
        searchAdapter = CitySearchAdapter(ArrayList())
        search_recyclerview.adapter = searchAdapter

        et_search.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().isEmpty()){
                    search_recyclerview.visibility = View.GONE
                    citylayout.visibility = View.VISIBLE
                    iv_search_clear.visibility = View.GONE
                }else{
                    search_recyclerview.visibility = View.VISIBLE
                    citylayout.visibility = View.GONE
                    iv_search_clear.visibility = View.VISIBLE
                    presenter.search(s.toString())
                }
            }

        })

        citson_recyclerview.layoutManager = LinearLayoutManager(this)
        citson_recyclerview.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this,2f)))
        cityAreaAdapter = CityAreaAdapter(ArrayList())
        citson_recyclerview.adapter = cityAreaAdapter

        iv_search_clear.setOnClickListener {
            et_search.text = Editable.Factory.getInstance().newEditable("")
        }
        presenter.getCitys()
    }

    override fun notifyCityAdapter(citys: List<CityBean>, letterMap: HashMap<String, Int>) {
        side_letter_bar.setOnLetterChangedListener {
            letterMap[it]?.let { it1 -> city_recyclerview.scrollToPosition(it1) }
        }
        cityAdapter?.setNewData(citys)
        cityAdapter?.notifyDataSetChanged()
        cityAdapter?.let {
            it.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener{
                _, view, position ->
                if(view.id == R.id.tv_item_city_listview_name){
                    if(needDetail){
                        presenter.getCitySon(citys[position].name,citys[position].id)
                    }else{
                        setCityResult(citys[position].name,citys[position].id,"")
                    }

                }
            }
        }
    }

    override fun notifySearchUpdate(searchList: MutableList<CityBean>) {
        searchAdapter?.setNewData(searchList)
        searchAdapter?.notifyDataSetChanged()
        searchAdapter?.let {
            it.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener {
                _, view, position ->
                if(view.id == R.id.item_city_search_name){
                    if(needDetail){
                        presenter.getCitySon(searchList[position].name,searchList[position].id)
                    }else{
                        setCityResult(searchList[position].name,searchList[position].id,"")
                    }

                }
            }
        }
    }

    override fun showCityArea(name: String, id: String,it: List<SysCityArea>?) {

        citson_recyclerview.visibility = View.VISIBLE
        cityAreaAdapter?.setNewData(it)
        cityAreaAdapter?.notifyDataSetChanged()
        cityAreaAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if(view.id == R.id.item_city_area_name){
                var area:SysCityArea= adapter.getItem(position) as SysCityArea
                setCityResult(name,id,area.name!!)
            }

        }
    }

    private fun setCityResult(name: String, id: String,areaName:String) {
        var intent= Intent()
        intent.putExtra("cityName",name)
        intent.putExtra("cityCode",id)
        intent.putExtra("areaName",areaName)
        setResult(Activity.RESULT_OK,intent)
        onBackPressed()
    }

    fun goBack(){
        if(citson_recyclerview.visibility == View.VISIBLE){
            citson_recyclerview.visibility = View.GONE
        }else{
            onBackPressed()
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
