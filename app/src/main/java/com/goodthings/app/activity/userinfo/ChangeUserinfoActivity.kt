package com.goodthings.app.activity.userinfo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import com.bumptech.glide.Glide
import com.goodthings.app.R
import com.goodthings.app.activity.selectpic.SelectPicActivity
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_change_userinfo.*
import kotlinx.android.synthetic.main.activity_regist.*
import kotlinx.android.synthetic.main.top_bar.*

class ChangeUserinfoActivity :
        BaseActivity<ChangeUserInfoContract.ChangeUserInfoVew,ChangeUserInfoContract.ChangeUserInfoPresenter>() ,
        ChangeUserInfoContract.ChangeUserInfoVew{

    override var presenter: ChangeUserInfoContract.ChangeUserInfoPresenter=ChangeUserInfoPresenterImpl()
    private var tempHeadImage:String? = null
    private var sexkey:Int = 2
    private var ageKey:Int = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_userinfo)
        toptitle.text = "个人资料"
        topback.setOnClickListener { onBackPressed() }

        initView()
        presenter.isUpdateUserName()
    }

    private fun initView() {
        Glide.with(this).load(Consts.user?.head_url).into(userinfo_headimage)
        userinfo_nickname_et.text = Editable.Factory.getInstance().newEditable(Consts.user?.nickname!!)
        if(Consts.user?.sex_key == 2){
            userinfo_sex_nan.isChecked = true
        }else{
            userinfo_sex_nv.isChecked = true
        }
        sexkey = Consts.user?.sex_key!!
        ageKey = Consts.user?.age_range!!

        when(Consts.user?.age_range){
            1 -> userinfo_age_60.isChecked = true
            2 -> userinfo_age_70.isChecked = true
            3 -> userinfo_age_80.isChecked = true
            4 -> userinfo_age_90.isChecked = true
        }

        userinfo_sex_group.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.userinfo_sex_nan -> sexkey = 2
                R.id.userinfo_sex_nv -> sexkey = 3
            }
        }

        userinfo_age_group.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.userinfo_age_60 -> ageKey = 1
                R.id.userinfo_age_70 -> ageKey = 2
                R.id.userinfo_age_80 -> ageKey = 3
                R.id.userinfo_age_90 -> ageKey = 4
            }
        }

        tempHeadImage = Consts.user?.head_url!!.replace(Consts.IMAGEURL,"")
        userinfo_save.setOnClickListener {
            if(tempHeadImage == null){
                showMessage("请选择头像")
                return@setOnClickListener
            }

            if(userinfo_nickname_et.text.toString().isEmpty()){
                showMessage("请填写昵称")
                return@setOnClickListener
            }

            var intent= Intent()
            intent.putExtra("nickName",userinfo_nickname_et.text.toString())
            intent.putExtra("revisesSex",sexkey)
            intent.putExtra("headUrl",tempHeadImage)
            intent.putExtra("revisesAge",ageKey)
            setResult(Activity.RESULT_OK,intent)
            finish()
            //presenter.updateUserInfo(tempHeadImage!!,userinfo_nickname_et.text.toString(),sexkey,ageKey)
        }

        userinfo_headimage.setOnClickListener {
            startActivityForResult(Intent(this@ChangeUserinfoActivity, SelectPicActivity::class.java), Consts.REQUEST_SELECT_PIC)
            overridePendingTransition(R.anim.enter_anim_alpha,0)
        }
    }
    override fun canEditNickname(it: Boolean?) {
        userinfo_nickname_et.isEnabled = it!!
    }

    override fun updateTempHeadUrl(data: String) {
        tempHeadImage = data
        Glide.with(this).load(Consts.IMAGEURL+data).into(userinfo_headimage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when(requestCode){
            Consts.REQUEST_SELECT_PIC ->{
                if(resultCode == Activity.RESULT_OK){
                    presenter.uploadImage(intent?.data)
                }
            }
        }
    }

    override fun goBack() {
        setResult(Activity.RESULT_OK)
        onBackPressed()
    }
}
