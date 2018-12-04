package com.goodthings.app.activity.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.bumptech.glide.Glide
import com.goodthings.app.R
import com.goodthings.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.top_bar.*

class LoginActivity : BaseActivity<LoginContract.LoginView,LoginContract.LoginPresenter>(),LoginContract.LoginView {

    override var presenter: LoginContract.LoginPresenter = LoginPresenterImpl()
    private var isFromHongBao= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        isFromHongBao = intent.getBooleanExtra("isFromHongBao",false)
        presenter.setFromHongbao(isFromHongBao)
    }

    private fun initView() {
        toptitle.text = "登录"
        topback.setOnClickListener {
            finish()
        }
        msg_phone_et.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isNotEmpty()){
                    msg_phone_del.visibility = View.VISIBLE
                    msg_commit_bt.isEnabled = true
                    if(s.toString().length == 11){
                        presenter.queryUserByPhone(s.toString())
                    }
                }else{
                    msg_phone_del.visibility = View.GONE
                }
            }
        })

        msg_phone_del.setOnClickListener {
            msg_phone_et.text = Editable.Factory().newEditable("")
            msg_commit_bt.isEnabled = false
            headview.setImageResource(R.mipmap.touxiang)
            nick_tv.text = String.format(resources.getString(R.string.login_nick),"")
        }

        psd_phone_et.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().length > 0){
                    psd_phone_del.visibility = View.VISIBLE
                    if(s.toString().length == 11){
                        presenter.queryUserByPhone(s.toString())
                    }
                }else{
                    psd_phone_del.visibility = View.GONE
                }
            }
        })

        psd_phone_del.setOnClickListener {
            psd_phone_et.text = Editable.Factory().newEditable("")
            headview.setImageResource(R.mipmap.touxiang)
            nick_tv.text = String.format(resources.getString(R.string.login_nick),"")
        }

        psd_psd_et.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().length > 0){
                    psd_psd_del.visibility = View.VISIBLE
                }else{
                    psd_psd_del.visibility = View.GONE
                }
            }
        })

        psd_psd_del.setOnClickListener {
            psd_psd_et.text = Editable.Factory().newEditable("")
        }

        login_way_ck.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                login_way_text.text = getString(R.string.login_msg)
                msglayout.visibility = View.GONE
                psdlayout.visibility = View.VISIBLE
            }else{
                login_way_text.text = getString(R.string.login_psd)
                psdlayout.visibility = View.GONE
                msglayout.visibility = View.VISIBLE
            }
        }

        msg_commit_bt.setOnClickListener {
            val phone:String = msg_phone_et.text.toString()
            if(phone.length == 11 && Regex("^1[0-9][0-9]\\d{4,8}\$").matches(phone)){
                presenter.loginByPhone(phone)
            }else{
                showMessage("请填写正确的手机号码")
            }
        }

        psd_commit.setOnClickListener {
            val phone:String = psd_phone_et.text.toString()
            if(phone.length != 11 || !Regex("^1[0-9][0-9]\\d{4,8}\$").matches(phone)){
                showMessage("请填写正确的手机号码")
                return@setOnClickListener
            }
            val psd = psd_psd_et.text.toString()
            if(psd == ""){
                showMessage("请输入密码")
                return@setOnClickListener
            }

            presenter.loginbyPsd(phone,psd)
        }

        psd_forgetpsd.setOnClickListener {
            val phone:String = psd_phone_et.text.toString()
            if(phone.length != 11 || !Regex("^1[3|4|5|8][0-9]\\d{4,8}\$").matches(phone)){
                showMessage("请填写正确的手机号码")
                return@setOnClickListener
            }

            presenter.forgetPassword(phone)
        }

        service_xieyi.setOnClickListener {
            //服务协议
        }

    }

    override fun setShowUser(nickname: String?, headUrl: String?) {
        nick_tv.text = String.format(resources.getString(R.string.login_nick),nickname)
        headUrl?.let {
            Glide.with(this).load(headUrl).into(headview)
        }
    }

    override fun goBack(id: Int, msg: String?) {
        if(msg.isNullOrEmpty()){
            var intent= Intent()
            intent.putExtra("userId","$id")
            intent.putExtra("isFromHongBao",isFromHongBao)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }else{
            login_firstregist_hb_money.text = "${msg}元"
            login_firstregist_hboutlayout.visibility = View.VISIBLE
            login_firstregist_hboutlayout.setOnClickListener {
                var intent= Intent()
                intent.putExtra("userId","$id")
                intent.putExtra("isFromHongBao",isFromHongBao)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
            login_firstregist_hblayout.setOnClickListener {  }
            login_firstregist_hbclose.setOnClickListener {
                var intent= Intent()
                intent.putExtra("userId","$id")
                intent.putExtra("isFromHongBao",isFromHongBao)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }


    }

}
