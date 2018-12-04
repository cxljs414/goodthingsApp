package com.goodthings.app.activity.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.bumptech.glide.Glide
import com.goodthings.app.R
import com.goodthings.app.activity.AdvertAcivity
import com.goodthings.app.activity.login.LoginActivity
import com.goodthings.app.activity.main.MainActivity
import com.goodthings.app.activity.shopmall.ShopMallFragment
import com.goodthings.app.application.Consts
import com.goodthings.app.util.AppUtil.backgroundAlpha
import com.goodthings.app.util.SPUtil
import com.goodthings.app.util.ScreenUtil
import com.goodthings.app.view.HomeMenuPopwindow
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class HomeActivity : AppCompatActivity(), AMapLocationListener {
    private lateinit var fragments : MutableList<Fragment>
    private lateinit var radioButtonIds:MutableList<Int>
    private var menuPopupWindow: HomeMenuPopwindow? = null
    private var isLogined:Boolean = false
    lateinit var mLocationClient : AMapLocationClient
    lateinit var mLocationOption : AMapLocationClientOption
    private var locationDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if(intent.hasExtra("imgurl")){
            val adintent = Intent(this@HomeActivity, AdvertAcivity::class.java)
            adintent.putExtras(intent)
            startActivityForResult(adintent,Consts.REQUEST_ADVER)
        }else{
            //开始定位
            startLocation()
        }
        initTopbar()
        radioButtonIds = ArrayList()
        radioButtonIds.add(R.id.rb_shouye)
        radioButtonIds.add(R.id.rb_shangcheng)
        radioButtonIds.add(R.id.rb_msg)
        radioButtonIds.add(R.id.rb_mine)

        fragments = ArrayList()
        fragments.add(HomeFragment())
        fragments.add(ShopMallFragment())
        fragments.add(ShopMallFragment())
        fragments.add(ShopMallFragment())
        viewpager.adapter = MyViewPageAdapter(supportFragmentManager,fragments)
        viewpager.offscreenPageLimit = 4
        viewpager.addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
                home_radiogroup.check(radioButtonIds[position])
            }
        })
        home_radiogroup.setOnCheckedChangeListener { _, checkedId ->
            viewpager.currentItem = radioButtonIds.indexOf(checkedId)
        }

    }

    private fun initTopbar() {
        menuPopupWindow = HomeMenuPopwindow(this)
        menuPopupWindow?.setOnDismissListener{
            backgroundAlpha(this,1f)
        }
        menuPopupWindow?.setNewMsgVisible(View.VISIBLE)
        home_menu.setOnClickListener {
            backgroundAlpha(this,0.5f)
            menuPopupWindow?.showAtLocation(home_menu, Gravity.NO_GRAVITY, (home_menu.x.toInt()- ScreenUtil.dip2px(this,5f)), home_menu.y.toInt())
        }
        home_login_tv.setOnClickListener {
            startActivity(Intent(this@HomeActivity,LoginActivity::class.java))
        }

        home_login_layout.setOnClickListener {
            //viewpager.currentItem = 3
            startActivity(Intent(this@HomeActivity,MainActivity::class.java))
        }

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
            var isShowLocated = SPUtil.getBooleanValue(this,"isShowLocated")
            isShowLocated = false
            if(!isShowLocated){
                //未显示过定位城市弹框
                showLocationDialog(p0.city)
                SPUtil.putBooleanValue(this,"isShowLocated",true)
                SPUtil.putStringValue(this,"showLocatedDateString",System.currentTimeMillis().toString())
            }else{
                var showLocatedDateString:String = SPUtil.getStringValue(this,"showLocatedDateString")
                if(!showLocatedDateString.isEmpty()){
                    var date:Date = Date(showLocatedDateString)
                    var cal = Calendar.getInstance()
                    cal.time = date
                    cal.add(Calendar.DAY_OF_MONTH,1)
                    var curCalendar = Calendar.getInstance()
                    if(curCalendar.after(cal)){
                        showLocationDialog(p0.city)
                    }

                }
            }
        }
    }

    private fun showLocationDialog(city: String) {
        locationDialog = null

        var view:View = View.inflate(this@HomeActivity,R.layout.dialog_changecity,null)
        var msg:TextView = view.findViewById(R.id.dialog_changecity_msg)
        var cancel:TextView = view.findViewById(R.id.dialog_changecity_cancel)
        var sure:TextView = view.findViewById(R.id.dialog_changecity_sure)
        msg.text = "您当前的位置在$city，是否切换城市？"
        cancel.setOnClickListener {
            if(locationDialog!= null && locationDialog!!.isShowing){
                locationDialog?.dismiss()
            }
        }
        sure.setOnClickListener {
            if(locationDialog!= null && locationDialog!!.isShowing){
                locationDialog?.dismiss()
            }
            home_location.text = city
        }
        locationDialog =  Dialog(this@HomeActivity,R.style.Dialog)
        locationDialog?.setContentView(view)
        locationDialog?.setCancelable(false)
        locationDialog?.show()
    }

    override fun onResume() {
        super.onResume()
        if(!isLogined) {
            var user = SPUtil.getUserBean(this)
            //检查是否登录
            if (user != null) {
                isLogined = true
                Consts.isLogined = true
                home_login_tv.visibility = View.GONE
                home_login_layout.visibility = View.VISIBLE
                home_use_nickname.text = user.nickname
                Glide.with(this).load(user.head_url).error(R.mipmap.default_headpic).into(home_use_headview)
            } else {
                home_login_tv.visibility = View.VISIBLE
                home_login_layout.visibility = View.GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Consts.REQUEST_ADVER -> startLocation()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationDialog = null
    }

    class MyViewPageAdapter(fm: FragmentManager?, private val fragments:MutableList<Fragment>) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }
}
