package com.goodthings.app.activity.groupbuyordersdetail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.goodthings.app.R
import com.goodthings.app.activity.addComment.AddCommentActivity
import com.goodthings.app.activity.aftersaledetail.AfterSaleDetailActivity
import com.goodthings.app.activity.applyaftersale.ApplyAfterSaleActivity
import com.goodthings.app.activity.express.ExpressActivity
import com.goodthings.app.activity.groupbuydetail.GroupBuyDetailActivity
import com.goodthings.app.activity.grouporderpay.GroupOrderPayActivity
import com.goodthings.app.activity.orderpay.OrderPayActivity
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.GroupOrderDetailBean
import com.goodthings.app.bean.ShareParamBean
import com.goodthings.app.util.onClick
import com.goodthings.app.util.toast
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_group_buy_detail.*
import kotlinx.android.synthetic.main.activity_group_buy_order_detail.*
import kotlinx.android.synthetic.main.activity_order_pay.*
import kotlinx.android.synthetic.main.top_bar.*
import org.jetbrains.anko.imageResource
import java.text.SimpleDateFormat
import java.util.*

class GroupBuyOrderDetailActivity :
        BaseActivity<GroupBuyOrderDetailContract.GroupBuyOrderDetailView,
                GroupBuyOrderDetailContract.GroupBuyOrderDetailPresenter>(),
        GroupBuyOrderDetailContract.GroupBuyOrderDetailView{

    override var presenter: GroupBuyOrderDetailContract.GroupBuyOrderDetailPresenter=GroupBuyOrderDetailPresenterImpl()
    private var groupAdapter:TagAdapter<String>? = null
    private var timeCountDownTimer:CountDownTimer? = null
    private var payCountDownTimer:CountDownTimer? = null
    private var staticOrderBean:GroupOrderDetailBean?  = null
    private var nextOption:Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_buy_order_detail)
        topback.setOnClickListener {
            intent.putExtra("newStatus",staticOrderBean?.status)
            setResult(Activity.RESULT_OK,intent)
            onBackPressed()
        }
        toptitle.text = "订单详情"
        presenter.startLoadData()
    }

    @SuppressLint("SetTextI18n")
    override fun showGroupOrderContent(orderBean: GroupOrderDetailBean?) {
        staticOrderBean = orderBean
        //物流信息
        if((orderBean?.status == 4 || orderBean?.status == 5) ){
            gporderdetail_wuliu_layout.visibility = View.VISIBLE
            if(orderBean.logis_company.isNullOrEmpty()){
                orderBean.logis_company = "无"
                orderBean.way_bill_no = "无"
            }
            gporderdetail_wuliu_company.text = "快递公司：${orderBean.logis_company}"
            gporderdetail_wuliu_orderno.text = "快递单号：${orderBean.way_bill_no}"
        }else{
            gporderdetail_wuliu_layout.visibility = View.GONE
        }

        //收货地址
        gporderdetail_addrinfo_addr.text = orderBean?.addr
        gporderdetail_addrinfo_name.text = orderBean?.name
        gporderdetail_addrinfo_phone.text = orderBean?.phone

        //商品信息
        gporderdetail_prodinfo_title.text = orderBean?.title
        gporderdetail_prodinfo_intro.text = ""//orderBean?.intro
        Glide.with(this).load(Consts.IMAGEURL+orderBean?.cover_url)
                .into(gporderdetail_prodinfo_coverpic)
        gporderdetail_prodinfo_price.text = "￥${orderBean?.price}"
        gporderdetail_prodinfo_count.text = "数量：${orderBean?.buy_num}件"

        //下单时间
        gporderdetail_createtime_time.text = orderBean?.create_time //SimpleDateFormat("yyyy-MM-dd hh:mm:ss") .format(Date(orderBean?.create_time!!.toLong()))

        //订单号
        gporderdetail_orderno.text = orderBean?.order_no

        //商品总额
        gporderdetail_prodall.text = "￥${orderBean?.price!!*orderBean.buy_num!!}"

        //运费
        gporderdetail_sendfee.text = "￥0"

        //按钮
        when(orderBean.status){
            0->{
                gporderdetail_opt1_layou.visibility = View.VISIBLE
                gporderdetail_opt4_layou.visibility = View.GONE
                gporderdetail_opt4_suretake.visibility = View.GONE
                gporderdetail_opt4_addcomment.visibility = View.GONE
                gporderdetail_opt4_addcommented.visibility = View.GONE
            }
            1,2,3,7,8,9->{
                gporderdetail_opt1_layou.visibility = View.GONE
                gporderdetail_opt4_layou.visibility = View.GONE
                gporderdetail_opt4_suretake.visibility = View.GONE
                gporderdetail_opt4_addcomment.visibility = View.GONE
                gporderdetail_opt4_addcommented.visibility = View.GONE
            }
            4->{
                gporderdetail_opt1_layou.visibility = View.GONE
                gporderdetail_opt4_layou.visibility = View.VISIBLE
                gporderdetail_opt4_suretake.visibility = View.VISIBLE
                gporderdetail_opt4_addcomment.visibility = View.GONE
                gporderdetail_opt4_addcommented.visibility = View.GONE
            }
            5->{
                gporderdetail_opt1_layou.visibility = View.GONE
                gporderdetail_opt4_layou.visibility = View.VISIBLE
                gporderdetail_opt4_suretake.visibility = View.GONE
                gporderdetail_opt4_addcomment.visibility = View.VISIBLE
                gporderdetail_opt4_addcommented.visibility = View.GONE
            }
            6->{
                gporderdetail_opt1_layou.visibility = View.GONE
                gporderdetail_opt4_layou.visibility = View.VISIBLE
                gporderdetail_opt4_suretake.visibility = View.GONE
                gporderdetail_opt4_addcomment.visibility = View.GONE
                gporderdetail_opt4_addcommented.visibility = View.VISIBLE
            }
        }

        //售后按钮
        if(orderBean.status >=4) {
            if (orderBean.is_shou == 0){
                gporderdetail_opt1_sqsh.visibility = View.VISIBLE
                gporderdetail_opt1_shjd.visibility = View.GONE
            }else{
                gporderdetail_opt1_sqsh.visibility = View.GONE
                gporderdetail_opt1_shjd.visibility = View.VISIBLE
            }
        }

        //状态步骤图
        when(orderBean.status){
            1,2->gporderdetail_step.imageResource = R.mipmap.group_order1
            3->gporderdetail_step.imageResource = R.mipmap.group_order2
            4->gporderdetail_step.imageResource = R.mipmap.grouporder3
            5->gporderdetail_step.imageResource = R.mipmap.grouporder4
            else->gporderdetail_step.imageResource = R.mipmap.group_order1
        }
        var strlist:MutableList<String> = ArrayList()
        orderBean.head_url.forEach {
            strlist.add(it)
        }
        groupAdapter = object :TagAdapter<String>(strlist) {
            override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                val view = layoutInflater.inflate(R.layout.iv_grouporder, gporderdetail_group_tagflow, false)
                var circleImageView = view.findViewById<CircleImageView>(R.id.iv_grouporder_image)
                Glide.with(this@GroupBuyOrderDetailActivity)
                        .load(Consts.IMAGEURL+t)
                        .into(circleImageView)
                var tz = view.findViewById<TextView>(R.id.iv_grouporder_tz)
                if(position == 0){
                    tz.visibility = View.VISIBLE
                }else{
                    tz.visibility = View.GONE
                }
                return view
            }

        }
        gporderdetail_group_tagflow.adapter = groupAdapter

        when (orderBean.status){
            0,2->{//未支付，已支付
                gporderdetail_state1_top.visibility = View.VISIBLE
                gporderdetail_statesuccess_top.visibility = View.GONE
                gporderdetail_statefail_top.visibility = View.GONE
                grouporder_detail_state_image.visibility = View.GONE
                //支付时间
                gporderdetail_state1_count.text = "仅剩${orderBean.person_num - orderBean.head_url.size }个名额，"
                var calendar = Calendar.getInstance()
                calendar.time = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(orderBean.create_time)
                gporderdetail_state1_time.text = covertTime(calendar.timeInMillis+24*60*60*1000 - System.currentTimeMillis())
                timeCountDownTimer = object:CountDownTimer(calendar.timeInMillis+24*60*60*1000 - System.currentTimeMillis(),1000){
                    override fun onFinish() {
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        gporderdetail_state1_time.text = covertTime(millisUntilFinished)
                    }

                }
                timeCountDownTimer?.start()
                presenter.shareParam(orderBean.pid)

            }

            3,4,5,6 -> {
                gporderdetail_state1_top.visibility = View.GONE
                gporderdetail_statesuccess_top.visibility = View.VISIBLE
                gporderdetail_statefail_top.visibility = View.GONE
                grouporder_detail_state_image.visibility = View.VISIBLE
                grouporder_detail_state_image.imageResource = R.mipmap.pintuanchenggong
                if(orderBean.status == 3){
                    gporderdetail_statesuccess_top_tip.visibility = View.VISIBLE
                    gporderdetail_statesuccess_top_tip.text = "商家正在努力发货，请耐心等待"
                }else if(orderBean.status == 4){
                    gporderdetail_statesuccess_top_tip.visibility = View.VISIBLE
                    gporderdetail_statesuccess_top_tip.text = "快递正在赶路，请耐心等待"
                }else{
                    gporderdetail_statesuccess_top_tip.visibility = View.GONE
                }
            }
            1,7,8,9 -> {
                gporderdetail_state1_top.visibility = View.GONE
                gporderdetail_statesuccess_top.visibility = View.GONE
                gporderdetail_statefail_top.visibility = View.VISIBLE
                grouporder_detail_state_image.visibility = View.VISIBLE
                grouporder_detail_state_image.imageResource = R.mipmap.pintuanshibai
            }
        }



        //顶部提示
        if(orderBean.status == 0){
            gporderdetail_toptip.visibility = View.VISIBLE
            var calendar = Calendar.getInstance()
            calendar.time = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(orderBean.create_time)
            var payTime= calendar.timeInMillis+60*60*1000 - System.currentTimeMillis()
            if(payTime < 0){
                orderBean.status = 1
                showGroupOrderContent(orderBean)
            }else{
                payCountDownTimer = object:CountDownTimer(payTime,1000){
                    override fun onTick(millisUntilFinished: Long) {
                        gporderdetail_toptip.text = covertPayTime(millisUntilFinished)
                    }

                    override fun onFinish() {
                        orderBean.status = 1
                        showGroupOrderContent(orderBean)
                    }
                }
                payCountDownTimer?.start()
            }

        }else{
            gporderdetail_toptip.visibility = View.GONE
        }

        //返回商品页面
        gporderdetail_state1_prod.setOnClickListener {
            var intent = Intent(this@GroupBuyOrderDetailActivity, GroupBuyDetailActivity::class.java)
            intent.putExtra("groupBuyId",orderBean.pid)
            startActivity(intent)
        }

        //再次发起拼团
        gporderdetail_statefail_again.setOnClickListener {
            var intent = Intent(this@GroupBuyOrderDetailActivity, GroupBuyDetailActivity::class.java)
            intent.putExtra("groupBuyId",orderBean.pid)
            startActivity(intent)
        }

        //取消订单
        gporderdetail_opt1_cancel.setOnClickListener {
            presenter.cancelOrder(orderBean)
        }
        //支付
        gporderdetail_opt1_pay.setOnClickListener {
            presenter.isComplete(orderBean.tId)
        }

        //确认收货
        gporderdetail_opt4_suretake.setOnClickListener {
            presenter.sureTake()
        }

        topback.setOnClickListener {
            intent.putExtra("newStatus",orderBean.status)
            setResult(Activity.RESULT_OK,intent)
            onBackPressed()
        }

        grouporder_detail_dialog_cancel.setOnClickListener {
            grouporder_detail_dialog_root.visibility = View.GONE
        }

        grouporder_detail_dialog_sure.setOnClickListener {
            grouporder_detail_dialog_root.visibility = View.GONE
            goPay()
        }

        grouporder_detail_dialog_close.setOnClickListener {
            grouporder_detail_dialog_root.visibility = View.GONE
        }

        gporderdetail_opt4_addcomment.onClick {
            var intent= Intent(this@GroupBuyOrderDetailActivity, AddCommentActivity::class.java)
            intent.putExtra("orderNo",orderBean.order_no)
            startActivityForResult(intent,Consts.REQUEST_GPORDER_DETAIL)
        }

        //申请售后
        gporderdetail_opt1_sqsh.onClick {
            nextOption = 1//售后相关
            var intent= Intent(this@GroupBuyOrderDetailActivity, ApplyAfterSaleActivity::class.java)
            intent.putExtra("type",1)
            intent.putExtra("orderNo",orderBean.order_no)
            startActivity(intent)
        }

        //售后进度
        gporderdetail_opt1_shjd.onClick {
            nextOption = 1//售后相关
            var intent = Intent(this@GroupBuyOrderDetailActivity, AfterSaleDetailActivity::class.java)
            intent.putExtra("type",1)
            intent.putExtra("orderNo",orderBean.order_no)
            startActivity(intent)
        }
        //查看物流
        gporderdetail_wuliu_layout.onClick {
            if(orderBean.logis_no.isNullOrEmpty()){
                toast("暂无物流信息")
                return@onClick
            }
            var intent = Intent(this@GroupBuyOrderDetailActivity, ExpressActivity::class.java)
            intent.putExtra("orderNo",orderBean.order_no)
            intent.putExtra("billNo",orderBean.way_bill_no)
            intent.putExtra("shipperCode",orderBean.logis_no)
            intent.putExtra("loginCompany",orderBean.logis_company)
            startActivity(intent)
        }
    }

    override fun notifyShare(shareParamBean: ShareParamBean?) {
        gporderdetail_state1_count.text = "仅剩${shareParamBean?.count}个名额，"
        //邀请好友拼团
        gporderdetail_state1_share.setOnClickListener {
            var url = Consts.URL + "collage/unauth/collageShare?collageId=${shareParamBean?.collageId}&tId=${shareParamBean?.tId}&userId=${Consts.user?.id}"
            openShareWindow(this, grouporder_detail_root, url,
                    staticOrderBean?.title!!,staticOrderBean?.title!!,
                    staticOrderBean?.cover_url!!, object : UMShareListener {
                override fun onCancel(p0: SHARE_MEDIA?) {

                }

                override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                }

                override fun onStart(p0: SHARE_MEDIA?) {
                }

                override fun onResult(p0: SHARE_MEDIA?) {
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if(nextOption != -1){
            if(nextOption == 1){
                //刷新页面
                presenter.startLoadData()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            intent.putExtra("newStatus",staticOrderBean?.status)
            setResult(Activity.RESULT_OK,intent)
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Consts.REQUEST_ORDER_PAY){
                presenter?.successPay()
            }else if(requestCode == Consts.REQUEST_GPORDER_DETAIL){
                staticOrderBean?.status = 6
                showGroupOrderContent(staticOrderBean)
            }
        }
    }

    fun covertTime(millis:Long):String{
        var seconds = millis/1000
        var mins= seconds/60
        var eSeconds=seconds%60
        var hours = mins/60
        var eMins= mins%60
        return "剩余$hours:$eMins:$eSeconds"
    }

    fun covertPayTime(millis:Long):String{
        var seconds = millis/1000
        var mins= seconds/60
        var eSeconds=seconds%60
        var eMins= mins%60
        return "${eMins}分${eSeconds}秒后未支付，此订单将自动关闭"
    }

    override fun isCompleteDialog(it: Boolean?) {
        if(it!!){//true 未满可以支付
            goPay()
        }else{//满了，弹框
            grouporder_detail_dialog_root.visibility = View.GONE
            grouporder_detail_dialog_root.alpha = 0f
            grouporder_detail_dialog_root.visibility = View.VISIBLE
            grouporder_detail_dialog_root.animate().alpha(1f).setDuration(300).start()
        }
    }

    private fun goPay() {
        //付款
        var intent = Intent(this@GroupBuyOrderDetailActivity, GroupOrderPayActivity::class.java)
        intent.putExtra("orderNo",staticOrderBean?.order_no)
        intent.putExtra("oId",staticOrderBean?.id)
        startActivityForResult(intent,Consts.REQUEST_ORDER_PAY)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(timeCountDownTimer != null){
            timeCountDownTimer?.cancel()
        }

        if(payCountDownTimer != null){
            payCountDownTimer?.cancel()
        }
    }
}
