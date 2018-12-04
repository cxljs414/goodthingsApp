package com.goodthings.app.activity.web

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.EditText
import android.widget.Toast
import com.goodthings.app.R
import com.goodthings.app.activity.addrlist.AddrListActivity
import com.goodthings.app.activity.login.LoginActivity
import com.goodthings.app.activity.userinfo.ChangeUserinfoActivity
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.util.SPUtil
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.top_bar.*
import org.jetbrains.anko.runOnUiThread

class WebActivity :
        BaseActivity<WebContract.WebView,WebContract.WebPresenter>(),
        WebContract.WebView{

    override var presenter: WebContract.WebPresenter= WebPresenterImpl()
    private var isReloaded= false
    private var isResumeFromLogin= false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        topback.setOnClickListener {
            if(webView.canGoBack()){
                webView.goBack()
            }else{
                onBackPressed()
            }
        }
        var url= intent.getStringExtra("url")
        if(url!= null) {
            val s = webView.settings
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
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
            webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            webView.requestFocus()
            webView.requestFocusFromTouch()
            webView.loadUrl(url)
            webView.webChromeClient = object :WebChromeClient() {
                override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
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
                    AlertDialog.Builder(this@WebActivity)
                            .setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton("确定"
                            ) { dialog, _ ->
                                dialog.dismiss()
                            }.create()
                            .show()
                    result?.confirm()
                    return true
                }

                override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                    AlertDialog.Builder(this@WebActivity)
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
                    val builder = AlertDialog.Builder(this@WebActivity)
                    builder.setTitle("对话框").setMessage(message)
                    val et = EditText(this@WebActivity)
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

            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {

                    if(intent.getBooleanExtra("isNeedReload",false)&&!isReloaded){
                        isReloaded = true
                        if(url.contains("wechat/toPrdDetail")){
                            webView.loadUrl(intent.getStringExtra("url"))
                        }
                    }

                    if(view.title.isEmpty()||view.title.startsWith("http")){
                        toptitle.text = "好事发生"
                    }else{
                        toptitle.text = view.title
                    }

                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    presenter.retryLogin()
                    if(url?.contains("index")!!){
                        isReloaded = true
                        webView.loadUrl(intent.getStringExtra("url"))
                    }
                }

            }

            webView.addJavascriptInterface(this,"AndroidInterface")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        AndPermission.onRequestPermissionsResult(this,requestCode,permissions,grantResults,object: PermissionListener {
            override fun onSucceed(requestCode: Int) {
                if(requestCode == 101) {
                    val intenttle = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone))
                    this@WebActivity.startActivity(intenttle)
                }
            }

            override fun onFailed(requestCode: Int) {
                Toast.makeText(this@WebActivity,"无此操作权限！",Toast.LENGTH_SHORT).show()
            }

        })
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

    override fun callJs(js: String, callback: ValueCallback<String>?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView!!.evaluateJavascript(js) { value ->
                callback?.onReceiveValue(value)
            }
        } else {
            webView!!.loadUrl(js)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if(webView.url == intent.getStringExtra("url")){
                onBackPressed()
                return true
            }

            if(webView.canGoBack()){
                webView.goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onResume() {
        super.onResume()
        if(isResumeFromLogin){
            isResumeFromLogin = false
            isReloaded = false
            webView.loadUrl(intent.getStringExtra("url"))
        }
    }


    private var phone = ""
    @JavascriptInterface
    fun dial(phoneStr:String){
        phone =phoneStr
        AndPermission.with(this@WebActivity)
                .requestCode(101)
                .permission(Manifest.permission.CALL_PHONE)
                .send()
    }

    @JavascriptInterface
    fun share(title:String){
        if(title == "hb") {
            runOnUiThread {
                openShareWindow(this@WebActivity,
                        web_root, Consts.URL + "sysUser/unauth/countRegist",
                        "给你发红包！点击和我一起领现金，数量有限>>",
                        "发红包啦！最高100元，真的现金哦，可提现。",
                        "665688451905141771.png")
            }
        }
    }

    @JavascriptInterface
    fun wxPay(json:String){
        Log.i("wxPay",json)
        this@WebActivity.presenter.callWX(json)
    }

    @JavascriptInterface
    fun share(title:String,describe:String,imageUrl:String){
        runOnUiThread {
            openShareWindow(this@WebActivity, web_root, webView.url, title, describe,imageUrl)
        }
    }

    @JavascriptInterface
    fun share(title:String,describe:String,imageUrl:String,webUrl:String){
        runOnUiThread {
            if(webUrl.startsWith("http")){
                openShareWindow(this@WebActivity, web_root, webUrl, title, describe,imageUrl,object : UMShareListener {
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
                openShareWindow(this@WebActivity, web_root, webView.url, title, describe,imageUrl,object : UMShareListener {
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
            openShareWindow(this@WebActivity,web_root,webView.url,title,describe,imageUrl,type,json,null)
        }
    }
    @JavascriptInterface
    fun goLastStep(param:String){
        if(webView.canGoBack()){
            webView.goBack()
        }else{
            onBackPressed()
        }
    }

    @JavascriptInterface
    fun myOperate(opt: String) {
        when(opt){
            "addr"->{
                startActivity(Intent(this@WebActivity, AddrListActivity::class.java))
            }
            "userinfo" ->{
                startActivityForResult(
                        Intent(this@WebActivity, ChangeUserinfoActivity::class.java),
                        Consts.REQUEST_CHANGEUSERINFO)
            }
            "goBack" ->{
                runOnUiThread {
                    if(webView.canGoBack()){
                        webView.goBack()
                    }else{
                        onBackPressed()
                    }
                }
            }
            "finish" -> {
                runOnUiThread {
                    onBackPressed()
                }
            }
        }
    }

    @JavascriptInterface
    fun handlerUrl(type:String,option:String){
        when(type){
            "1" -> {//具体操作
                when(option){
                    "login" ->{ //跳转登录
                        isResumeFromLogin = true
                        this@WebActivity.startActivityForResult(
                                Intent(this@WebActivity, LoginActivity::class.java),
                                Consts.REQUEST_LOGIN)
                    }
                    "loginout" -> { //退出登录
                        SPUtil.removeUserBean(this@WebActivity)
                        runOnUiThread {
                            Consts.isLogined = false
                            Consts.user = null
                        }
                    }
                }
            }
            "0" -> {//url

            }
        }
    }

    @JavascriptInterface
    fun handlerOperate(operate:String,param:String){
        when(operate){
            "autologin" -> {//具体操作
                presenter.autoLogin(param)
            }
        }
    }

    @JavascriptInterface
    fun hideFooter(str:String){
    }

}
