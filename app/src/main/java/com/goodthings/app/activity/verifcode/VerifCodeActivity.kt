package com.goodthings.app.activity.verifcode

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.goodthings.app.R
import com.goodthings.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_verif_code.*
import kotlinx.android.synthetic.main.top_bar.*

class VerifCodeActivity : BaseActivity<VerifCodeContract.VerifCodeView,VerifCodeContract.VerifCodePresenter>(),VerifCodeContract.VerifCodeView {

    override var presenter: VerifCodeContract.VerifCodePresenter = VerifCodePresenterImpl()

    val coundDownTimer = object: CountDownTimer(60000,1000){
        override fun onTick(p0: Long) {
            verifcode_tv.text = String.format(resources.getString(R.string.verif_code),(p0/1000).toInt())
            verifcode_tv.isEnabled = false
        }

        override fun onFinish() {
            verifcode_tv.text = resources.getString(R.string.sendcodeagain)
            verifcode_tv.isEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verif_code)
        presenter.start()
        coundDownTimer.start()
    }

    override fun initView(phone: String?, type: Int?) {
        topback.setOnClickListener {
            finish()
        }
        verifcode_phone.text = String.format(resources.getString(R.string.verif_code_phone),phone)
        when(type){
            VerifCodeContract.TYPE_VERIFCODE ->{
                toptitle.text = "登录验证"
            }
            VerifCodeContract.TYPE_FIRSTREGIST ->{
                toptitle.text = "注册"
                verifcode_commit.text = resources.getString(R.string.regist)
                psd_layout.visibility = View.VISIBLE
                verifcode_newpsd_tip.visibility = View.GONE
            }
            VerifCodeContract.TYPE_FORGETPASSWORD ->{
                psd_layout.visibility = View.VISIBLE
                verifcode_input_tip.visibility = View.GONE
                verifcode_commit.text = resources.getString(R.string.saveandlogin)
            }
        }
        verifcode_psd_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().length > 0){
                    verifcode_psd_del.visibility = View.VISIBLE
                }else{
                    verifcode_psd_del.visibility = View.GONE
                }
            }
        })

        verifcode_psd_del.setOnClickListener {
            verifcode_psd_et.text = Editable.Factory().newEditable("")
        }
        verifcode_commit.setOnClickListener {
            if(verifcode_input.editContent.isNullOrEmpty()){
                showMessage("请输入验证码")
                return@setOnClickListener
            }

            if(type != VerifCodeContract.TYPE_VERIFCODE && verifcode_psd_et.text.isEmpty()){
                showMessage("请输入密码")
                return@setOnClickListener
            }

            presenter.commit(verifcode_input.editContent.toString(),verifcode_psd_et.text.toString(),type)
        }

        verifcode_tv.setOnClickListener {
            presenter.sendVerifCode()
            coundDownTimer.start()
        }

        service_phone.setOnClickListener {
            val intenttle = Intent(Intent.ACTION_DIAL, Uri.parse("tel:4000616263"))
            this@VerifCodeActivity.startActivity(intenttle)
        }
    }



}
