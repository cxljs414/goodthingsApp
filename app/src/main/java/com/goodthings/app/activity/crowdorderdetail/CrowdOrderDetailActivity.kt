package com.goodthings.app.activity.crowdorderdetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.bumptech.glide.Glide
import com.goodthings.app.R
import com.goodthings.app.activity.orderpay.OrderPayActivity
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.CrowdOrderBean
import com.goodthings.app.util.DoubleUtil
import kotlinx.android.synthetic.main.activity_crowd_order_detail.*
import kotlinx.android.synthetic.main.activity_prod_crowd_detail.*
import kotlinx.android.synthetic.main.top_bar.*
import org.jetbrains.anko.imageResource
import java.text.SimpleDateFormat
import java.util.*

class CrowdOrderDetailActivity :
        BaseActivity<CrowdOrderDetailContract.CrowdOrderDetailView,CrowdOrderDetailContract.CrowdOrderDetailPresenter>(),
        CrowdOrderDetailContract.CrowdOrderDetailView{

    override var presenter: CrowdOrderDetailContract.CrowdOrderDetailPresenter= CrowdOrderDetailPresenterImpl()
    private var coundDownTimer:CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crowd_order_detail)
        topback.setOnClickListener {onBackPressed() }
        toptitle.text = "订单详情"
        presenter.startLoadData()
    }

    override fun showCrowdOrderDetail(orderBean: CrowdOrderBean?) {
        scrollView.visibility = View.VISIBLE

        //物流信息
        if((orderBean?.status == 4 || orderBean?.status == 5) ){
            cforderdetail_wuliu_layout.visibility = View.VISIBLE
            if(orderBean.logis_company.isNullOrEmpty()){
                orderBean.logis_company = "无"
                orderBean.way_bill_no = "无"
            }

            cforderdetail_wuliu_company.text = "快递公司：${orderBean.logis_company}"
            cforderdetail_wuliu_orderno.text = "快递单号：${orderBean.way_bill_no}"
        }else{
            cforderdetail_wuliu_layout.visibility = View.GONE
        }

        //收货地址
        cforderdetail_addrinfo_addr.text = orderBean?.addr
        cforderdetail_addrinfo_name.text = orderBean?.name
        cforderdetail_addrinfo_phone.text = orderBean?.phone

        //商品信息
        cforderdetail_prodinfo_title.text = orderBean?.title
        cforderdetail_prodinfo_intro.text = orderBean?.intro
        Glide.with(this).load(Consts.IMAGEURL+orderBean?.sub_cover_pic).into(cforderdetail_prodinfo_coverpic)
        cforderdetail_prodinfo_price.text = "￥${orderBean?.price}"
        cforderdetail_prodinfo_count.text = "数量：${orderBean?.count}件"

        //下单时间
        cforderdetail_createtime_time.text =SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(orderBean?.create_time!!.toLong()))

        //订单号
        cforderdetail_orderno.text = orderBean?.order_no

        //商品总额
        cforderdetail_prodall.text = "￥${orderBean?.price!!*orderBean?.count!!}"

        //运费
        cforderdetail_sendfee.text = "￥0"

        //按钮
        when(orderBean?.status){
            1->{
                cforderdetail_opt1_layou.visibility = View.VISIBLE
                cforderdetail_opt4_layou.visibility = View.GONE
            }
            2,5,6,7,8,9->{
                cforderdetail_opt1_layou.visibility = View.GONE
                cforderdetail_opt4_layou.visibility = View.GONE
            }
            3->{
                cforderdetail_opt1_layou.visibility = View.GONE
                cforderdetail_opt4_layou.visibility = View.GONE
            }
            4->{
                cforderdetail_opt1_layou.visibility = View.GONE
                cforderdetail_opt4_layou.visibility = View.VISIBLE
            }
        }


        //顶部提示
        when(orderBean?.status){
            1->{//未付款  “59分59秒后未支付，此订单将自动关闭”
                var calendar = Calendar.getInstance()
                calendar.time = Date(orderBean.create_time.toLong())//SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(orderBean.create_time)////
                var totleSecond = ((System.currentTimeMillis() - calendar.timeInMillis)/1000).toInt()
                var surMinutes= 60-(totleSecond/60).toInt()
                if(surMinutes<=0){
                    cfodetail_toptip.text = "订单已取消"
                    orderBean.status = 2
                }else{
                    var surSeconds = 60*60 - totleSecond%60
                    cfodetail_toptip.text = "${(surSeconds/60).toInt()}分${(surSeconds%60).toInt()}秒后未支付，此订单将自动关闭"
                    coundDownTimer = object: CountDownTimer((surMinutes*60*1000).toLong(),1000){
                        override fun onTick(surMiliSeconds: Long) {
                            cfodetail_toptip.text = "${(surMiliSeconds/(1000*60))}分${(surMiliSeconds/1000)%60}秒后未支付，此订单将自动关闭"
                        }

                        override fun onFinish() {
                            orderBean.status = 2
                            showCrowdOrderDetail(orderBean)
                        }
                    }
                    coundDownTimer?.start()
                }
            }
            2->{
                cfodetail_toptip.text = "订单已取消"
            }
            3->{//已支付 ，
                //如果已手动点成功或者预购产品已经成功 显示“预购成，请耐心等待发货”
                // 如果失败显示“很抱歉预购没有达成，众筹款将在15个工作日内退还到原账户”
                // 否则 “预购进行中，剩余12天4小时”
                if(orderBean.is_manual_finish == 1){
                    cfodetail_toptip.text = "预购成功，请耐心等待发货"
                }else{
                    when(orderBean.pStatus){
                        0->{//进行中
                            cfodetail_toptip.text = calculateEndTime(orderBean.end_time)
                        }
                        1->{//已成功
                            cfodetail_toptip.text = "预购成功，请耐心等待发货"
                        }
                        2->{//已失败
                            cfodetail_toptip.text = "很抱歉预购没有达成，众筹款将在15个工作日内退还到原账户"
                        }
                    }
                }

            }
            4->{//已发货
                cfodetail_toptip.text = "卖家已发货"
            }
            5,6->{//已收货
                cfodetail_toptip.text = "预购完成"
            }
            7,8,9->{
                cfodetail_toptip.text = "很抱歉预购没有达成，众筹款将在15个工作日内退还到原账户"
            }
        }



        //圆形进度条
        if(orderBean?.status == 2){//已取消
            cforderdetail_progress_layout.visibility = View.GONE
            cforderdetail_fail_layout.visibility = View.VISIBLE
            cforderdetail_fail_text.visibility = View.GONE
            cforderdetail_fail_image.imageResource = R.mipmap.order_cancel
        }else if(orderBean?.status == 7){//退款中
            cforderdetail_fail_text.text = "预购没有达成，退款中"
            cforderdetail_fail_image.imageResource = R.mipmap.order_backing
            cforderdetail_progress_layout.visibility = View.GONE
            cforderdetail_fail_layout.visibility = View.VISIBLE
        }else if(orderBean?.status == 8){//退款成功
            cforderdetail_fail_text.text = "预购没有达成，退款成功"
            cforderdetail_fail_image.imageResource = R.mipmap.order_back_success
            cforderdetail_progress_layout.visibility = View.GONE
            cforderdetail_fail_layout.visibility = View.VISIBLE
        }else if(orderBean?.status == 9){//退款失败
            cforderdetail_fail_text.text = "预购没有达成，退款失败"
            cforderdetail_fail_image.imageResource = R.mipmap.order_back_fail
            cforderdetail_progress_layout.visibility = View.GONE
            cforderdetail_fail_layout.visibility = View.VISIBLE
        }else{
            cforderdetail_progress_layout.visibility = View.VISIBLE
            cforderdetail_fail_layout.visibility = View.GONE
            if(orderBean?.is_manual_finish == 1){
                cfodetail_progressbar.setProgress(100)
                cfodetail_progressbar_gray.setProgress(100)
                cfodetail_progressbar.visibility = View.VISIBLE
                cfodetail_progressbar_gray.visibility = View.GONE
                cfodetail_progressbar.setProgressExtraText("预购成功")
            }else{
                var per= orderBean?.sale_num!!.toDouble() /orderBean.stock.toDouble()
                var percen = DoubleUtil.round(per * 100,2)
                cfodetail_progressbar.setProgress(percen.toInt())
                cfodetail_progressbar_gray.setProgress(percen.toInt())
                if(percen > percen.toInt()){
                    cfodetail_progressbar.setProgressTextCus("$percen%")
                    cfodetail_progressbar_gray.setProgressTextCus("$percen%")
                }else{
                    cfodetail_progressbar.setProgressTextCus("${percen.toInt()}%")
                    cfodetail_progressbar_gray.setProgressTextCus("${percen.toInt()}%")
                }
                when(orderBean?.pStatus){
                    0->{//进行中
                        cfodetail_progressbar.visibility = View.VISIBLE
                        cfodetail_progressbar_gray.visibility = View.GONE
                        cfodetail_progressbar.setProgressExtraText("进行中")
                    }
                    1->{//已成功
                        cfodetail_progressbar.visibility = View.VISIBLE
                        cfodetail_progressbar_gray.visibility = View.GONE
                        cfodetail_progressbar.setProgressExtraText("预购成功")
                    }
                    2->{//已失败
                        cfodetail_progressbar.visibility = View.GONE
                        cfodetail_progressbar_gray.visibility = View.VISIBLE
                        cfodetail_progressbar_gray.setProgressExtraText("未达成")
                    }
                }
            }

            //状态步骤图
            when(orderBean.status){
                1,2->cfodetail_step.imageResource = R.mipmap.cforderdetail_imag1
                3->cfodetail_step.imageResource = R.mipmap.cforderdetail_imag2
                4->cfodetail_step.imageResource = R.mipmap.cforderdetail_imag3
                5->cfodetail_step.imageResource = R.mipmap.cforderdetail_imag4
                else->cfodetail_step.imageResource = R.mipmap.cforderdetail_imag4
            }
        }


        //按钮点击事件
        cforderdetail_opt1_cancel.setOnClickListener {
            //取消订单
            presenter?.cancelOrder(orderBean)
        }

        cforderdetail_opt1_pay.setOnClickListener {
            //付款
            var intent = Intent(this@CrowdOrderDetailActivity, OrderPayActivity::class.java)
            intent.putExtra("orderNo",orderBean.order_no)
            startActivityForResult(intent,Consts.REQUEST_ORDER_PAY)
        }

        cforderdetail_opt4_suretake.setOnClickListener {
            //确认收货
            presenter.sureTake()
        }
        topback.setOnClickListener {
            intent.putExtra("newStatus",orderBean.status)
            setResult(Activity.RESULT_OK,intent)
            onBackPressed()
        }

    }

    private fun calculateEndTime(end_time: String): String? {
        var calendar = Calendar.getInstance()
        calendar.time = Date(end_time.toLong())
        var totleHours = ((calendar.timeInMillis - System.currentTimeMillis())/(1000*60*60)).toInt()
        var totleDays= (totleHours/24).toInt()
        var extraHours= totleHours%24
        return "预购进行中，剩余${totleDays}天${extraHours}小时"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Consts.REQUEST_ORDER_PAY){
                presenter?.successPay()
            }
        }
    }
}
