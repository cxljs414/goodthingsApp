package com.goodthings.app.activity.addaddr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import com.goodthings.app.R
import com.goodthings.app.activity.city.CityActivity
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.AddressBean
import kotlinx.android.synthetic.main.activity_add_addr.*
import kotlinx.android.synthetic.main.top_bar.*

class AddAddrActivity :
        BaseActivity<AddAddrContract.AddAddrView,AddAddrContract.AddAddrPresenter>(),
        AddAddrContract.AddAddrView{

    override var presenter: AddAddrContract.AddAddrPresenter = AddAddrPresenterImpl()
    private var cityCode = "110000"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_addr)
        topback.setOnClickListener { onBackPressed() }
        toptitle.text = "新增联系方式和地址"
        if(intent.hasExtra("addrId")){
            presenter.getAddrInfo(intent.getIntExtra("addrId",0))
        }

        addaddr_save.setOnClickListener {
            if(addaddr_name.text.toString().isEmpty()){
                showMessage("请填写联系人")
                return@setOnClickListener
            }
            if(addaddr_phone.text.toString().isEmpty()){
                showMessage("请填写联系电话")
                return@setOnClickListener
            }

            if(!Regex("^1[3|4|5|8][0-9]\\d{4,8}\$").matches(addaddr_phone.text)){
                showMessage("请填写正确的手机号码")
                return@setOnClickListener
            }

            if(addaddr_addr.text.toString().isEmpty()){
                showMessage("请填写联系地址")
                return@setOnClickListener
            }
            if(addaddr_detail_addr.text.toString().isEmpty()){
                showMessage("请填写详细地址")
                return@setOnClickListener
            }

            presenter.addAddress(
                    addaddr_name.text.toString(),
                    addaddr_phone.text.toString(),
                    addaddr_addr.text.toString(),
                    cityCode,
                    addaddr_detail_addr.text.toString(),
                    addaddr_default.isChecked)
        }

        addaddr_addr.setOnClickListener {
            var intent= Intent(this@AddAddrActivity,CityActivity::class.java)
            intent.putExtra("needDetail",true)
            startActivityForResult(intent,Consts.REQUEST_CITY)
        }
    }

    override fun initResult() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Consts.REQUEST_CITY && resultCode == Activity.RESULT_OK){
            if(data != null){
                var city= data.getStringExtra("cityName")
                cityCode = data.getStringExtra("cityCode")
                var areaName= data.getStringExtra("areaName")
                addaddr_addr.text = city+areaName
            }
        }
    }

    override fun showAddrInfo(it: AddressBean?) {
        addaddr_name.text = Editable.Factory.getInstance().newEditable(it?.name)
        addaddr_phone.text = Editable.Factory.getInstance().newEditable(it?.phone)
        addaddr_addr.text = Editable.Factory.getInstance().newEditable(it?.city)
        addaddr_detail_addr.text = Editable.Factory.getInstance().newEditable(it?.addr)
        addaddr_default.isChecked = it?.is_default == 1
    }
}
