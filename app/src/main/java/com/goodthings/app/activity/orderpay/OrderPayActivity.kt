package com.goodthings.app.activity.orderpay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.goodthings.app.R
import com.goodthings.app.activity.addaddr.AddAddrActivity
import com.goodthings.app.activity.addrlist.AddrListActivity
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.AddressBean
import com.goodthings.app.bean.CrowdOrderBean
import com.goodthings.app.bean.ProdCrowdBean
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_order_pay.*
import kotlinx.android.synthetic.main.top_bar.*


class OrderPayActivity :
        BaseActivity<OrderPayContract.OrderPayView,OrderPayContract.OrderPayPresenter>(),
        OrderPayContract.OrderPayView{

    override var presenter: OrderPayContract.OrderPayPresenter = OrderPayPresenterImpl()
    private var isHasDefaultAddr= false
    private var adrId:Int = -1
    private var isAddOrdered = false
    private val sato0 = ScaleAnimation(1f, 0f, 1f, 1f,
            Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f)

    private val sato1 = ScaleAnimation(0f, 1f, 1f, 1f,
            Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_pay)
        topback.setOnClickListener {
            if(isAddOrdered){
                setResult(Activity.RESULT_OK)
            }
            onBackPressed()
        }
        toptitle.text = "订单支付"
        presenter.startLoadData()

    }

    private fun goResult() {
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    override fun showCrowdOrderContent(orderBean: CrowdOrderBean?) {
        orderpay_title.text = orderBean?.title
        orderpay_count.text = "x ${orderBean?.count}"
        orderpay_realpay.text = "￥${orderBean?.price!!*orderBean.count}元"
        showComContent(orderBean.pId,orderBean.title,orderBean.intro,orderBean.cover_pic,orderBean.price,orderBean.count,orderBean.eId)
        isHasDefaultAddr = true
        orderpay_address_yes.visibility = View.VISIBLE
        orderpay_address_no.visibility = View.GONE
        orderpay_address_name.text = orderBean?.name
        orderpay_address_phone.text = orderBean?.phone
        orderpay_address_detail.text = orderBean?.addr
    }

    override fun showCrowdContent(prod: ProdCrowdBean?,count:Int) {
        orderpay_title.text = prod?.title
        orderpay_count.text = "x $count"
        orderpay_realpay.text = "￥${prod?.price!!*count}元"
        showComContent(prod.pId,prod.title,prod.intro,prod.cover_pic,prod.price,count,prod.eid)
    }

    private fun showComContent(prodId:Int,title:String,intro:String,coverPic:String,price:Double,count:Int,eid:Int){
        orderpay_address_yes.setOnClickListener {
            selectAddr()
        }

        orderpay_address_no.setOnClickListener {
            goAddr()
        }

        orderpay_commit.setOnClickListener {
            if(isHasDefaultAddr){
                presenter.addOrder(prodId,price,count,eid,adrId)
            }else{
                goAddr()
            }
        }

        orderpay_dialog_submmit.setOnClickListener {
            var url = Consts.URL + "crowdinfo/unauth/share/" + prodId
            openShareWindow(this, orderpay_root, url, title, intro, coverPic, object : UMShareListener {
                override fun onCancel(p0: SHARE_MEDIA?) {

                }

                override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                }

                override fun onStart(p0: SHARE_MEDIA?) {
                }

                override fun onResult(p0: SHARE_MEDIA?) {
                    presenter.shareCrowdRedEnvelopes()
                }
            })
        }

        orderpay_red_sub2_share.setOnClickListener {
            var url = Consts.URL + "crowdinfo/unauth/share/" +  prodId
            openShareWindow(this, orderpay_root, url, title, intro, coverPic, object : UMShareListener {
                override fun onCancel(p0: SHARE_MEDIA?) {

                }

                override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                }

                override fun onStart(p0: SHARE_MEDIA?) {
                }

                override fun onResult(p0: SHARE_MEDIA?) {
                    presenter.shareCrowdEnvelopes()
                }
            })
        }

        orderpay_red_open.setOnClickListener {
            orderpay_red_root_sub1.startAnimation(sato0)
        }
        orderpay_red_sub2_close.setOnClickListener {
            orderpay_red_root.visibility = View.GONE
            goResult()
        }

        sato0.duration = 300
        sato1.duration = 300
        sato0.setAnimationListener(object:Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                orderpay_red_root_sub1.visibility = View.GONE
                orderpay_red_root_sub2.visibility = View.VISIBLE
                orderpay_red_root_sub2.startAnimation(sato1)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })


        orderpay_dialog_root.setOnClickListener {  }
        orderpay_dialog_close.setOnClickListener {
            orderpay_dialog_root.visibility = View.GONE
            goResult()
        }
        orderpay_orderdetail.setOnClickListener {
            presenter.goOrderDetail()
        }
        orderpay_red_rule.setOnClickListener {
            orderpay_contract_root.visibility = View.VISIBLE
        }
        orderpay_contract_root.setOnClickListener {
            orderpay_contract_root.visibility = View.GONE
        }
        orderpay_contract_close.setOnClickListener {
            orderpay_contract_root.visibility = View.GONE
        }

        orderpay_contract_content.text = Html.fromHtml("<div id=\"u540_text\" class=\"text \">\n" +
                "            <p>1.第一步，微信端点击右上角“...”，选择“发送给朋友”</br>APP在弹窗中选择一个分享渠道分享给朋友</p ><p>2.第二步，分享成功后，点击领取红包，将随机获得1-100元红包奖励</p ><p>3.第三步，得到奖励可以在好事发生APP或者微信版里，“个人”-“我的钱包”中的金币账户看到，满50元可提</p ><p>每人每天只能领取一次该活动红包好事发生拥有本活动最终解释权</p ></div>")

    }

    private fun selectAddr() {
        var intent = Intent(this@OrderPayActivity, AddrListActivity::class.java)
        intent.putExtra("isSelectAddr",true)
        startActivityForResult(intent,Consts.REQUEST_ADDRLIST)
    }


    private fun goAddr() {
        var intent = Intent(this@OrderPayActivity, AddAddrActivity::class.java)
        intent.putExtra("isAddDefault",true)
        startActivityForResult(intent,Consts.REQUEST_ADDADDR)
    }

    override fun showDefaultAddress(it: AddressBean?) {
        isHasDefaultAddr = true
        this.adrId = it?.id!!
        orderpay_address_yes.visibility = View.VISIBLE
        orderpay_address_no.visibility = View.GONE
        orderpay_address_name.text = it?.name
        orderpay_address_phone.text = it?.phone
        orderpay_address_detail.text = it?.addr
    }

    override fun wxPayResult(content: Int) {
        super.wxPayResult(content)
        when (content) {
            0 ->{
                showMessage("支付成功")
                showShareDialog()
            }
            -1 -> showMessage("支付失败")
            -2 -> showMessage("支付取消")
        }
    }

    override fun setAddOrder(addOrder: Boolean) {
        isAddOrdered = true
        showMessage("已生成订单")
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
                    orderpay_address_yes.visibility = View.VISIBLE
                    orderpay_address_no.visibility = View.GONE
                    orderpay_address_name.text = data.getStringExtra("addrName")
                    orderpay_address_phone.text = data.getStringExtra("addrPhone")
                    orderpay_address_detail.text = data.getStringExtra("addrDetail")
                }
            }
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isAddOrdered){
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showShareDialog(){
        orderpay_dialog_root.visibility = View.GONE
        orderpay_dialog_root.alpha = 0f
        orderpay_dialog_root.visibility = View.VISIBLE
        orderpay_dialog_root.animate().alpha(1f).setDuration(300).start()
       }

    override fun showRedDialog(it: String) {
        orderpay_dialog_root.visibility = View.GONE
        orderpay_red_sub2_money.text = "${it}元"
        orderpay_red_root.alpha = 0f
        orderpay_red_root.visibility = View.VISIBLE
        orderpay_red_root.animate().alpha(1f).setDuration(300).start()
        orderpay_red_root.setOnClickListener {  }
        orderpay_red_close.setOnClickListener {
            orderpay_red_root.visibility = View.GONE
            goResult()
        }
    }

    override fun showRedSubMoney(it: String, isFirst: Boolean) {
        if(!isFirst){
            orderpay_dialog_root.visibility = View.GONE
            orderpay_red_root.alpha = 0f
            orderpay_red_root.visibility = View.VISIBLE
            orderpay_red_root.animate().alpha(1f).setDuration(300).start()
            orderpay_red_root.setOnClickListener {  }
            orderpay_red_close.setOnClickListener {
                orderpay_red_root.visibility = View.GONE
                setResult(Activity.RESULT_OK)
                finish()
            }
            orderpay_red_root_sub1.visibility = View.GONE
            orderpay_red_root_sub2.visibility = View.VISIBLE
            orderpay_red_sub2_money.text = "${it}元"
        }else{
            orderpay_red_sub2_money.text = "${it}金币"
        }
        orderpay_red_sub2_title.text = "今天已经领取过了"
        orderpay_red_sub2_share.setBackgroundResource(R.drawable.bg_grey_corner_5)
        orderpay_red_sub2_share.text = "已领取过金币"
        orderpay_red_sub2_share.isEnabled = false

    }
}
