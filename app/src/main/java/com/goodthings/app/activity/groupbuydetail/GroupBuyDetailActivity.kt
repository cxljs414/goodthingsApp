package com.goodthings.app.activity.groupbuydetail

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.KeyEvent
import android.view.View
import android.widget.CheckedTextView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.goodthings.app.R
import com.goodthings.app.activity.comments.AllCommentsActivity
import com.goodthings.app.activity.comments.AllCommentsContract
import com.goodthings.app.activity.grouporderpay.GroupOrderPayActivity
import com.goodthings.app.activity.login.LoginActivity
import com.goodthings.app.activity.shop.ShopActivity
import com.goodthings.app.activity.web.WebActivity
import com.goodthings.app.adapter.GroupBuyDetailDialogAdapter
import com.goodthings.app.adapter.GroupBuyDetailRecomAdapter
import com.goodthings.app.adapter.GroupCommentAdapter
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.*
import com.goodthings.app.util.*
import com.goodthings.app.view.AutoVerticalViewForGroup
import com.liji.imagezoom.util.ImageZoom
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_group_buy_detail.*
import kotlinx.android.synthetic.main.top_bar.*
import org.jetbrains.anko.imageResource

class GroupBuyDetailActivity :
        BaseActivity<GroupBuyDetailContract.GroupBuyDetailView,GroupBuyDetailContract.GroupBuyDetailPresenter>(),
        GroupBuyDetailContract.GroupBuyDetailView{

    override var presenter: GroupBuyDetailContract.GroupBuyDetailPresenter= GroupBuyDetailPresenterImpl()
    private var dialogAdapter: GroupBuyDetailDialogAdapter? = null
    private var recomAdapter:GroupBuyDetailRecomAdapter? = null
    private var commentAdapter: GroupCommentAdapter? = null
    private var isCollect:Boolean = false
    private var resumFromLogin:Boolean = false
    private var urlImageParser :URLImageParser ? = null
    private var cateFlowAdapter:TagAdapter<ProdBuyDetailBean>? = null
    private var subCateAdapter:TagAdapter<ProdBuyDetailSubBean>? = null
    private var groupBuyId= -1
    private var teamId= -1
    private var detailBean:GroupBuyDetailBean? = null
    private var buyDeailList:List<ProdBuyDetailBean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_buy_detail)
        toptitle.text = "拼团"
        topback.setOnClickListener {
            if(groupdetail_success_layout.visibility == View.VISIBLE){
                toptitle.text = "拼团"
                groupdetail_nomal_layout.visibility = View.VISIBLE
                groupdetail_success_layout.visibility = View.GONE
                groupdetail_bottom.visibility = View.VISIBLE
                presenter.collageQuery()
            }else{
                onBackPressed()
            }
        }
        top_right_btn.visibility = View.VISIBLE
        top_right_btn.setOnClickListener {
            if(detailBean != null){
                addShare(detailBean!!)
            }

        }
        top_right_image.setImageResource(R.mipmap.fenxiang4)

        initGroupDetail()
        initGroupingDialog()
        initRecomRecyclerview()
        groupBuyId = intent.getIntExtra("groupBuyId",-1)
        presenter.start(groupBuyId)

    }

    private fun initRecomRecyclerview() {
        groupdetail_recom_recyclerview.layoutManager = NoScrollGridLayoutManager(this,2)
        groupdetail_recom_recyclerview.addItemDecoration(GridSpacingItemDecoration(2,
                ScreenUtil.dip2px(this,15f),false))
        var imageWidth= (ScreenUtil.getScreenWidth(this) - ScreenUtil.dip2px(this,45f))/2
        recomAdapter = GroupBuyDetailRecomAdapter(ArrayList(),imageWidth)
        groupdetail_recom_recyclerview.adapter = recomAdapter
        recomAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if(view.id == R.id.item_crowdrecom_root){
                presenter.start((adapter.getItem(position) as GroupListBean).cId)
            }
        }


        groupbuy_comment_layout.onClick {
            var intent = Intent(this@GroupBuyDetailActivity,AllCommentsActivity::class.java)
            intent.putExtra("id",groupBuyId)
            startActivity(intent)
        }

        groupbuy_comment_recyclerview.layoutManager = NoScrollLinearLayoutManager(this)
        groupbuy_comment_recyclerview.addItemDecoration(SpacesItemDecorationtwo(0,ScreenUtil.dip2px(this,1f)))
        commentAdapter = GroupCommentAdapter(ArrayList())
        groupbuy_comment_recyclerview.adapter = commentAdapter
        commentAdapter?.setOnItemChildClickListener { adapter, view, position ->

        }
        commentAdapter?.listener = object :AllCommentsContract.OnCommentsCheckedListener {
            override fun onCommentsChecked(mId: Int) {
                presenter.addFabulous(mId)
            }

        }
    }

    private fun initGroupDetail() {
        var imageWidth= ScreenUtil.getScreenWidth(this)
        var imageheight= imageWidth*10/16
        groupdetail_coverpic.layoutParams = LinearLayout.LayoutParams(imageWidth,imageheight)

        groupbuy_dialog_close.setOnClickListener {
            groupbuy_grouping_root.visibility = View.GONE
        }
        groupbuy_grouping_root.setOnClickListener {  }

    }

    private fun initGroupingDialog() {
        groupbuy_dialog_recyclerview.layoutManager = LinearLayoutManager(this)
        groupbuy_dialog_recyclerview.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this,1f)))
        dialogAdapter = GroupBuyDetailDialogAdapter(ArrayList())
        groupbuy_dialog_recyclerview.adapter = dialogAdapter
        dialogAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if(Consts.isLogined) {
                groupbuy_grouping_root.visibility = View.GONE
                if (view.id == R.id.item_groupbuy_detail_dialog_ctbt) {
                    var bean= adapter?.getItem(position) as GroupingBean
                    if(bean.user_id == Consts.user?.id){
                        showMessage("不能跟自己拼团")
                    }else{
                        teamId =bean.tId
                        if (detailBean != null) {
                            showDialog(false, detailBean!!)
                        }
                    }

                }
            }else{
                goLogin()
            }
        }
    }

    override fun  showGroupDetailContent(bean: GroupBuyDetailBean){
        detailBean = bean
        toptitle.text = "拼团"
        groupdetail_nomal_layout.visibility = View.VISIBLE
        groupdetail_success_layout.visibility = View.GONE
        groupdetail_bottom.visibility = View.VISIBLE
        groupdetail_scrollview.scrollTo(0,0)

        groupdetail_buy_alone_price.text = "￥${bean.old_price}"
        groupdetail_buy_group_price.text = "￥${bean.price}"

        Glide.with(this)
                .load(Consts.IMAGEURL+bean.cover_url)
                .into(groupdetail_coverpic)
        groupdetail_price.text = "￥${bean.price}"
        groupdetail_sellcount.text = "已团${bean.virtual_num}件·${bean.person_num}人拼团"
        groupdetail_title.text = bean.title
        groupdetail_yqcount.text = "支付并邀请${bean.person_num - 1}人参团，人数不足自动退款"
        Glide.with(this)
                .load(Consts.IMAGEURL+bean.head_url)
                .into(groupdetail_shop_img)
        groupdetail_shop_name.text = bean.nickname
        groupdetail_shop_content.text = "商品数量${bean.stock}件，已拼${bean.virtual_num}件"
        groupdetail_shop_gobt.setOnClickListener {
            var intent = Intent(this@GroupBuyDetailActivity, ShopActivity::class.java)
            intent.putExtra("merId",bean.mer_id)
            intent.putExtra("type",1)
            startActivity(intent)
        }
        groupdetail_bottom_shop.setOnClickListener {
            var intent = Intent(this@GroupBuyDetailActivity, ShopActivity::class.java)
            intent.putExtra("merId",bean.mer_id)
            intent.putExtra("type",1)
            startActivity(intent)
        }

        if(bean.remark == null || bean.remark.isEmpty()){
            groupdetail_html_content.visibility = View.GONE
        }else {
            groupdetail_html_content.visibility = View.VISIBLE
            var width = ScreenUtil.getScreenWidth(this) - ScreenUtil.dip2px(this,30f)
            urlImageParser = URLImageParser(this@GroupBuyDetailActivity,groupdetail_html_content,width)
            groupdetail_html_content.text = Html.fromHtml(bean.remark, urlImageParser,null)
        }

        groupdetail_xihuan_img.setOnClickListener {
            presenter.requestChangeCollect(isCollect)
        }

        groupdetail_bottom_likecount.setOnClickListener {
            presenter.requestChangeCollect(isCollect)
        }

        groupdetail_share_img.setOnClickListener {
            if(detailBean != null){
                addShare(detailBean!!)
            }
        }

        groupdetail_bottom_sharecount.setOnClickListener {
            if(detailBean != null){
                addShare(detailBean!!)
            }
        }

        groupdetail_buy_alone.setOnClickListener {
            if(Consts.isLogined) {
                teamId = -1
                var url = Consts.URL + "mallPrt/unauth/toPrdDetail?id=${bean.spu_id}&userId=${Consts.user?.id}"
                var intent = Intent(this@GroupBuyDetailActivity, WebActivity::class.java)
                intent.putExtra("url", url)
                startActivity(intent)
            }else{
                goLogin()
            }
        }

        groupdetail_buy_group.setOnClickListener {
            if(Consts.isLogined) {
                teamId = -1
                //拼团
                showDialog(false, bean)
            }else{
                goLogin()
            }
        }

    }

    override fun notifyTeamsListUpdate(it: List<GroupingBean>?) {
        if(it == null || it.isEmpty()){
            groupdetail_teamslayout.visibility = View.GONE
        }else{
            groupdetail_teamslayout.visibility = View.VISIBLE
            groupdetail_autoview.setViews(it)
            groupdetail_autoview.setOnItemClickListener(object : AutoVerticalViewForGroup.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    if(Consts.isLogined) {
                        groupbuy_grouping_root.visibility = View.GONE
                        if(it[position].user_id == Consts.user?.id){
                            showMessage("不能跟自己拼团")
                        }else{
                            teamId = it[position].tId
                            if(detailBean != null){
                                showDialog(false,detailBean!!)
                            }
                        }

                    }else{
                        goLogin()
                    }

                }
            })

            groupdetail_autoview_top.setOnClickListener {
                showGroupingDialog()
            }
            dialogAdapter?.setNewData(it)

        }

    }

    private fun showGroupingDialog(){
        groupbuy_grouping_root.visibility = View.GONE
        groupbuy_grouping_root.alpha = 0f
        groupbuy_grouping_root.visibility = View.VISIBLE
        groupbuy_grouping_root.animate().alpha(1f).setDuration(300).start()
    }

    override fun shareCountChange() {
        var countStr:String= groupdetail_share_count.text.toString()
        var shareCount= 0
        if(countStr != ""){
            shareCount = Integer.parseInt(countStr)
        }
        shareCount+=1
        groupdetail_share_count.text = "$shareCount"
    }

    override fun showCollectCount(it: Int?) {
        groupdetail_xihuan_count.text = "$it"
    }

    override fun showShareCount(it: Int?) {
        groupdetail_share_count.text = "$it"
    }

    override fun showComment(pageList: List<CommentBean>?) {
        if(pageList == null || pageList.isEmpty()){
            groupbuy_comment.visibility = View.GONE
        }else{
            groupbuy_comment.visibility = View.VISIBLE
            commentAdapter?.setNewData(pageList)
            commentAdapter?.notifyDataSetChanged()
        }
    }

    override fun changeCollect(isCollect: Boolean, needAdd: Boolean) {
        this.isCollect = isCollect
        var likeCount= Integer.parseInt(groupdetail_xihuan_count.text.toString())
        if(isCollect){
            groupdetail_xihuan_img.imageResource = R.mipmap.like_selected1
            var top: Drawable = resources.getDrawable(R.mipmap.xihuan2_red)
            top.setBounds(0, 0, top.minimumWidth, top.minimumHeight)
            groupdetail_bottom_likecount.setCompoundDrawables(null,top,null,null)
            groupdetail_bottom_likecount.text = "已喜欢"
            if(needAdd) likeCount+=1
        }else{
            groupdetail_xihuan_img.imageResource = R.mipmap.like_nomal1
            var top: Drawable = resources.getDrawable(R.mipmap.xihuan2)
            top.setBounds(0, 0, top.minimumWidth, top.minimumHeight)
            groupdetail_bottom_likecount.setCompoundDrawables(null,top,null,null)
            groupdetail_bottom_likecount.text = "喜欢"
            if(needAdd){
                likeCount-=1
                if(likeCount<0)likeCount = 0
            }
        }
        groupdetail_xihuan_count.text = "$likeCount"
    }

    override fun goLogin() {
        resumFromLogin = true
        startActivity(Intent(this@GroupBuyDetailActivity, LoginActivity::class.java))
    }

    override fun notifyGroupBuyListUpdate(list: MutableList<GroupListBean>) {
        recomAdapter?.setNewData(list)
        recomAdapter?.notifyDataSetChanged()
    }

    fun showDialog(isAlone:Boolean,bean:GroupBuyDetailBean){
        showProdBuyDetail(buyDeailList)
        grouppay_dialog_root.alpha = 0f
        grouppay_dialog_root.visibility = View.VISIBLE
        grouppay_dialog_root.animate().alpha(1f).setDuration(300).start()
        grouppay_dialog_root.setOnClickListener {  }
        grouppay_dialog_close.setOnClickListener {
            grouppay_dialog_root.visibility = View.GONE
        }
        if(subCate != null){
            Glide.with(this).load(Consts.IMAGEURL+subCate?.cover_url).into(grouppay_dialog_image)
            grouppay_dialog_image.setOnClickListener {
                ImageZoom.show(this@GroupBuyDetailActivity,Consts.IMAGEURL+subCate?.cover_url)
            }
        }else{
            Glide.with(this).load(Consts.IMAGEURL+bean.cover_url).into(grouppay_dialog_image)
            grouppay_dialog_image.setOnClickListener {
                ImageZoom.show(this@GroupBuyDetailActivity,Consts.IMAGEURL+bean.cover_url)
            }
        }
        grouppay_dialog_title.text = bean.title
        if(isAlone){
            grouppay_dialog_price.text = "${bean.old_price}元"
            grouppay_dialog_submmit.text = "￥${bean.old_price}确定"
        }else{
            grouppay_dialog_price.text = "${bean.price}元"
            grouppay_dialog_submmit.text = "￥${bean.price}确定"
        }

        grouppay_dialog_stock.text = "目前剩余${bean.stock}件"
        grouppay_dialog_count.text = "1"
        var curCount= Integer.parseInt(grouppay_dialog_count.text.toString())
        var curStock= bean.stock
        grouppay_dialog_reduce.setOnClickListener {
            curCount-=1
            if(curCount<1)curCount =1
            grouppay_dialog_count.text = "$curCount"
            if(isAlone){
                grouppay_dialog_submmit.text = "￥${bean.old_price*curCount}确定"
            }else{
                grouppay_dialog_submmit.text = "￥${bean.price*curCount}确定"
            }

        }
        grouppay_dialog_add.setOnClickListener {
            curCount+=1
            if (curCount > curStock) {
                curCount = curStock
                showMessage("库存剩余${curStock}件")
            }
            grouppay_dialog_count.text = "$curCount"

            if(isAlone){
                grouppay_dialog_submmit.text = "￥${(bean.old_price*curCount*100).toInt()/100}确定"
            }else{
                grouppay_dialog_submmit.text = "￥${(bean.price*curCount*100).toInt()/100}确定"
            }
        }
        grouppay_dialog_submmit.setOnClickListener {
            if(Consts.isLogined) {
                //到支付
                var count = Integer.parseInt(grouppay_dialog_count.text.toString())
                var intent = Intent(this@GroupBuyDetailActivity, GroupOrderPayActivity::class.java)
                intent.putExtra("groupId", bean.cId)
                intent.putExtra("count", count)
                intent.putExtra("isAlone", isAlone)
                startActivityForResult(intent, Consts.REQUEST_ORDER_PAY)
                grouppay_dialog_root.visibility = View.GONE
            }else{
                goLogin()
            }
        }

    }

    override fun updateIsBuy(isBuyed: Boolean?) {
        if(!isBuyed!!){
            groupdetail_buy_share.visibility = View.VISIBLE
            groupdetail_buy_layout.visibility = View.GONE
            presenter.shareParam(detailBean?.cId)
        }else{
            groupdetail_buy_share.visibility = View.GONE
            groupdetail_buy_layout.visibility = View.VISIBLE
        }
    }

    private var preCate:ProdBuyDetailBean?=null
    private var subCate:ProdBuyDetailSubBean? = null
    override fun showProdBuyDetail(it: List<ProdBuyDetailBean>?) {
        buyDeailList = it
        if(preCate == null){
            preCate = it?.get(0)
            subCate = it?.get(0)!!.subDetailList[0]
            changeDialogContent(preCate!!,0)
        }
        grouppay_dialog_subcatelayout.visibility = View.GONE
        cateFlowAdapter = object:TagAdapter<ProdBuyDetailBean>(it){
            override fun getView(parent: FlowLayout?, position: Int, t: ProdBuyDetailBean?): View {
                val tv = layoutInflater.inflate(R.layout.tv_cate, grouppay_dialog_cateflow, false) as CheckedTextView
                tv.text = t?.cateName
                if(preCate?.id == t?.id){
                    tv.isChecked = true
                    changeSubCateAdapter(it?.get(position))
                }else{
                    tv.isChecked = false
                }
                return tv
            }

            override fun onSelected(position: Int, view: View?) {
                super.onSelected(position, view)
                preCate = it!![position]
                subCate = it!![position].subDetailList!![0]
                changeSubCateAdapter(it?.get(position))
                cateFlowAdapter?.notifyDataChanged()
            }

            override fun unSelected(position: Int, view: View?) {
                super.unSelected(position, view)
                grouppay_dialog_subcatelayout.visibility = View.GONE
            }
        }
        grouppay_dialog_cateflow.adapter =cateFlowAdapter


    }

    private fun changeSubCateAdapter(it: ProdBuyDetailBean?) {
        grouppay_dialog_subcatelayout.visibility = View.VISIBLE
        subCateAdapter = object:TagAdapter<ProdBuyDetailSubBean>(it?.subDetailList){
            override fun getView(parent: FlowLayout?, position: Int, t: ProdBuyDetailSubBean?): View {
                val tv = layoutInflater.inflate(R.layout.tv_cate, grouppay_dialog_subcate, false) as CheckedTextView
                tv.text = t?.standard
                if(subCate?.skuDetailId == t?.skuDetailId){
                    tv.isChecked = true
                    changeDialogContent(it!!,position)
                }else{
                    tv.isChecked = false
                }
                return tv
            }
            override fun onSelected(position: Int, view: View?) {
                super.onSelected(position, view)
                subCate = it?.subDetailList!![position]
                changeDialogContent(it!!,position)
            }

            override fun unSelected(position: Int, view: View?) {
                super.unSelected(position, view)
            }
        }

        grouppay_dialog_subcate.adapter = subCateAdapter
    }

    private fun changeDialogContent(proddetailBean: ProdBuyDetailBean, position: Int) {
        var subBean= proddetailBean.subDetailList[position]
        Glide.with(this).load(Consts.IMAGEURL+subCate?.cover_url).into(grouppay_dialog_image)
        grouppay_dialog_image.setOnClickListener {
            ImageZoom.show(this@GroupBuyDetailActivity,Consts.IMAGEURL+subCate?.cover_url)
        }
        grouppay_dialog_stock.text = "目前剩余${subBean.stock}件"
        grouppay_dialog_price.text = "${detailBean?.price}元"
        grouppay_dialog_submmit.text = "￥${detailBean?.price}确定"
        var curCount= Integer.parseInt(grouppay_dialog_count.text.toString())
        grouppay_dialog_submmit.text = "￥${(detailBean?.price!!.toDouble()*curCount*100).toInt()*1.00/100}确定"
        var curStock= Integer.parseInt(subBean.stock)
        grouppay_dialog_reduce.setOnClickListener {
            curCount-=1
            if(curCount<1)curCount =1
            grouppay_dialog_count.text = "$curCount"
            grouppay_dialog_submmit.text = "￥${(detailBean?.price!!.toDouble()*curCount*100).toInt()*1.00/100}确定"

        }
        grouppay_dialog_add.setOnClickListener {
            curCount+=1
            if (curCount >curStock ) {
                curCount = curStock
                showMessage("库存剩余${curStock}件")
            }
            grouppay_dialog_count.text = "$curCount"
            grouppay_dialog_submmit.text = "￥${(detailBean?.price!!.toDouble()*curCount*100).toInt()*1.00/100}确定"
        }

        grouppay_dialog_submmit.setOnClickListener {
            if(Consts.isLogined) {
                //到支付
                var count = Integer.parseInt(grouppay_dialog_count.text.toString())
                var intent = Intent(this@GroupBuyDetailActivity, GroupOrderPayActivity::class.java)
                intent.putExtra("groupId", groupBuyId)
                intent.putExtra("detailId", proddetailBean.subDetailList[position].skuDetailId)
                intent.putExtra("count", count)
                intent.putExtra("isAlone", false)
                intent.putExtra("teamId", teamId)
                startActivityForResult(intent, Consts.REQUEST_ORDER_PAY)
                grouppay_dialog_root.visibility = View.GONE
            }else{
                goLogin()
            }
        }
    }

    fun addShare(detailBean:GroupBuyDetailBean) {
        if(Consts.isLogined) {
            var url = Consts.URL + "collage/unauth/collageDetail?collageId=" + detailBean.cId
            openShareWindow(this, groupbuy_root, url,
                    detailBean.title,detailBean.title, detailBean.cover_url, object : UMShareListener {
                override fun onCancel(p0: SHARE_MEDIA?) {

                }

                override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                }

                override fun onStart(p0: SHARE_MEDIA?) {
                }

                override fun onResult(p0: SHARE_MEDIA?) {
                    presenter.addShare(detailBean.cId)
                }
            })
        }else{
            goLogin()
        }
    }

    override fun notifyShare(shareParamBean: ShareParamBean?) {
        groupdetail_success_count.text = "还差${shareParamBean?.count}人"
        groupdetail_success_share.setOnClickListener {
            addSuccessShare(shareParamBean)
        }
        groupdetail_buy_share.setOnClickListener {
            addSuccessShare(shareParamBean)
        }
    }

    fun addSuccessShare(shareParamBean: ShareParamBean?) {
        if(Consts.isLogined) {
            var url = Consts.URL + "collage/unauth/collageShare?collageId=${shareParamBean?.collageId}&tId=${shareParamBean?.tId}&userId=${Consts.user?.id}"
            openShareWindow(this, groupbuy_root, url,
                    detailBean?.title!!,detailBean?.title!!, detailBean?.cover_url!!, object : UMShareListener {
                override fun onCancel(p0: SHARE_MEDIA?) {

                }

                override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                }

                override fun onStart(p0: SHARE_MEDIA?) {
                }

                override fun onResult(p0: SHARE_MEDIA?) {
                }
            })
        }else{
            goLogin()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Consts.REQUEST_ORDER_PAY){
            if(resultCode == Activity.RESULT_OK){
                var str= "发起拼团成功"
                if(data!= null&&data.hasExtra("teamId")){
                    if(data.getIntExtra("teamId",-1) != -1){
                        str = "参与拼团成功"
                    }
                }
                groupdetail_nomal_layout.visibility = View.GONE
                groupdetail_success_layout.visibility = View.VISIBLE
                groupdetail_bottom.visibility = View.GONE
                toptitle.text =str
                presenter.tjCollageQuery()
                if(data!= null && data.hasExtra("addOrdered")){
                    if(data.getBooleanExtra("addOrdered",false)){
                        presenter.shareParam(detailBean?.cId)
                    }
                }
                presenter.getPrdDetail(detailBean?.spu_id)
            }
            presenter.getIsBuyed()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(groupbuy_grouping_root.visibility == View.VISIBLE){
                groupbuy_grouping_root.visibility = View.GONE
                return true
            }

            if(grouppay_dialog_root.visibility == View.VISIBLE){
                grouppay_dialog_root.visibility = View.GONE
                return true
            }

            if(groupdetail_success_layout.visibility == View.VISIBLE){
                toptitle.text = "拼团"
                groupdetail_nomal_layout.visibility = View.VISIBLE
                groupdetail_success_layout.visibility = View.GONE
                groupdetail_bottom.visibility = View.VISIBLE
                presenter.collageQuery()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        if(resumFromLogin){
            if(Consts.isLogined) {
                presenter.getIsCollect(false)
                presenter.getIsBuyed()
                presenter.colTeamQuery()
            }
        }
        resumFromLogin = false
    }

    override fun onDestroy() {
        super.onDestroy()
        urlImageParser?.destory()
        dialogAdapter?.destory()
    }

}
