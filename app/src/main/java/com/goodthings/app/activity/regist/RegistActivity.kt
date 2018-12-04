package com.goodthings.app.activity.regist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.bumptech.glide.Glide
import com.goodthings.app.R
import com.goodthings.app.activity.selectpic.SelectPicActivity
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.application.Consts
import kotlinx.android.synthetic.main.activity_regist.*
import kotlinx.android.synthetic.main.top_bar.*

class RegistActivity : BaseActivity<RegistContract.RegistView,RegistContract.RegistPresenter>(), RegistContract.RegistView {

    override var presenter: RegistContract.RegistPresenter = RegistPresenter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist)
        presenter.start()
        initView()

    }

    private fun initView() {
        toptitle.text = resources.getString(R.string.regist_personinfo)
        topback.setOnClickListener {
            finish()
        }

        regist_headview.setOnClickListener {
            startActivityForResult(Intent(this@RegistActivity,SelectPicActivity::class.java), Consts.REQUEST_SELECT_PIC)
            overridePendingTransition(R.anim.enter_anim_alpha,0)
        }

        nickname_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(nickname: Editable?) {
                if(nickname.toString().isNotEmpty()){
                    nickanme_del.visibility = View.VISIBLE
                }else{
                    nickanme_del.visibility = View.GONE
                }
            }
        })

        nickanme_del.setOnClickListener {
            nickname_et.text = Editable.Factory().newEditable("")
        }

        sex_rbgroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.sex_nan -> presenter.setSex("2")
                R.id.sex_nv -> presenter.setSex("3")
            }
        }

        age_radiogroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.age_1 -> presenter.setAge("1")
                R.id.age_2 -> presenter.setAge("2")
                R.id.age_3 -> presenter.setAge("3")
                R.id.age_4 -> presenter.setAge("4")
                R.id.age_5 -> presenter.setAge("5")
            }
        }

        regist_commit.setOnClickListener {
            presenter.commitRegist(nickname_et.text.toString())
        }
    }

    override fun showHeadPic(data: String) {
        Glide.with(this).load(Consts.IMAGEURL+data).into(regist_headview)
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
}
