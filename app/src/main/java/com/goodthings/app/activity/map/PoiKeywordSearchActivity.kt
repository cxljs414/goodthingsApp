package com.goodthings.app.activity.map

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.Marker
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.maps2d.overlay.PoiOverlay
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.goodthings.app.R
import com.goodthings.app.util.AMapUtil
import com.goodthings.app.util.ScreenUtil
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionListener
import com.yinglan.scrolllayout.ScrollLayout
import kotlinx.android.synthetic.main.activity_poi_keyword_search.*
import kotlinx.android.synthetic.main.search_uri.view.*

class PoiKeywordSearchActivity : FragmentActivity(), View.OnClickListener, TextWatcher, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, PoiSearch.OnPoiSearchListener, Inputtips.InputtipsListener, ScrollLayout.OnScrollChangedListener, RecyclerViewAdapter.OnCheckIndexChanged, AMapLocationListener {
    lateinit var mLocationClient : AMapLocationClient
    lateinit var mLocationOption : AMapLocationClientOption
    var aMap:AMap?= null
    lateinit var dialog:ProgressDialog
    lateinit var poiResult:PoiResult
    lateinit var query:PoiSearch.Query
    lateinit var poiSearch:PoiSearch
    lateinit var aMapLocation:AMapLocation
    var currentPage:Int = 0
    var keyWord:String = ""
    var topHeight:Int = 0
    var scrollHeight:Int = 0
    val pois:ArrayList<PoiItem> = ArrayList()
    lateinit var adapter:RecyclerViewAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poi_keyword_search)
        mapView.onCreate(savedInstanceState)
        initScrollLayout()
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                .send()
        aMap = mapView.map
        val uiSetting = aMap?.getUiSettings();
        uiSetting?.isZoomControlsEnabled = false
        searchButton.setOnClickListener(this)
        keyWordtv.addTextChangedListener(this)
        aMap?.setOnMarkerClickListener(this)
        aMap?.setInfoWindowAdapter(this)
        aMap?.moveCamera(CameraUpdateFactory.zoomTo(19f))

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        AndPermission.onRequestPermissionsResult(this,requestCode,permissions,grantResults,object: PermissionListener {
            override fun onSucceed(requestCode: Int) {
                startLocation()
            }

            override fun onFailed(requestCode: Int) {
                Toast.makeText(this@PoiKeywordSearchActivity,"无此操作权限!",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun startLocation() {
        mLocationClient = AMapLocationClient(applicationContext)
        mLocationClient.setLocationListener(this)
        mLocationOption = AMapLocationClientOption()
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        mLocationOption.isOnceLocation = true
        //mLocationOption.isOnceLocationLatest = true
        mLocationOption.isNeedAddress = true
        mLocationOption.isMockEnable = true
        mLocationOption.httpTimeOut = 20000
        mLocationOption.isLocationCacheEnable = false
        mLocationClient.setLocationOption(mLocationOption)
        mLocationClient.startLocation()
    }

    override fun onLocationChanged(p0: AMapLocation?) {
        if(p0?.errorCode ==0){
            aMapLocation = p0
            initLocation()
            scrolllayout.scrollToOpen()
            return
        }

        Toast.makeText(this@PoiKeywordSearchActivity,"定位失败"+p0?.errorCode,Toast.LENGTH_SHORT).show()
    }

    private fun initLocation() {
        val markerOption:MarkerOptions = MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(LatLng(aMapLocation.latitude,aMapLocation.longitude))
                .title(aMapLocation.aoiName)
                .snippet(aMapLocation.address)
        aMap?.clear()
        aMap?.addMarker(markerOption)
        aMap?.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(aMapLocation.latitude,aMapLocation.longitude)))
        val item = PoiItem("0",
                LatLonPoint(aMapLocation.longitude,aMapLocation.latitude),
                aMapLocation.aoiName,aMapLocation.address)
        pois.add(item)
    }

    private fun initScrollLayout() {
        recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter(this,pois,this)
        recyclerview.adapter = adapter

        val maxOffset = ScreenUtil.dip2px(this,200f);
        topHeight = ScreenUtil.dip2px(this,50f)
        scrollHeight = (ScreenUtil.getScreenHeight(this)-maxOffset)
        scrolllayout.minOffset = 0
        scrolllayout.setMaxOffset(maxOffset)
        scrolllayout.setExitOffset(topHeight)
        scrolllayout.setIsSupportExit(true)
        scrolllayout.isAllowHorizontalScroll= false
        scrolllayout.setOnScrollChangedListener(this)
        scrolllayout.setToExit()
        root.setOnClickListener{
            scrolllayout.scrollToExit()
        }

        text_foot.setOnClickListener{
            scrolllayout.scrollToOpen()
        }

        sure.setOnClickListener {
            if(pois.size > 0) {
                val intent = Intent()
                val selectPoi: PoiItem = pois[adapter.checkIndex]
                intent.putExtra("selectPoi", selectPoi)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.searchButton -> search()
        }
    }

    private fun nextPage() {
        if(query != null && poiSearch != null && poiResult != null){
            if(poiResult.pageCount -1 > currentPage){
                currentPage++
                query.pageNum = currentPage
                poiSearch.searchPOIAsyn()
            }else{
                Toast.makeText(this@PoiKeywordSearchActivity,"没有下一页了",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private fun search() {
        keyWord = AMapUtil.checkEditText(keyWordtv)
        if(keyWord == ""){
            Toast.makeText(this@PoiKeywordSearchActivity,"请输入搜索关键字",Toast.LENGTH_SHORT).show()
        }else{
            showProgressDialog()
            currentPage = 0
            query = PoiSearch.Query(keyWord,"","${aMapLocation.city}")
            query.pageSize = 10
            query.pageNum = currentPage
            query.cityLimit = true

            poiSearch = PoiSearch(this,query)
            poiSearch.setOnPoiSearchListener(this)
            poiSearch.searchPOIAsyn()
        }
    }

    private fun showProgressDialog() {
        dialog = ProgressDialog(this)
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.isIndeterminate = false
        dialog.setCancelable(false)
        dialog.setMessage("正在搜索：\n $keyWord")
        dialog.show()
    }

    private fun dissmissDialog(){
        if(dialog != null)dialog.dismiss()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val newText = p0.toString().trim()
        if(!AMapUtil.isEmptyOrNullString(newText)){
            val inputQuery = InputtipsQuery(newText,"")
            val inputTips = Inputtips(this@PoiKeywordSearchActivity,inputQuery)
            inputTips.setInputtipsListener(this)
            inputTips.requestInputtipsAsyn()
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        p0?.showInfoWindow()
        return false
    }

    override fun getInfoContents(p0: Marker?): View? {
        return null
    }

    override fun getInfoWindow(p0: Marker?): View {
        val view = layoutInflater.inflate(R.layout.search_uri,null)
        view.title.text = p0?.title
        view.snippet.text = p0?.snippet
        return view
    }

    override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
    }

    override fun onPoiSearched(result: PoiResult?, p1: Int) {
        val imm: InputMethodManager =  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
        if(imm != null){
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        dissmissDialog()
        if(p1 == AMapException.CODE_AMAP_SUCCESS){
            if(result != null && result.query != null){
                poiResult = result
                val poiItems:ArrayList<PoiItem> = poiResult.pois
                if(poiItems?.size >0){
                    aMap?.clear()
                    val poiOverlay = PoiOverlay(aMap,poiItems)
                    poiOverlay.removeFromMap()
                    poiOverlay.addToMap()
                    poiOverlay.zoomToSpan()
                    updateRecyclerView(poiItems)
                    scrolllayout.scrollToOpen()
                }else{
                    Toast.makeText(this@PoiKeywordSearchActivity,"没有搜索到相关数据",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@PoiKeywordSearchActivity,"没有搜索到相关数据",Toast.LENGTH_SHORT).show()
            }
        }else{
            AMapUtil.showError(this@PoiKeywordSearchActivity,p1)
        }
    }

    private fun updateRecyclerView(poiItems: ArrayList<PoiItem>) {
        if(currentPage == 0)pois.clear()
        val iterator = poiItems!!.iterator()
        while (iterator.hasNext()){
            pois.add(iterator.next())
        }
        adapter.notifyDataSetChanged()

    }
    override fun onCheckIndexChanged(position: Int) {
        val poi:PoiItem = pois[position]
        aMap?.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(poi.latLonPoint.latitude,poi.latLonPoint.longitude)))
        recyclerview.post {
            adapter.checkIndex = position
            adapter.notifyDataSetChanged()
        }

    }

    override fun onGetInputtips(p0: MutableList<Tip>?, p1: Int) {
        if(p1 == AMapException.CODE_AMAP_SUCCESS){
            val listString = ArrayList<String>()
            val iterator:MutableListIterator<Tip>? = p0?.listIterator()
            while (iterator!!.hasNext()){
                listString.add(iterator.next().name)
            }
            val adapter = ArrayAdapter<String>(applicationContext,R.layout.poi_item,listString)
            keyWordtv.setAdapter(adapter)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onScrollFinished(currentStatus: ScrollLayout.Status?) {
        if(currentStatus == ScrollLayout.Status.EXIT){
            text_foot.visibility = View.VISIBLE
            bottomlayout.visibility = View.GONE
        }
    }

    override fun onChildScroll(top: Int) {
    }
    override fun onScrollProgressChanged(currentProgress: Float) {
        Log.i("progress","$currentProgress")
        if(text_foot.visibility == View.VISIBLE){
            text_foot.visibility = View.GONE
        }
        bottomlayout.visibility = View.VISIBLE
        val topOffset:Float = scrollHeight*currentProgress
        if(topOffset >0 && topOffset < topHeight){
            toplayout.translationY = -(topHeight-topOffset)
            Log.i("progress","top:${toplayout.translationY}")
        }
        if(topOffset > topHeight){
            toplayout.translationY = 0f
        }
    }
}
