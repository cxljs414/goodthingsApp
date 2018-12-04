package com.goodthings.app.activity.crowddetail

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.goodthings.app.R
import com.goodthings.app.activity.crowdorderlist.CrowdOrderListActivity
import com.goodthings.app.activity.login.LoginActivity
import com.goodthings.app.activity.orderpay.OrderPayActivity
import com.goodthings.app.activity.web.WebActivity
import com.goodthings.app.adapter.CrowdDetailRecomAdapter
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.ProdCrowdBean
import com.goodthings.app.bean.ScrowdUserBean
import com.goodthings.app.util.*
import com.liji.imagezoom.util.ImageZoom
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_prod_crowd_detail.*
import kotlinx.android.synthetic.main.top_bar.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.imageResource
import java.util.*
import kotlin.collections.ArrayList


class ProdCrowdDetailActivity :
        BaseActivity<ProdCrowdDetailContract.ProdCrowdDetailView,ProdCrowdDetailContract.ProdCrowdDetailPresenter>(),
        ProdCrowdDetailContract.ProdCrowdDetailView{

    override var presenter: ProdCrowdDetailContract.ProdCrowdDetailPresenter = ProdCrowdDetailPresenterImpl()
    private var isCollect:Boolean = false
    private var isUserFollowBean : Boolean = false
    private var recomAdapter: CrowdDetailRecomAdapter? = null
    private var resumFromLogin:Boolean= false
    private var isLooked10 = false
    private var isScrollBottom= false
    private var isGetLoolGold= false
    private var downTimer:CountDownTimer? = null
    private var readdownTimer:CountDownTimer? = null
    private var urlImageParser :URLImageParser ? = null
    private var prodCrowdId =-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prod_crowd_detail)
        toptitle.text = "产品预购"
        topback.setOnClickListener { onBackPressed() }
        top_right_btn.visibility = View.VISIBLE
        top_right_image.imageResource = R.mipmap.fenxiang2
        initBanner()
        downTimer = object:CountDownTimer(10000,1000){
            override fun onFinish() {
                isLooked10 = true
                if(!isGetLoolGold && isScrollBottom){
                    isGetLoolGold = true
                    presenter.cfDailyGoldEnvelopes()
                }

            }

            override fun onTick(millisUntilFinished: Long) {

            }

        }
        downTimer?.start()
    }

    private fun initBanner() {
        var screenWidth= ScreenUtil.getScreenWidth(this)
        var bannerHeight= (screenWidth*10/16).toInt()
        prodcrowddetail_banner?.layoutParams = LinearLayout.LayoutParams(screenWidth,bannerHeight)
    }

    private fun loadData() {
        initRecommend()
        presenter.startLoadData(prodCrowdId)
        prodcrowd_scrollview.scrollTo(0,0)
    }

    private inner class TouchListenerImpl : OnTouchListener {
        override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                }
                MotionEvent.ACTION_MOVE -> {
                    val scrollY = view.scrollY
                    val height = view.height
                    val scrollViewMeasuredHeight = prodcrowd_scrollview.getChildAt(0).getMeasuredHeight()
                    if (scrollY == 0) {
                        println("滑动到了顶端 view.getScrollY()=" + scrollY)
                    }
                    if (scrollY + height == scrollViewMeasuredHeight) {
                        isScrollBottom = true
                        if(!isGetLoolGold && isLooked10){
                            isGetLoolGold = true
                            presenter.cfDailyGoldEnvelopes()
                        }
                    }
                }

                else -> {
                }
            }
            return false
        }

    }

    override fun showCrowdContent(prod: ProdCrowdBean) {
        prodcrowd_scrollview.setOnTouchListener(TouchListenerImpl())
        //banner
        if(prod.covers != null){
            var bannerList:MutableList<String> = ArrayList()
            prod.covers !!.forEach {
                bannerList.add(Consts.IMAGEURL+it)
            }
            prodcrowddetail_banner?.setImages(bannerList)
            prodcrowddetail_banner?.setImageLoader(GlideImageLoader())
            prodcrowddetail_banner?.setOnBannerListener {

            }
            prodcrowddetail_banner?.start()
        }/*else{
            banner.visibility= View.GONE
        }*/
        when(prod?.status){
            0->{
                prodcrowd_state.text = "进行中"
                prodcrowd_state_layout.visibility = View.GONE
                prodcrowd_buy.text = "去支持"
                prodcrowd_buy.backgroundResource = R.color.red_ff4949
                prodcrowd_buy.isEnabled = true
            }
            1->{
                prodcrowd_state.text = "已成功"
                prodcrowd_state_layout.visibility = View.VISIBLE
                prodcrowd_state1.text = "预购成功"
                prodcrowd_state_content.text = "预购目标达成，商城将在${prod.send_days}天后陆续发货"
                prodcrowd_buy.text = "众筹已结束"
                prodcrowd_buy.backgroundResource = R.color.grey_153
                prodcrowd_buy.isEnabled = false
            }
            2->{
                prodcrowd_state.text = "已结束"
                prodcrowd_state_layout.visibility = View.VISIBLE
                prodcrowd_state1.text = "预购已结束"
                prodcrowd_state_content.text = "预购目标没有达成，已付货款将在15个工作日内退回原支付账户"

                prodcrowd_buy.text = "众筹已结束"
                prodcrowd_buy.backgroundResource = R.color.grey_153
                prodcrowd_buy.isEnabled = false
            }
        }
        prodcrowd_buy.setOnClickListener {
            if(Consts.isLogined){
                var curStock= prod.stock - prod.sale_num_tmp
                if(curStock == 0 && prod.is_beyond_stock == 0){
                    showMessage("库存不足")
                }else{
                    showDialog(prod)
                }

            }else{
                goLogin()
            }

        }
        prodcrowd_title.text = prod.title
        prodcrowd_support_count.text = "${prod.userCount}人支持"
        prodcrowd_market_price.paint?.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
        prodcrowd_market_price.text = "市场价：${prod.oldPrice}元"
        prodcrowd_intro.text = prod.intro
        prodcrowd_target_amt.text = "目标金额：￥${prod.target_amt}"
        prodcrowd_sale_amt.text = "￥${prod.sale_amt}"

        var per= prod?.sale_num!!.toDouble() /prod?.stock!!.toDouble()
        var percen = DoubleUtil.round(per * 100,2)
        if(prod.is_manual_finish == 1){
            prodcrowd_progressbar.progress = 100
            prodcrowd_state.text = "已成功"
            if(prod.userCount == 0){
                prodcrowd_support_count.text = "${prod.sale_num}人支持"
            }
        }else{
            prodcrowd_progressbar.progress = percen.toInt()
        }
        if(percen > percen.toInt()){
            prodcrowd_percent.text = "$percen%"
        }else{
            prodcrowd_percent.text = "${percen.toInt()}%"
        }

        prodcrowd_crowd_price.text = "￥${prod.price}"
        prodcrowd_crowd_count.text = "${prod.stock}件"
        if(prod.limit_num == 0){
            prodcrowd_limit_count.text = "不限"
        }else{
            prodcrowd_limit_count.text = "${prod.limit_num}件"
        }

        Glide.with(this).load(Consts.IMAGEURL+prod.head_url).into(prodcrowd_headview)
        prodcrowd_nickname.text = prod.nickname
        if(prod.province_value == null){
            prodcrowd_location.visibility = View.GONE
        }else{
            prodcrowd_location.visibility = View.VISIBLE
            prodcrowd_location.text = prod.province_value
        }
        if(prod.status != 0){
            prodcrowd_endtime.visibility = View.GONE
        }else{
            prodcrowd_endtime.visibility = View.VISIBLE
            prodcrowd_endtime.text = calculateEndTime(prod.end_time)
        }

        if(prod.content == null || prod.content.isEmpty()){
            prodcrowd_html.visibility = View.GONE
        }else{
            prodcrowd_html.visibility = View.VISIBLE

            var  head:String= "<html><head><style type=\"text/css\">img{width:${ScreenUtil.getScreenWidth(this)}px !important;height:auto}</style></head>" +
                    "<body>${prod.content}</body></html>"
            var width = ScreenUtil.getScreenWidth(this) - ScreenUtil.dip2px(this,30f)
            urlImageParser = URLImageParser(this@ProdCrowdDetailActivity,prodcrowd_html,width)
            prodcrowd_html.text = Html.fromHtml(prod.content, urlImageParser,null)
        }

        if(prod.cf_plan_url.isNullOrEmpty() &&
                prod.finance_plan_url.isNullOrEmpty() &&
                prod.show_plan_url.isNullOrEmpty()){
            crowddetail_plan_layout.visibility = View.GONE
        }else{
            crowddetail_plan_layout.visibility = View.VISIBLE
            //计划书
            if (prod.cf_plan_url.isNullOrEmpty()){
                crowddetail_cfplan.visibility = View.GONE
            }else{
                crowddetail_cfplan.visibility = View.VISIBLE
            }

            if (prod.finance_plan_url.isNullOrEmpty()){
                crowddetail_finance.visibility = View.GONE
            }else{
                crowddetail_finance.visibility = View.VISIBLE
            }

            if (prod.show_plan_url.isNullOrEmpty()){
                crowddetail_showplan.visibility = View.GONE
            }else{
                crowddetail_showplan.visibility = View.VISIBLE
            }
        }

        prodcrowd_share_img.setOnClickListener {
            addShare(prod)
        }

        prodcrowd_xihuan_img.setOnClickListener {
            presenter.requestChangeCollect(isCollect)
        }

        prodcrowd_bottom_likecount_img.setOnClickListener {
            presenter.requestChangeCollect(isCollect)
        }

        prodcrowd_user_follow.setOnClickListener {
            presenter.requestChangeUserFollow(isUserFollowBean)
        }

        prodcrowd_bottom_back.setOnClickListener {
            onBackPressed()
        }

        prodcrowd_bottom_sharecount.setOnClickListener {
            addShare(prod)
        }

        prodcrowd_bottom_likecount.setOnClickListener {
            presenter.requestChangeCollect(isCollect)
        }

        top_right_btn.setOnClickListener { addShare(prod) }

        crowddetail_cfplan.setOnClickListener {
            goWebView(prod.cf_plan_url)
        }

        crowddetail_finance.setOnClickListener {
            goWebView(prod.finance_plan_url)
        }

        crowddetail_showplan.setOnClickListener {
            goWebView(prod.show_plan_url)
        }

        prodcrowd_ip1.setOnClickListener {
            var intent = Intent(this@ProdCrowdDetailActivity, WebActivity::class.java)
            intent.putExtra("url",Consts.URL+"/unauth/toShare?ind=0")
            startActivity(intent)
        }
        prodcrowd_ip2.setOnClickListener {
            var intent = Intent(this@ProdCrowdDetailActivity, WebActivity::class.java)
            intent.putExtra("url",Consts.URL+"/unauth/toShare?ind=1")
            startActivity(intent)
        }
        prodcrowd_ip3.setOnClickListener {
            var intent = Intent(this@ProdCrowdDetailActivity, WebActivity::class.java)
            intent.putExtra("url",Consts.URL+"/unauth/toShare?ind=2")
            startActivity(intent)
        }
        prodcrowd_ip4.setOnClickListener {
            var intent = Intent(this@ProdCrowdDetailActivity, WebActivity::class.java)
            intent.putExtra("url",Consts.URL+"/unauth/toShare?ind=3")
            startActivity(intent)
        }

    }

    private fun initRecommend() {
        crowddetail_recyclerview.layoutManager = NoScrollGridLayoutManager(this,2)
        crowddetail_recyclerview.addItemDecoration(GridSpacingItemDecoration(2, ScreenUtil.dip2px(this,15f),false))

        var imageWidth= (ScreenUtil.getScreenWidth(this) - ScreenUtil.dip2px(this,45f))/2
        recomAdapter = CrowdDetailRecomAdapter(ArrayList(),imageWidth)
        crowddetail_recyclerview.adapter = recomAdapter
        recomAdapter?.setOnItemChildClickListener { adapter, view, position ->
            var bean:ProdCrowdBean = adapter.getItem(position) as ProdCrowdBean
            prodCrowdId = bean.pId
            loadData()
        }
    }

    fun showDialog(prod:ProdCrowdBean){
        prodcrowd_dialog_root.alpha = 0f
        prodcrowd_dialog_root.visibility = View.VISIBLE
        prodcrowd_dialog_root.animate().alpha(1f).setDuration(300).start()
        prodcrowd_dialog_root.setOnClickListener {  }
        prodcrowd_dialog_close.setOnClickListener {
            prodcrowd_dialog_root.visibility = View.GONE
        }
        Glide.with(this).load(Consts.IMAGEURL+prod.sub_cover_pic).into(prodcrowd_dialog_image)
        prodcrowd_dialog_image.setOnClickListener {
            ImageZoom.show(this@ProdCrowdDetailActivity,Consts.IMAGEURL+prod.cover_pic)
        }
        prodcrowd_dialog_title.text = prod.title
        prodcrowd_dialog_price.text = "${prod.price}元"
        prodcrowd_dialog_stock.text = "目前剩余${prod.stock - prod.sale_num_tmp}件"
        prodcrowd_dialog_submmit.text = "￥${prod.price}确定"
        prodcrowd_dialog_checkbox.isChecked = true
        prodcrowd_dialog_contract.paint?.flags = Paint.UNDERLINE_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
        prodcrowd_dialog_count.text = "1"
        var curCount= Integer.parseInt(prodcrowd_dialog_count.text.toString())
        var curStock= prod.stock - prod.sale_num_tmp
        prodcrowd_dialog_reduce.setOnClickListener {
            curCount-=1
            if(curCount<1)curCount =1
            prodcrowd_dialog_count.text = "$curCount"
            prodcrowd_dialog_submmit.text = "￥${prod.price*curCount}确定"
        }

        //是：是否大于limit_num
        //  否：判断（库存-销量）> 购买数
        //      否：购买
        //      是：判断is_beyond_stock
        //          是：购买
        prodcrowd_dialog_add.setOnClickListener {
            curCount+=1
            if(prod.limit_num in 1..(curCount - 1)){
                curCount = prod.limit_num
                showMessage("每人限购${prod.limit_num}件")
            }
            if(prod.is_beyond_stock == 0) {
                if (curCount > curStock) {
                    curCount = curStock
                    showMessage("库存剩余${curStock}件")
                }
            }
            prodcrowd_dialog_count.text = "$curCount"
            prodcrowd_dialog_submmit.text = "￥${prod.price*curCount}确定"
        }

        prodcrowd_dialog_submmit.setOnClickListener {

            if(prodcrowd_dialog_checkbox.isChecked){
                //到支付
                var count= Integer.parseInt(prodcrowd_dialog_count.text.toString())
                var intent = Intent(this@ProdCrowdDetailActivity, OrderPayActivity::class.java)
                intent.putExtra("prodId",prod.pId)
                intent.putExtra("count",count)
                startActivityForResult(intent,Consts.REQUEST_ORDER_PAY)
                prodcrowd_dialog_root.visibility = View.GONE
            }
        }

        prodcrowd_dialog_contract.setOnClickListener {
            prodcrowd_contract_root.visibility = View.VISIBLE
        }
        prodcrowd_contract_root.setOnClickListener {
            prodcrowd_contract_root.visibility = View.GONE
        }
        prodcrowd_contract_close.setOnClickListener {
            prodcrowd_contract_root.visibility = View.GONE
        }
        prodcrowd_contract_content.setOnClickListener {

        }

        prodcrowd_contract_content.text = Html.fromHtml("<div><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">支持者协议</font></span></b><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">本支持者协议（</font>“本协议”）由项目支持方（下称“支持者”）、发起方（下称“发起者”）与</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">产品的运营管理者及其关联公司（下称</font>“</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\">”）在北京共同签署。</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">发起者和支持者应在签署本协议前认真阅读协议全部内容，尤其是以加粗标示的有关风险提示、特别提示、管辖法院等条款。发起者点击</font>“同意并接受”按钮、支持者点击“支持”按钮、签署本协议的纸版文件或使用本协议项下服务的，即表示发起者和支持者均已阅读并理解本协议所有内容，同意并签署了本协议，本协议则立即生效，各方均应遵守本协议约定。</span></b><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">本着自愿、平等、互利原则，经充分协商，各方就本协议项下事宜达成如下协议，以昭信守。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">第一条</font> &nbsp;术语和定义</span></b><span style=\"font-family: 宋体; font-size: 10.5pt;\">&nbsp;</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.1 &nbsp;</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">产品：指</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">运营管理的产品，包括但不限于</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">网站（网址</font>www.</span><span style=\"font-family: 宋体; font-size: 10.5pt;\">h</span><span style=\"font-family: 宋体; font-size: 10.5pt;\">igoodthings.com）及其App、微打赏产品及其未来新开发或合作的平台或工具。</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.2 &nbsp;</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">：指</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">产品的运营管理者及其关联公司。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.3 &nbsp;</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">预购</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">：指发起者与支持者共同完成项目、实现梦想的行为，在这一过程中支持者出资</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">购买</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">发起者</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">商品</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">、发起者完成</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">商品生产与发货，</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">并依据项目页面中的约定完成承诺。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.4 &nbsp;项目：指发起者通过</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">产品发起的、拟通过</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">预购</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">方式</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">出售商品</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">的项目。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.5 &nbsp;项目页面：指发起者在</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">产品中发布的项目展示页面，页面中包含了项目名称、项目介绍、</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">预购</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">期限、预购目标等内容和信息。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.6 &nbsp;发起者：指通过</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">产品发起项目的自然人、法人或其他组织。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.7 &nbsp;支持者：指出资支持项目的自然人、法人或其他组织。</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.8 &nbsp;</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">预购</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">期限：指发起者确定的项目的募集期间。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.9 &nbsp;</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">预购</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">目标：指发起者确定的项目成功的目标。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.10 &nbsp;</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">目标</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">金额：指预购期限届满时所募集到的资金总额。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.11&nbsp;</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">预购</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">成功：指项目</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">预购</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">期限届满且达到</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">预购</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">目标。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.12&nbsp;</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">预购</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">失败：指项目预购期限届满但未能达到预购目标。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.13&nbsp;</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">预购</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">取消：指预购期限届满前，发起者主动终止预购或者</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">根据本协议及相关规则的规定决定终止预购。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.14&nbsp; 项目终止：指项目在预购成功后，发起者最终未能向支持者发放全部</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">商品</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">或者</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">根据本协议及相关规则的规定决定终止项目。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.15&nbsp; 项目成功结束：指项目在预购成功后，发起者向支持者发放完毕全部</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">商品</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">1.16&nbsp; 各方：指本协议的签署方，即支持者和</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">第二条</font> &nbsp;特别注意事项</span></b><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\">2.1 &nbsp;</span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">好事发生</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">仅为发起者和支持者之间的预购提供平台网络空间、技术服务和支持等服务。</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">好事发生</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">作为平台方，并不是发起者或支持者中的任何一方，预购仅存在于发起者和支持者之间，使用</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">好事发生</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">产品产生的法律后果由发起者和支持者自行承担。</font></span></b><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\">2.2 &nbsp;本协议内容包括协议正文、附件、</span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">好事发生</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">产品上已经发布和将来可能发布的各类规则（包括但不限于</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">如</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">《结算和费率规则》、《退款流程》等）、操作流程、规范、指南等，均为本协议不可分割的一部分，与本协议具有同等效力（另有约定的除外）。</font></span></b><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\">2.3 &nbsp;支持者勾选本协议、签署本协议，或者使用本协议项下服务的，即视为支持者同意并接受本协议的全部内容，本协议即对支持者具有约束力，支持者不得再以未阅读或理解本协议的全部内容为由主张本协议无效或要求撤销本协议。</span></b><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\">2.4 &nbsp;</span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">好事发生</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">有权不时修改本协议及相关规则、流程、指南、规范等，如本协议有任何变更，</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">好事发生</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">将以公示形式通知，修改后的协议内容及相关规则即成为本协议的组成部分。发起者、支持者继续使用</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">好事发生</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">产品服务的，即视为已阅读理解并接受修改后的协议和规则，并受其约束。</font></span></b><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\">2.5 &nbsp;支持者应按照本协议要求行使权利、履行义务，支持者不能接受本协议及相关规则（包括修订后版本）的约定的，应立即停止使用</span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">好事发生</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">产品的各项服务。</font></span></b><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\">2.6&nbsp; 本协议目前适用的</span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">所有</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">类别</font></span></b><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">预购。</font></span></b><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">第三条</font> &nbsp;发起者和支持者的权利义务</span></b><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">3.1 &nbsp;发起者通过</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">产品发起项目，在项目页面注明项目名称、项目介绍、预购期限、预购目标等内容和信息；支持者点击</font>“立即支持”按钮进行支持。</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">3.2 &nbsp;发起者应遵守</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">的各类规则。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">3.3 &nbsp;预购成功后，发起者按约定在承诺时间内完成承诺事项。支持者有权在项目成功结束后对项目进行评价和反馈。</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">3.4 &nbsp;预购失败或预购取消的，所筹款项将由</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">系统自动退回给支持者。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">3.5 &nbsp;项目终止的，发起者应及时通过</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">产品及其他适当途径告知支持者，并与其友好协商，妥善处理终止缮后事宜；未结算款项将由</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">系统按支付金额比例发回支持者账户，已结算款项首先由项目发起者返还至</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">账户，然后由</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">平台完成对支持者的退款；如项目发起者拒绝返还已结算款项，支持者应向发起者追索，</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">作为第三方平台，不负有支付义务。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">3.6 &nbsp;因项目产生的任何争议，应由发起者与支持者进行友好协商，尽其最大努力妥善处理纠纷。</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">第四条</font> &nbsp;违约责任</span></b><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">4.1 &nbsp;除本协议另有约定外，任何一方违反其于本协议项下的陈述、保证或承诺，而使其他各方遭受任何诉讼、纠纷、索赔、处罚等的，违约方应与其他各方进行充分友好协商并依法承担违约责任。</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">4.2 &nbsp;如某一方发生违约行为，守约方可以书面通知方式要求违约方在指定的时限内停止违约行为，要求其消除影响。如违约方未能按时停止违约行为，则守约方有权立即终止本协议。&nbsp;</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">第五条</font> &nbsp;风险提示</span></b><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">5.1 &nbsp;政策法律风险：有关预购的相关国家法律法规及政策变化，发起者可能根据该变化依法调整预购的内容，届时支持者须与发起者友好协商解决，发起者应尽最大可能处理纠纷。</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">5.2 &nbsp;市场和运营风险：由于项目运营问题、市场行情变化等情形，支持者可能无法得到项目承诺回报或回报与承诺不一致，届时支持者须与发起者友好协商解决，发起者应尽最大可能处理纠纷。</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">5.3 &nbsp;技术风险：预购涉及到互联网服务，可能会受到各个环节不稳定因素的影响。因不可抗力、计算机病毒或黑客攻击、系统不稳定以及其他任何技术、互联网络、通信线路原因等造成的系统故障、服务中断或不能满足服务要求的风险，及由此发生经济损失的，发起者和支持者须自行承担。&nbsp;</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><b><span class=\"15\" style=\"font-family: 等线; font-size: 10.5pt;\"><font face=\"等线\">第六条</font> &nbsp;其他规则</span></b><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">6.1 &nbsp;本协议项下预购活动所产生的税费负担，由中国相关法律法规所规定的责任方自行承担并依法申报和缴纳。</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">6.2 &nbsp;如果本协议的部分条款被争议解决机构认定为违法或不可执行，该部分条款的违法或不可执行不影响其他条款的有效性和可执行性。</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">6.3 &nbsp;本协议适用于中华人民共和国法律法规（仅为本协议之目的，不包括香港特别行政区、澳门特别行政区和台湾地区）。</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p><p class=\"p\" style=\"margin-top:10.5000pt;margin-bottom:10.5000pt;background:rgb(255,255,255);\"><span style=\"font-family: 宋体; font-size: 10.5pt;\">6.4 &nbsp;因本协议及其执行或解释等发生争议的，应先友好协商解决，如协商不成的，任何一方均有权向</span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">好事发生</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><font face=\"宋体\">住所地有管辖权的人民法院起诉。</font></span><span style=\"font-family: 宋体; font-size: 10.5pt;\"><o:p></o:p></span></p></div>")
    }

    fun addShare(prod: ProdCrowdBean) {
        if(Consts.isLogined) {
            var url = Consts.URL + "crowdinfo/unauth/share/" + prod.pId
            openShareWindow(this, crowddetail_root, url, prod.title, prod.intro, prod.cover_pic, object : UMShareListener {
                override fun onCancel(p0: SHARE_MEDIA?) {

                }

                override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                }

                override fun onStart(p0: SHARE_MEDIA?) {
                }

                override fun onResult(p0: SHARE_MEDIA?) {
                    presenter.addShare(prod.pId)
                }
            })
        }else{
            goLogin()
        }
    }

    override fun showCrowdUserInfo(user: ScrowdUserBean?) {
        Glide.with(this).load(Consts.IMAGEURL+user?.img).into(prodcrowd_headview2)
        prodcrowd_nickname2.text = user?.userName
        prodcrowd_user_city.text = user?.city
        prodcrowd_user_works.text = "${user?.spuCount}"
        prodcrowd_user_fans.text = "${user?.count}"
        if(user?.browse!!){
            changeUserFollow(true)
        }else{
            changeUserFollow(false)
        }

    }

    private fun calculateEndTime(end_time: String): String? {
        var calendar = Calendar.getInstance()
        calendar.time = Date(end_time.toLong())
        var totleHours = ((calendar.timeInMillis - System.currentTimeMillis())/(1000*60*60)).toInt()
        var totleDays= (totleHours/24).toInt()
        var extraHours= totleHours%24
        return "剩余${totleDays}天${extraHours}小时"
    }

    override fun shareCountChange() {
        var countStr:String= prodcrowd_share_count.text.toString()
        var shareCount= 0
        if(countStr != ""){
            shareCount = Integer.parseInt(countStr)
        }
        shareCount+=1
        prodcrowd_share_count.text = "$shareCount"
        prodcrowd_bottom_sharecount.text = "$shareCount"
    }

    override fun showCollectCount(it: Int?) {
        prodcrowd_xihuan_count.text = "$it"
        prodcrowd_bottom_likecount.text = "$it"
    }

    override fun showShareCount(it: Int?) {
        prodcrowd_share_count.text = "$it"
        prodcrowd_bottom_sharecount.text = "$it"
    }

    override fun changeCollect(isCollect: Boolean, needAdd: Boolean) {
        this.isCollect = isCollect
        var likeCount= Integer.parseInt(prodcrowd_xihuan_count.text.toString())
        if(isCollect){
            prodcrowd_xihuan_img.imageResource = R.mipmap.like_selected1
            prodcrowd_bottom_likecount_img.imageResource = R.mipmap.xihuan_heart
            if(needAdd) likeCount+=1
        }else{
            prodcrowd_xihuan_img.imageResource = R.mipmap.like_nomal1
            prodcrowd_bottom_likecount_img.imageResource = R.mipmap.xihuan_heart_gray
            if(needAdd){
                likeCount-=1
                if(likeCount<0)likeCount = 0
            }
        }
        prodcrowd_xihuan_count.text = "$likeCount"
        prodcrowd_bottom_likecount.text = "$likeCount"
    }

    override fun changeUserFollow(isFollow: Boolean) {
        isUserFollowBean = isFollow
        if(!isFollow){
            prodcrowd_user_follow.text = "+关注"
            prodcrowd_user_follow.backgroundResource = R.drawable.bg_blue_corner_5
        }else{
            prodcrowd_user_follow.text = "取消关注"
            prodcrowd_user_follow.backgroundResource = R.drawable.bg_grey_corner_5
        }
    }

    override fun goLogin() {
        resumFromLogin = true
        startActivity(Intent(this@ProdCrowdDetailActivity,LoginActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK ){
            if(requestCode == Consts.REQUEST_ORDER_PAY){
                presenter.payQueryByid()
            }
        }
    }

    override fun notifyRecomListUpdate(list: MutableList<ProdCrowdBean>) {
        recomAdapter?.setNewData(list)
        recomAdapter?.notifyDataSetChanged()
    }

    /**
     * 是否购买过，
     *   是：判断is_more_buy
     */
    override fun showCanBuy(prodId: Int, canMoreBuy:Int,buycount: Int) {
        if(buycount == 0){
            prodcrowd_buy.text = "去支持"
            prodcrowd_buy.backgroundResource = R.color.red_ff4949
            prodcrowd_buy.isEnabled = true
        }else{
            if(canMoreBuy == 1){
                prodcrowd_buy.text = "再次支持"
                prodcrowd_buy.backgroundResource = R.color.red_ff4949
                prodcrowd_buy.isEnabled = true
            }else{
                prodcrowd_buy.isEnabled = true
                prodcrowd_buy.text = "查看预购订单"
                prodcrowd_buy.backgroundResource = R.color.grey_153
                prodcrowd_buy.setOnClickListener {
                    showMessage("查看众筹订单")
                    if(Consts.isLogined) {
                        startActivity(Intent(this@ProdCrowdDetailActivity, CrowdOrderListActivity::class.java))
                    }else{
                        resumFromLogin = true
                        startActivity(Intent(this@ProdCrowdDetailActivity,LoginActivity::class.java))
                    }
                }
            }
        }
    }

    override fun showReadGold(it: Int?) {
        prodcrowd_ydjl.visibility = View.VISIBLE
        readdownTimer = object:CountDownTimer(3000,1000){
            override fun onFinish() {
                prodcrowd_ydjl.visibility = View.GONE
            }

            override fun onTick(millisUntilFinished: Long) {
            }

        }
        readdownTimer?.start()
    }

    override fun onResume() {
        super.onResume()
        if(Consts.prodCrowdId != -1){
            if(Consts.prodCrowdId != prodCrowdId){
                prodCrowdId = Consts.prodCrowdId
                loadData()
            }
            Consts.prodCrowdId = -1
        }
        if(resumFromLogin){
            if(Consts.isLogined) {
                presenter.payQueryByid()
                presenter.getIsCollect(false)
                presenter.querySkillUser()
            }
        }
        resumFromLogin = false
    }

    private fun goWebView(url:String){
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Consts.IMAGEURL+url)))
    }

    override fun onDestroy() {
        super.onDestroy()
        downTimer?.cancel()
        readdownTimer?.cancel()
        urlImageParser?.destory()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(prodcrowd_contract_root.visibility == View.VISIBLE){
                prodcrowd_contract_root.visibility = View.GONE
                return true
            }
            if(prodcrowd_dialog_root.visibility == View.VISIBLE){
                prodcrowd_dialog_root.visibility = View.GONE
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}
