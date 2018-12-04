package com.goodthings.app.activity.selectpic

import android.Manifest
import android.animation.Animator
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import com.goodthings.app.R
import com.goodthings.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_select_pic.*

class SelectPicActivity : BaseActivity<SelectPicContract.SelectPicView,SelectPicContract.SelectPicPresenter>(), SelectPicContract.SelectPicView {

    override var presenter: SelectPicContract.SelectPicPresenter = SelectPicPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_pic)
        val layoutParams = window.attributes
        layoutParams.gravity = Gravity.BOTTOM
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.decorView.setPadding(0, 0, 0, 0)
        window.attributes = layoutParams
        initView()
    }

    private fun initView() {
        openSelectLayout()
        empty_view.setOnClickListener {
            closeSelectLayout()
        }
        select_cancel.setOnClickListener{
            closeSelectLayout()
        }
        select_take_photo.setOnClickListener{
            addPermission(100, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        select_pick_photo.setOnClickListener{
            presenter.takePhoto(false)
        }
    }

    override fun addPermissionSuccess(requestCode: Int) {
        super.addPermissionSuccess(requestCode)
        when(requestCode){
            100 -> presenter.takePhoto(true)
        }
    }

    fun openSelectLayout(){
        select_layout.translationY = 300f
        select_layout.animate()
                .translationY(0f)
                .setDuration(300)
                .start()
    }

    fun closeSelectLayout(){
        backgroundAlpha(1f)
        select_layout.animate()
                .translationY(500f)
                .setDuration(100)
                .setListener(object: Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator?) {
                    }

                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        close(null)
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }
                })
                .start()
    }

    fun close(uri: Uri?) {
        if(uri != null){
            val intent=Intent()
            intent.data = uri
            setResult(Activity.RESULT_OK,intent)
        }
        this@SelectPicActivity.finish()
        overridePendingTransition(0,R.anim.out_anim_alpha)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            presenter.handleActivityResult(requestCode,data)
        }

    }

    override fun getPicSuccess(arrayOf: Array<Uri>) {
        close(arrayOf[0])
    }

    override fun getPicError() {
        showMessage("获取图片失败")
        close(null)
    }
    /**
    * 设置添加屏幕的背景透明度
    */
    fun backgroundAlpha(bgAlpha: Float) {
        val lp = window.attributes
        lp.alpha = bgAlpha
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.attributes = lp
    }
}
