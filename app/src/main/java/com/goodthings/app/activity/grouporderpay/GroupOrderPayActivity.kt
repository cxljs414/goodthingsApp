package com.goodthings.app.activity.grouporderpay

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.goodthings.app.R
import com.goodthings.app.activity.addaddr.AddAddrActivity
import com.goodthings.app.activity.addrlist.AddrListActivity
import com.goodthings.app.activity.groupbuyordersdetail.GroupBuyOrderDetailActivity
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.AddressBean
import com.goodthings.app.bean.GroupBuyDetailBean
import com.goodthings.app.bean.GroupOrderDetailBean
import kotlinx.android.synthetic.main.activity_group_order_pay.*
import kotlinx.android.synthetic.main.top_bar.*

class GroupOrderPayActivity :
        BaseActivity<GroupOrderContract.GroupOrderView,GroupOrderContract.GroupOrderPresenter>(),
        GroupOrderContract.GroupOrderView{

    override var presenter: GroupOrderContract.GroupOrderPresenter = GroupOrderPresenterImpl()
    private var isHasDefaultAddr= false
    private var adrId:Int = -1
    private var addOrdered:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_order_pay)
        topback.setOnClickListener {
            onBackPressed()
        }
        toptitle.text = "订单支付"
        presenter.startLoadData()
    }

    override fun showGroupDetailContent(it: GroupBuyDetailBean?, groupCount: Int, alone: Boolean) {
        grouppay_title.text = it?.title
        grouppay_count.text = "x $groupCount"
        if(alone){
            grouppay_realpay.text = "￥${it?.old_price!!*groupCount}元"
            showComContent(it.cId,it.title,"介绍",it.cover_url,it.old_price,groupCount)
        }else{
            grouppay_realpay.text = "￥${it?.price!!*groupCount}元"
            showComContent(it.cId,it.title,"介绍",it.cover_url,it.price,groupCount)
        }


    }

    private fun showComContent(prodId:Int,title:String,intro:String,coverPic:String,price:Double,count:Int){
        grouppay_address_yes.setOnClickListener {
            if(!intent.hasExtra("orderNo")){
                selectAddr()
            }else{
                showMessage("待支付订单收货地址不可更改")
            }
        }

        grouppay_address_no.setOnClickListener {
            goAddr()
        }

        grouppay_commit.setOnClickListener {
            if(isHasDefaultAddr){
                presenter.addOrder(prodId,price,count,adrId)
                /*setResult(Activity.RESULT_OK,intent)
                finish()*/
            }else{
                goAddr()
            }
        }

    }

    override fun showDefaultAddress(it: AddressBean?) {
        isHasDefaultAddr = true
        this.adrId = it?.id!!
        grouppay_address_yes.visibility = View.VISIBLE
        grouppay_address_no.visibility = View.GONE
        grouppay_address_name.text = it?.name
        grouppay_address_phone.text = it?.phone
        grouppay_address_detail.text = it?.addr
    }

    override fun showGroupOrderContent(orderBean: GroupOrderDetailBean?) {
        grouppay_title.text = orderBean?.title
        grouppay_count.text = "x ${orderBean?.buy_num}"
        grouppay_realpay.text = "￥${orderBean?.price!!*orderBean.buy_num}元"
        showComContent(orderBean.id,orderBean.title,"介绍",orderBean.cover_url,orderBean.price,orderBean.buy_num)
        isHasDefaultAddr = true
        grouppay_address_yes.visibility = View.VISIBLE
        grouppay_address_no.visibility = View.GONE
        grouppay_address_name.text = orderBean.name
        grouppay_address_phone.text = orderBean.phone
        grouppay_address_detail.text = orderBean.addr
    }

    private fun selectAddr() {
        var intent = Intent(this@GroupOrderPayActivity, AddrListActivity::class.java)
        intent.putExtra("isSelectAddr",true)
        startActivityForResult(intent,Consts.REQUEST_ADDRLIST)
    }


    private fun goAddr() {
        var intent = Intent(this@GroupOrderPayActivity, AddAddrActivity::class.java)
        intent.putExtra("isAddDefault",true)
        startActivityForResult(intent,Consts.REQUEST_ADDADDR)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Consts.REQUEST_ADDADDR){
                presenter.queryDefaultAddr()
            }
            if(requestCode == Consts.REQUEST_ADDRLIST){
                if(data != null){
                    this.adrId = data.getIntExtra("addrId",0)
                    isHasDefaultAddr = true
                    grouppay_address_yes.visibility = View.VISIBLE
                    grouppay_address_no.visibility = View.GONE
                    grouppay_address_name.text = data.getStringExtra("addrName")
                    grouppay_address_phone.text = data.getStringExtra("addrPhone")
                    grouppay_address_detail.text = data.getStringExtra("addrDetail")
                }
            }
        }

    }

    override fun wxPayResult(content: Int) {
        super.wxPayResult(content)
        when (content) {
            0 ->{
                showMessage("支付成功")
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
            -1 -> showMessage("支付失败")
            -2 -> {
                showMessage("支付取消")
            }
        }
    }

    private var addedOrderNo:String? = null
    private var payDialog:AlertDialog? = null
    override fun setAddOrder(b: Boolean, orderNo: String) {
        showMessage("已生成订单")
        addOrdered = true
        addedOrderNo  = orderNo
        intent.putExtra("addOrdered",addOrdered)
        topback.setOnClickListener {
            if(addOrdered){
                payDialog = AlertDialog.Builder(this)
                        .setTitle("订单已生成，确定放弃付款吗？")
                        .setPositiveButton("暂时放弃") { dialog, which ->
                            var intent= Intent(this@GroupOrderPayActivity,GroupBuyOrderDetailActivity::class.java)
                            intent.putExtra("orderNo",orderNo)
                            intent.putExtra("position",0)
                            startActivity(intent)
                            finish()
                        }
                        .setNegativeButton("继续支付"){ dialog,which ->
                            payDialog?.dismiss()
                        }
                        .setCancelable(false)
                        .create()
                payDialog?.show()
            }else{
                onBackPressed()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(addOrdered){
                payDialog = AlertDialog.Builder(this)
                        .setTitle("订单已生成，确定放弃付款吗？")
                        .setPositiveButton("暂时放弃") { dialog, which ->
                            var intent= Intent(this@GroupOrderPayActivity,GroupBuyOrderDetailActivity::class.java)
                            intent.putExtra("orderNo",addedOrderNo)
                            intent.putExtra("position",0)
                            startActivity(intent)
                            finish()
                        }
                        .setNegativeButton("继续支付"){ dialog,which ->
                            payDialog?.dismiss()
                        }
                        .setCancelable(false)
                        .create()
                payDialog?.show()
            }else{
                onBackPressed()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        payDialog = null
    }
}
