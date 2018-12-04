package com.goodthings.app.activity.splash

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v4.view.ViewPager
import android.text.Html
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.goodthings.app.R
import com.goodthings.app.activity.home.HomeActivity
import com.goodthings.app.activity.main.MainActivity
import com.goodthings.app.adapter.MyVpAdapter
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.GuideBean
import com.goodthings.app.bean.UpdateAppBean
import com.goodthings.app.util.AppUtil
import com.goodthings.app.util.NetworkType
import com.goodthings.app.util.NetworkUtil
import com.goodthings.app.util.SPUtil
import kotlinx.android.synthetic.main.activity_splash.*

/**启动页
 * 功能：
 *  1、判断是否有版本更新
 *  2、显示广告页
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/25
 * 修改内容：
 * 最后修改时间：
 */
class SplashActivity : BaseActivity<SplashContract.SplashView, SplashContract.SplashPresenter>(), SplashContract.SplashView {
    override var presenter: SplashContract.SplashPresenter = SplashPresenter()
    var isGuided = true
    var guide: GuideBean? = null
    var varsionDialog:Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        setContentView(R.layout.activity_splash)
        addPermission(100,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
        progressbar.max = 100
        isGuided = SPUtil.getBooleanValue(this@SplashActivity, "IsGuided")
        val images = intArrayOf(R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3, R.mipmap.guide4)
        viewpager.adapter = MyVpAdapter(this, images)
        viewpager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 3) {
                    gomainlayout.visibility = View.VISIBLE
                    guide_go.visibility = View.GONE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        gomainlayout.setOnClickListener{ }
        gotomain.setOnClickListener {
            toMain()
        }
        guide_go.setOnClickListener {
            toMain()
        }
    }

    override fun addPermissionSuccess(requestCode: Int) {
        super.addPermissionSuccess(requestCode)
        if(requestCode == 100){
            checkNetWork()
        }
    }

    override fun onNetwokChange(isEnable: Boolean) {
        super.onNetwokChange(isEnable)
        if(isEnable){
            presenter.checkVersion()
            //presenter.requestGuide()
        }else{
            val normalDialog = AlertDialog.Builder(this)
            normalDialog.setMessage("网络无法访问，请检查网络连接")
            normalDialog.setCancelable(false)
            normalDialog.setPositiveButton("确定"
            ) { dialog, which ->
                dialog.dismiss()
                startActivityForResult(Intent(Settings.ACTION_SETTINGS),111)
            }
            normalDialog.setNegativeButton("取消"
            ) { dialog, which ->
                dialog.dismiss()
                AppUtil.exitApp()
            }
            // 显示
            normalDialog.show()
        }
    }

    override fun goToMain(it: GuideBean?) {
        //updateCheck()
        guide = it
        if(!isGuided){
            viewpager.visibility = View.VISIBLE
            guide_go.visibility = View.VISIBLE
            SPUtil.putBooleanValue(this@SplashActivity,"IsGuided",true)
        }else{
            toMain()
        }
    }

    private fun toMain(){
        val intent = Intent(this, MainActivity::class.java)
        if(guide != null && guide!!.isforce == 1){
            intent.putExtra("imgurl",guide?.imgurl)
            intent.putExtra("linkurl",guide?.linkurl)
        }
        startActivity(intent)
        finish()
    }

    private fun updateCheck() {
        /*val builder = VersionParams.Builder()
                .setRequestUrl(Consts.SERVER+"facade/unauth/appVersions")
                .setCustomDownloadActivityClass(CustomDialogActivity::class.java)
                .setService(UpAppService::class.java)
                .setForceRedownload(false)
                .setShowNotification(true)
        AllenChecker.startVersionCheck(this@SplashActivity,builder.build())*/
    }

    override fun updateVersion(updateBean: UpdateAppBean?) {
        if(updateBean != null && (updateBean.vercode).toInt() > AppUtil.getVersionCode(applicationContext)){
            showVersionDialog(updateBean)
        }else{
            presenter.requestGuide()
        }
    }

    private fun showVersionDialog(updateBean: UpdateAppBean) {
        var upMsg = updateBean.content
        if(NetworkUtil(this).NET_WORK_TYPE != NetworkType.WIFI){
            upMsg = "<html><p>$upMsg</p></br><font color='red'>当前为移动网络,是否更新？</font></html>"
        }

        var view:View = View.inflate(this,R.layout.dialog_update_version,null)
        var title: TextView = view.findViewById(R.id.dialog_version_title)
        var content:TextView = view.findViewById(R.id.dialog_version_content)
        var cancel: TextView = view.findViewById(R.id.dialog_version_cancel)
        var sure: TextView = view.findViewById(R.id.dialog_version_sure)
        title.text = "发现新版本app ${updateBean.vername} 是否马上更新？"
        content.text = upMsg
        cancel.setOnClickListener {
            if(varsionDialog!= null && varsionDialog!!.isShowing){
                varsionDialog?.dismiss()
                presenter.cancelUpdate(false,updateBean.isforce)
            }
        }
        sure.setOnClickListener {
            if(varsionDialog!= null && varsionDialog!!.isShowing){
                varsionDialog?.dismiss()
                presenter.dealUpdate(updateBean.url,(updateBean.vercode).toInt())
            }
        }
        varsionDialog = null
        varsionDialog = Dialog(this,R.style.Dialog)
        varsionDialog?.setContentView(view)
        varsionDialog?.setCancelable(false)
        varsionDialog?.show()
    }

    override fun updateProgress(progress: Long, total: Long) {
        val per = (progress.toDouble()* 100 / total ).toInt()
        progressbar.progress = per
        progresstv.text = "${per}%"
    }

    override fun hideProgress() {
        progresslayout.visibility = View.GONE
    }

    override fun shoProgress() {
        progresslayout.visibility = View.VISIBLE
    }
}
