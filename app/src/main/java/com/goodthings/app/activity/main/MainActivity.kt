package com.goodthings.app.activity.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.animation.BounceInterpolator
import android.webkit.*
import android.widget.EditText
import android.widget.TextView
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.bumptech.glide.Glide
import com.goodthings.app.R
import com.goodthings.app.activity.AdvertAcivity
import com.goodthings.app.activity.addrlist.AddrListActivity
import com.goodthings.app.activity.city.CityActivity
import com.goodthings.app.activity.login.LoginActivity
import com.goodthings.app.activity.main.mainfrag.MainFragment
import com.goodthings.app.activity.map.PoiKeywordSearchActivity
import com.goodthings.app.activity.selectpic.SelectPicActivity
import com.goodthings.app.activity.userinfo.ChangeUserinfoActivity
import com.goodthings.app.activity.wallet.WalletActivity
import com.goodthings.app.activity.web.WebActivity
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.util.AppUtil
import com.goodthings.app.util.SPUtil
import com.goodthings.app.util.ScreenUtil
import com.goodthings.app.view.HomeMenuPopwindow
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.top_bar.*
import org.jetbrains.anko.runOnUiThread
import org.joda.time.LocalDate


class MainActivity : BaseActivity<MainContract.MainView,
        MainContract.MainPresenter>(),
        MainContract.MainView,
        AMapLocationListener {
    override var presenter: MainContract.MainPresenter = MainPresenterImpl()
    var titleStr:String= ""
    var mFileCallback: ValueCallback<Array<Uri>>? = null
    private var menuPopupWindow: HomeMenuPopwindow? = null
    private var phone:String = ""
    private lateinit var webview:WebView
    private var isLogined:Boolean = false
    lateinit var mLocationClient : AMapLocationClient
    lateinit var mLocationOption : AMapLocationClientOption
    private var locationDialog: Dialog? = null
    private var mainFragment:MainFragment? = null
    private var isWebViewLoading = false
    private var isNeedReLogin= false
    private var isOptLogin= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.start()
        initIntent()
        initTopbar()
        initFragment()
        initWebView()
        showHongBao()
    }

    private fun initTopbar() {
        menuPopupWindow = HomeMenuPopwindow(this)
        menuPopupWindow?.setOnDismissListener{
            AppUtil.backgroundAlpha(this, 1f)
        }
        menuPopupWindow?.setNewMsgVisible(View.GONE)
        menuPopupWindow?.setNewMsgClickListener(object: HomeMenuPopwindow.OnMsgClickListener {
            override fun onMsgClick() {
                goNext("msg","")
                menuPopupWindow?.dismiss()
            }

        })
        menuPopupWindow?.setScanClickListener(object:HomeMenuPopwindow.OnScanClickListener{
            override fun onScanClick() {
                /*this@MainActivity.startActivityForResult(
                        Intent(this@MainActivity, WalletActivity::class.java),
                        Consts.REQUEST_LOGIN)*/
                /*var intent=Intent(this@MainActivity, RegistHongBaoActivity::class.java)
                intent.putExtra("hongbao",2.45)
                startActivity(intent)
                menuPopupWindow?.dismiss()*/
            }

        })
        main_location.text = SPUtil.getStringValue(this@MainActivity,"city","北京市")
        main_menu.setOnClickListener {
            AppUtil.backgroundAlpha(this, 0.5f)
            menuPopupWindow?.showAtLocation(main_menu, Gravity.NO_GRAVITY, (main_menu.x.toInt()- ScreenUtil.dip2px(this,5f)), main_menu.y.toInt())
        }
        main_login_tv.setOnClickListener {
            this@MainActivity.startActivityForResult(
                    Intent(this@MainActivity, LoginActivity::class.java),
                    Consts.REQUEST_LOGIN)
        }
        main_location.setOnClickListener {
            var intent = Intent(this@MainActivity,CityActivity::class.java)
            var curCity = main_location.text
            intent.putExtra("curCity",curCity)
            startActivityForResult(intent,Consts.REQUEST_CITY)
        }

        main_login_layout.setOnClickListener {
            
        }

    }

    private fun initFragment() {
        mainFragment = MainFragment()
        val fm = this.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.fragframe, mainFragment)
        ft.commit()
    }

    private fun initIntent() {
        if(intent.hasExtra("imgurl")){
            val adintent = Intent(this@MainActivity,AdvertAcivity::class.java)
            adintent.putExtras(intent)
            startActivityForResult(adintent,Consts.REQUEST_ADVER)
        }else{
            //开始定位
            startLocation()
        }
    }

    /**
     * 初始化webview
     */
    @SuppressLint("JavascriptInterface")
    private fun initWebView() {

        webview = WebView(this)
        webframe.addView(webview)

        val s = webview.settings
        s.builtInZoomControls=true
        s.useWideViewPort = true
        s.loadWithOverviewMode = true
        s.javaScriptEnabled = true
        s.domStorageEnabled = true
        s.javaScriptCanOpenWindowsAutomatically = true
        s.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        s.setSupportZoom(false)
        s.setGeolocationEnabled(true)
        s.loadsImagesAutomatically = Build.VERSION.SDK_INT >= 19 //为了加快加载速度

        val cacheDirPath = filesDir.absolutePath+"cache/"
        s.setAppCachePath(cacheDirPath)
        s.setAppCacheEnabled(true)
        s.cacheMode = WebSettings.LOAD_DEFAULT
        s.blockNetworkImage = false
        s.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN

        webview.setLayerType(View.LAYER_TYPE_HARDWARE,null)
        webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webview.requestFocus()
        webview.requestFocusFromTouch()
        webview.webViewClient = object: WebViewClient(){
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                isWebViewLoading = false
                if(view == null)return
                webview.settings.blockNetworkImage = false
                if(view.title != null && !view.title.contains("wechat")){
                    toptitle.text = view.title
                    titleStr = view.title
                }else{
                    toptitle.text = ""
                    titleStr = ""
                }
                topback.visibility = View.GONE
                if(titleStr == "首页" || titleStr == "约单" || titleStr == "消息" || titleStr == "我的"){
                    topback.visibility = View.GONE
                    //footbar.visibility = View.VISIBLE
                    main_include.visibility = View.GONE
                    if(titleStr == "首页"){
                        main_top_bar.visibility = View.VISIBLE
                        main_footbar.visibility = View.VISIBLE
                        fragframe.visibility =View.VISIBLE
                        webframe.visibility = View.GONE
                        main_rb_shouye.isChecked = true
                    }else{
                        fragframe.visibility =View.GONE
                        webframe.visibility = View.VISIBLE
                        main_top_bar.visibility = View.GONE
                    }
                }else{
                    topback.visibility = View.VISIBLE
                    main_footbar.visibility = View.GONE
                    main_top_bar.visibility = View.GONE
                    main_include.visibility = View.VISIBLE
                    fragframe.visibility =View.GONE
                    webframe.visibility = View.VISIBLE
                }
                if(!webview.settings.loadsImagesAutomatically) {
                    webview.settings.loadsImagesAutomatically = true
                }

                if(isNeedReLogin){
                    presenter.retryLogin()
                    isNeedReLogin = false
                }
                if(titleStr == "我的" && isOptLogin){
                    isOptLogin = false
                    presenter.loadNewPage("4")
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                isWebViewLoading = true
                if(url == Consts.URL || url!!.contains("index")){
                    main_top_bar.visibility = View.VISIBLE
                    main_footbar.visibility = View.VISIBLE
                    fragframe.visibility =View.VISIBLE
                    webframe.visibility = View.GONE
                    main_rb_shouye.isChecked = true
                }

                if(url.contains("regist")){
                    isOptLogin = true
                    this@MainActivity.startActivityForResult(
                            Intent(this@MainActivity,LoginActivity::class.java),
                            Consts.REQUEST_LOGIN)
                }
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if(!isNetAvaiable) {
                    showMessage("请检查您的网络")
                }
                return false
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if(!isNetAvaiable) {
                    showMessage("请检查您的网络")
                }
                return false
            }

        }

        webview.webChromeClient = object :WebChromeClient() {
            override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
                if(mFileCallback == null) {
                    mFileCallback = filePathCallback
                    startActivityForResult(Intent(this@MainActivity, SelectPicActivity::class.java), Consts.REQUEST_SELECT_PIC)
                    overridePendingTransition(R.anim.enter_anim_alpha,0)
                }
                return true
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if(newProgress == 100){
                    progressBar.visibility = View.GONE
                }else{
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = newProgress
                }
            }



            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                showAlert(message)
                result?.confirm()
                return true
            }

            override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                AlertDialog.Builder(this@MainActivity)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("确定"
                        ) { dialog, _ ->
                            dialog.dismiss()
                            result?.confirm()
                        }
                        .setNegativeButton("取消"){
                            _, _ ->
                            result?.cancel()
                        }
                        .show()

                return true
            }

            override fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?): Boolean {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("对话框").setMessage(message)
                val et = EditText(this@MainActivity)
                et.setSingleLine()
                et.setText(defaultValue)
                val dialog = builder.setView(et)
                        .setPositiveButton("确定"){
                            dialog, _ -> result?.confirm(et.text.toString())
                        }
                        .setNeutralButton("取消"){
                            _, _ -> result?.cancel()
                        }
                        .setOnKeyListener{// 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
                            dialog,keyCode,event -> true
                        }.setCancelable(false)
                dialog.show()
                return true
            }
        }
        webview.addJavascriptInterface(this,"AndroidInterface")
        //webview.loadUrl("file:///android_asset/map.html")
        webview.loadUrl(Consts.URL)
        //webview.loadUrl(Consts.URL+"sysUser/unauth/goLogin?fromType=0")
        topback.setOnClickListener {
            if (webview.canGoBack()){
                webview.goBack()
            }
        }
        main_rb_shouye.setOnClickListener {
            //webview.loadUrl(Consts.URL)
            presenter.loadNewPage("1")
            main_footbar.visibility = View.VISIBLE
            main_top_bar.visibility = View.VISIBLE
            fragframe.visibility =View.VISIBLE
            //webframe.visibility = View.GONE
            main_include.visibility = View.GONE
            main_rb_shouye.isChecked = true
            //每次点击首页更新消息

        }
        main_rb_yuedan.setOnClickListener {
            //webview.loadUrl(Consts.URL_APPOINTMENT)
            presenter.loadNewPage("2")
        }
        main_rb_msg.setOnClickListener {
            presenter.loadNewPage("3")
        }
        main_rb_mine.setOnClickListener {
            presenter.loadNewPage("4")
        }
        main_fabu_floatbt.setOnClickListener {
            showFabuFrame()
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

    private fun showLocationDialog(city: String) {
        locationDialog = null

        var view:View = View.inflate(this@MainActivity,R.layout.dialog_changecity,null)
        var msg: TextView = view.findViewById(R.id.dialog_changecity_msg)
        var cancel: TextView = view.findViewById(R.id.dialog_changecity_cancel)
        var sure: TextView = view.findViewById(R.id.dialog_changecity_sure)
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
            main_location.text = city
            presenter.requestCityCode(city)
            goNext("notifyCityChange",city)
        }
        locationDialog =  Dialog(this@MainActivity,R.style.Dialog)
        locationDialog?.setContentView(view)
        locationDialog?.setCancelable(false)
        locationDialog?.show()
    }

    private fun checkLogin(){
        Log.i("main","islogined="+isLogined)
        if(!isLogined) {
            var user = SPUtil.getUserBean(this)
            //检查是否登录
            if (user != null) {
                Log.i("main","user!=null")
                isLogined = true
                if(!Consts.isLogined){
                    Consts.isLogined = true
                    Consts.user = user
                }

                main_login_tv.visibility = View.GONE
                main_login_layout.visibility = View.VISIBLE
                main_use_nickname.text = Consts.user!!.nickname
                Glide.with(this).load(Consts.user!!.head_url).error(R.mipmap.default_headpic).into(main_use_headview)
                if(isWebViewLoading){
                    isNeedReLogin = true
                }else{
                    presenter.retryLogin()
                }
            } else {
                Log.i("main","user=null")
                main_login_tv.visibility = View.VISIBLE
                main_login_layout.visibility = View.GONE
            }

        }else{
            if(isWebViewLoading){
                isNeedReLogin = true
            }else{
                presenter.retryLogin()
            }
        }
    }

    public fun goNext(pageName:String,params:String){
        if(pageName == "newsMore"){
            fragframe.visibility =View.GONE
            webframe.visibility = View.VISIBLE
            main_top_bar.visibility = View.GONE
            main_include.visibility = View.GONE
            presenter.loadNewPage("2")
            main_rb_yuedan.isChecked = true
            return
        }
        presenter.goNextPage(pageName,params)
    }

    fun share(title:String,content:String,url:String,imageUrl: String,shareId:String){
        openShareWindow(this@MainActivity,root,url,title,content,imageUrl,object:UMShareListener{
            override fun onResult(p0: SHARE_MEDIA?) {
                presenter.shareGoldEnvelopes(shareId)
            }

            override fun onCancel(p0: SHARE_MEDIA?) {
            }

            override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                showError(p1?.message)
            }

            override fun onStart(p0: SHARE_MEDIA?) {
            }

        })
    }

    /**
     * 原生调用js
     */
    override fun callJs(js: String, callback: ValueCallback<String>?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview.evaluateJavascript(js) { value ->
                callback?.onReceiveValue(value)
            }
        } else {
            webview.loadUrl(js)
        }
    }

    /**
     * 返回图片
     */
    override fun getPicSuccess(s: Array<Uri>) {
        mFileCallback.let {
            it?.onReceiveValue(s)
            mFileCallback = null
        }
    }

    /**
     * 获取图片
     */
    override fun getPicError() {
        mFileCallback.let {
            it?.onReceiveValue(null)
            mFileCallback = null
        }
    }

    /**
     * 添加权限成功
     */
    override fun addPermissionSuccess(requestCode: Int) {
        super.addPermissionSuccess(requestCode)
        if(requestCode == 101){
            val intenttle = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone))
            this@MainActivity.startActivity(intenttle)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            presenter.handleActivityResult(requestCode,data)
            if(requestCode == Consts.REQUEST_ADVER){
                startLocation()
            }
            if(requestCode == Consts.REQUEST_CHANGEUSERINFO){
                if(data != null) {
                    webview.clearCache(true)
                    var nickName = data.getStringExtra("nickName")
                    var revisesSex = data.getIntExtra("revisesSex",2)
                    var headUrl = data.getStringExtra("headUrl")
                    var revisesAge = data.getIntExtra("revisesAge",5)

                    presenter.saveUserInfo(nickName,revisesSex,headUrl,revisesAge)
                    var user = SPUtil.getUserBean(this)
                    user?.head_url = Consts.IMAGEURL + headUrl
                    user?.nickname = nickName
                    user?.age_range = revisesAge
                    user?.sex_key = revisesSex
                    SPUtil.saveUserBean(this, user)
                    Consts.user = user
                }
                isLogined = false
                checkLogin()
            }
        }else{
            if(requestCode == Consts.REQUEST_SELECT_PIC){
                getPicError()
            }
            presenter.handleLoginResult(false, data)

        }

    }

    /**
     * 红包
     * 规则：不管登录与否，每天只显示一次
     */
    override fun showHongBao() {
        var lastTime= SPUtil.getStringValue(this@MainActivity,"hongbao","")
        var today = LocalDate.now().toString("yyyy-MM-dd")
        if(lastTime != today){
            main_hongbao_layout.visibility = View.VISIBLE
            SPUtil.putStringValue(this@MainActivity,"hongbao",LocalDate.now().toString("yyyy-MM-dd"))
        }
        main_hongbao_layout.setOnClickListener {
        }
        main_hongbao_click.setOnClickListener {
            var intent = Intent(this,WebActivity::class.java)
            intent.putExtra("url",Consts.URL+"/wechat/page131")
            startActivity(intent)
            main_hongbao_layout.visibility = View.GONE
        }
        main_hongbao_close.setOnClickListener {
            main_hongbao_layout.visibility = View.GONE
        }

    }

    private var mkeyTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if(fabu_frame.visibility == View.VISIBLE){
                fabu_frame.visibility = View.GONE;
                return true
            }
            if(titleStr == "首页" || fragframe.visibility ==View.VISIBLE){
                if((System.currentTimeMillis() - mkeyTime) > 2000){
                    mkeyTime = System.currentTimeMillis()
                    showMessage("再按一次退出程序")
                    return true
                }
                return super.onKeyDown(keyCode, event)
            }

            if(titleStr == "约单" || titleStr == "消息" || titleStr == "我的"){
                presenter.loadNewPage("1")
                main_footbar.visibility = View.VISIBLE
                main_top_bar.visibility = View.VISIBLE
                fragframe.visibility =View.VISIBLE
                //webframe.visibility = View.GONE
                main_include.visibility = View.GONE
                main_rb_shouye.isChecked = true
                return true
            }

            if(webview.canGoBack()){
                webview.goBack()
                return true
            }
            if((System.currentTimeMillis() - mkeyTime) > 2000){
                mkeyTime = System.currentTimeMillis()
                showMessage("再按一次退出程序")
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onLocationChanged(p0: AMapLocation?) {
        if(p0?.errorCode ==0){
            var isShowLocated = SPUtil.getBooleanValue(this,"isShowLocated")
            //isShowLocated = false
            if(!isShowLocated){
                //未显示过定位城市弹框
                showLocationDialog(p0.city)
                SPUtil.putBooleanValue(this,"isShowLocated",true)
                SPUtil.putStringValue(this,"showLocatedDateString",LocalDate.now().toString("yyyy-MM-dd"))
            }else{
                var lastTime:String = SPUtil.getStringValue(this,"showLocatedDateString")
                if(!lastTime.isEmpty()){
                    if(LocalDate.now().toString("yyyy-MM-dd") != lastTime){
                        SPUtil.putStringValue(this,"showLocatedDateString",LocalDate.now().toString("yyyy-MM-dd"))
                        if(p0.city != main_location.text){//如果定位城市和显示城市不同才弹
                            showLocationDialog(p0.city)
                        }
                    }
                }else{
                    showLocationDialog(p0.city)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(titleStr == "我的" && isOptLogin){
            isOptLogin = false
            presenter.loadNewPage("4")
        }

        if(SPUtil.getBooleanValue(this,"isApkUpdated")){
            isLogined = false
            SPUtil.putBooleanValue(this,"isApkUpdated",false)
        }
        checkLogin()
    }

    override fun notifyHasNewMsgs(hasNewMsg: Boolean) {
        if(hasNewMsg){
            main_msg_biaoji.visibility = View.VISIBLE
            main_menu_biaoji.visibility = View.VISIBLE
            menuPopupWindow?.setNewMsgVisible(View.VISIBLE)
        }else{
            main_msg_biaoji.visibility = View.GONE
            main_menu_biaoji.visibility = View.GONE
            menuPopupWindow?.setNewMsgVisible(View.GONE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isLogined = false
        webview.removeAllViews()
        webview.destroy()
        presenter.destroy()
    }

    override fun wxPayResult(content: Int) {
        super.wxPayResult(content)
        when (content) {
            0 ->{
                showMessage("支付成功")
                presenter.delWxPayResult()
            }
            -1 -> showMessage("支付失败")
            -2 -> showMessage("支付取消")
        }
    }

    override fun showShareGold() {
        main_share_goldimg.visibility = View.VISIBLE
        main_share_goldimg.scaleX = 0.5f
        main_share_goldimg.scaleY = 0.5f
        main_share_goldimg.animate().scaleX(1f).scaleY(1f)
                .setInterpolator(BounceInterpolator())
                .setDuration(300)
                .start()
        val coundDownTimer = object: CountDownTimer(3000,1000){
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
                main_share_goldimg.visibility = View.GONE
            }
        }
        coundDownTimer.start()
    }

    override fun notifyCityChanged(city: String?, needReload: Boolean) {
        main_location.text = city
        if(needReload){
            mainFragment?.reloadData()
        }
    }

    fun goLogin() {
        this@MainActivity.startActivityForResult(
                Intent(this@MainActivity,LoginActivity::class.java),
                Consts.REQUEST_LOGIN)
    }

    fun showFabuFrame(){
        fabu_frame.visibility = View.VISIBLE
        fabu_frame.alpha = 0f
        fabu_frame.visibility = View.VISIBLE
        fabu_frame.animate().alpha(1f).setDuration(300).start()
        main_fabu_close.setOnClickListener {
            fabu_frame.visibility = View.GONE
        }
        fabu_frame.setOnClickListener {
            fabu_frame.visibility = View.GONE
        }

        fabu_syq.setOnClickListener {
            presenter.loadNewPage("5")
            fragframe.visibility =View.GONE
            webframe.visibility = View.VISIBLE
            fabu_frame.visibility = View.GONE
        }

        fabu_wyrz.setOnClickListener {
            fabu_frame.visibility = View.GONE
            var intent = Intent(this@MainActivity, WebActivity::class.java)
            intent.putExtra("url",Consts.URL+"wechat/static76")
            startActivity(intent)
        }
    }

    @JavascriptInterface
    fun getLocation(){

        val intent = Intent(this@MainActivity, PoiKeywordSearchActivity::class.java)
        this@MainActivity.startActivityForResult(intent, Consts.REQUEST_CODE_GOMAP)
    }

    @JavascriptInterface
    fun wxPay(json:String){
        Log.i("wxPay",json)
        this@MainActivity.presenter.callWX(json)
    }

    @JavascriptInterface
    fun share(title:String,describe:String,imageUrl:String){
        runOnUiThread {
            openShareWindow(this@MainActivity, root, webview.url, title, describe,imageUrl)
        }
    }

    @JavascriptInterface
    fun share(title:String,describe:String,imageUrl:String,webUrl:String){
        runOnUiThread {
            if(webUrl.startsWith("http")){
                openShareWindow(this@MainActivity, root, webUrl, title, describe,imageUrl,object : UMShareListener{
                    override fun onResult(p0: SHARE_MEDIA?) {
                        presenter.userShareGoldEnvelopes()
                    }

                    override fun onCancel(p0: SHARE_MEDIA?) {
                    }

                    override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                        showError(p1?.message)
                    }

                    override fun onStart(p0: SHARE_MEDIA?) {
                    }

                })
            }else{
                openShareWindow(this@MainActivity, root, webview.url, title, describe,imageUrl,object : UMShareListener{
                    override fun onResult(p0: SHARE_MEDIA?) {
                        showMessage("分享成功")
                        presenter.webShareResult(webUrl)
                    }

                    override fun onCancel(p0: SHARE_MEDIA?) {
                        shareResult(false,"取消分享")
                    }

                    override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                        showError(p1?.message)
                    }

                    override fun onStart(p0: SHARE_MEDIA?) {
                    }

                })
            }

        }
    }

    @JavascriptInterface
    fun shareMore(title:String,describe:String,imageUrl:String,type:String,json:String){ //type:0 普通 1 有二维码  json:二维码信息集
        runOnUiThread {
            openShareWindow(this@MainActivity,root,webview.url,title,describe,imageUrl,type,json,null)
        }
    }

    @JavascriptInterface
    fun dial(phoneStr:String){
        phone =phoneStr
        addPermission(101,Manifest.permission.CALL_PHONE)
    }

    @JavascriptInterface
    fun showFooter(index:String){
        runOnUiThread {
            main_footbar.visibility = View.VISIBLE
            when(index){
                "1" -> main_rb_shouye.isChecked = true
                "2" -> main_rb_yuedan.isChecked = true
                "3" -> main_rb_msg.isChecked = true
                "4" -> main_rb_mine.isChecked = true
            }
        }
    }

    @JavascriptInterface
    fun hideFooter(str:String){
        runOnUiThread {
            main_footbar.visibility = View.GONE
        }
    }

    @JavascriptInterface
    fun handlerUrl(type:String,option:String){
        when(type){
            "1" -> {//具体操作
                when(option){
                    "login" ->{ //跳转登录
                        /* isOptLogin = true
                         this@MainActivity.startActivityForResult(
                                 Intent(this@MainActivity,LoginActivity::class.java),
                                 Consts.REQUEST_LOGIN)*/
                    }
                    "loginout" -> { //退出登录
                        SPUtil.removeUserBean(this@MainActivity)
                        runOnUiThread {
                            Consts.isLogined = false
                            Consts.user = null
                            this@MainActivity.isLogined = false
                            this@MainActivity.checkLogin()
                        }
                    }
                }
            }
            "0" -> {//url

            }
        }
    }

    @JavascriptInterface
    fun msgCount(count:String){
        presenter.dealMsgCount(count)
    }

    @JavascriptInterface
    fun changeCity(city:String,code:String){
        runOnUiThread {
            main_location.text = city
            var localCode = SPUtil.getStringValue(this@MainActivity,"cityCode","")
            if(code != localCode) {
                SPUtil.putStringValue(this@MainActivity,"city",city)
                SPUtil.putStringValue(this@MainActivity, "cityCode", code)
                mainFragment?.reloadData()
            }
        }
    }

    @JavascriptInterface
    fun reloadData(){
        runOnUiThread{
            mainFragment?.reloadData()
        }
    }

    @JavascriptInterface
    fun hasNewMsg(isShow:String){
        runOnUiThread {
            notifyHasNewMsgs(isShow == "true")
        }

    }
    @JavascriptInterface
    fun myWallet(){
        runOnUiThread {
            if(Consts.isLogined){
                this@MainActivity.startActivityForResult(
                        Intent(this@MainActivity,WalletActivity::class.java),
                        Consts.REQUEST_LOGIN)
            }else{
                isOptLogin = true
                this@MainActivity.startActivityForResult(
                        Intent(this@MainActivity,LoginActivity::class.java),
                        Consts.REQUEST_LOGIN)
            }
        }
    }

    @JavascriptInterface
    fun myOperate(opt: String) {
        when(opt){
            "addr"->{
                startActivity(Intent(this@MainActivity,AddrListActivity::class.java))
            }
            "userinfo" ->{
                startActivityForResult(
                        Intent(this@MainActivity,ChangeUserinfoActivity::class.java),
                        Consts.REQUEST_CHANGEUSERINFO)
            }
            "goBack" ->{
                runOnUiThread {
                    if(webview.canGoBack()){
                        webview.goBack()
                    }
                }
            }
        }
    }
}
