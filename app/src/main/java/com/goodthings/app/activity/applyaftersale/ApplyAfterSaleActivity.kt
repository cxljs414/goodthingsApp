package com.goodthings.app.activity.applyaftersale

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.goodthings.app.R
import com.goodthings.app.activity.aftersaledetail.AfterSaleDetailActivity
import com.goodthings.app.activity.selectpic.SelectPicActivity
import com.goodthings.app.adapter.ApplyDialogAdapter
import com.goodthings.app.adapter.ApplyPicAdapter
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.AfterSaleBean
import com.goodthings.app.bean.GroupOrderDetailBean
import com.goodthings.app.util.*
import com.liji.imagezoom.util.ImageZoom
import kotlinx.android.synthetic.main.activity_apply_after_sale.*
import kotlinx.android.synthetic.main.top_bar.*

class ApplyAfterSaleActivity :
        BaseActivity<ApplyAfterSaleContract.ApplyAfterSaleView,ApplyAfterSaleContract.ApplyAfterSalePresenter>(),
        ApplyAfterSaleContract.ApplyAfterSaleView{
    override var presenter: ApplyAfterSaleContract.ApplyAfterSalePresenter = ApplyAfterSalePresenterImpl()
    private var typeadapter: ApplyDialogAdapter? = null
    private var statusadapter: ApplyDialogAdapter? = null
    private var reasonadapter: ApplyDialogAdapter? = null
    private var picAdapter:ApplyPicAdapter? = null
    private var typeDialog:BottomSheetDialog? = null
    private var statusDialog:BottomSheetDialog? = null
    private var reasonDialog:BottomSheetDialog? = null
    private var picsList:MutableList<String> = ArrayList()
    private var applyType:Int = -1
    private var applyStatus:Int= -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_after_sale)
        toptitle.text = "申请售后"
        topback.onClick { onBackPressed() }
        initApply()
        initTypeDialog()
        initStatusDialog()
        initReasonDialog()
        presenter.start()
    }

    private fun initApply() {

        applysale_type.onClick {
            typeDialog?.show()
        }

        applysale_status.onClick {
            statusDialog?.show()
        }

        applysale_cause.onClick {
            reasonDialog?.show()
        }

        applysale_explain_content.textChange {
            applysale_explain_wordcount.text = "您还可在此输入${200-it.length}字"
        }

        applysale_pic_recyclerview.layoutManager = GridLayoutManager(this,3)
        applysale_pic_recyclerview.addItemDecoration(GridSpacingItemDecoration(3,ScreenUtil.dip2px(this,10f),false))
        picsList.add("add")
        picAdapter = ApplyPicAdapter(picsList)
        applysale_pic_recyclerview.adapter = picAdapter
        picAdapter?.addListener= object:ApplyAfterSaleContract.OnAddPicListener{
            override fun onAddpic(positon: Int) {
                selectPic()
            }
        }
        picAdapter?.seeListener = object:ApplyAfterSaleContract.OnSeePicListener{
            override fun onSeePic(item: String?) {
                ImageZoom.show(this@ApplyAfterSaleActivity,Consts.IMAGEURL+item)
            }
        }
        picAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if(view.id == R.id.item_applypics_close) {
                picsList.removeAt(position)
                if (!picsList.contains("add")) {
                    picsList.add("add")
                }
                picAdapter?.notifyDataSetChanged()
            }
        }

        applysale_commit.onClick {
            if(applysale_type.text.toString().isNullOrEmpty()){
                showMessage("请选择申请类型")
            }else if(applysale_status.text.toString().isNullOrEmpty()){
                showMessage("请选择收货状态")
            }else if(applysale_cause.text.toString().isNullOrEmpty()){
                showMessage("请选择申请原因")
            }else if(applysale_explain_content.text.toString().isNullOrEmpty()){
                showMessage("请填写申请说明")
            }else if(applysale_explain_phone.text.toString().isNullOrEmpty()){
                showMessage("请填写联系电话")
            }else{
                presenter.commitApply(applyType,applyStatus,
                        applysale_cause.text.toString(),
                        applysale_explain_content.text.toString(),
                        applysale_explain_phone.text.toString(),
                        picsList)
            }
        }

    }

    override fun showGroupOrderContent(it: GroupOrderDetailBean?) {
        it.let {
            Glide.with(this@ApplyAfterSaleActivity)
                    .load(Consts.IMAGEURL+it?.cover_url)
                    .into(applysale_topbar_proimage)
            applysale_topbar_proname.text = it?.title
            applysale_topbar_procount.text = "数量: x${it?.buy_num} "
            applysale_topbar_proprice.text = "价格：￥${it?.price}"
            applysale_money.text = "￥${it?.price!! * it?.buy_num!!}"
        }
    }

    override fun showApplyContent(it: AfterSaleBean?) {
        //修改售后申请，先把之前的内容展示出来
        it.let {
            if(it?.apply_type == 0){
                applyType = 0
                applysale_type.text="我要退款退货"
                typeadapter?.checkedStr ="我要退货退款"
            }else{
                applyType = 1
                applysale_type.text="我要退款（无需退货）"
                typeadapter?.checkedStr = "我要退款（无需退货）"
            }
            applyStatus = it?.goods_state!!
            if(it?.goods_state == 0){
                applysale_status.text = "未收到货"
                statusadapter?.checkedStr = "未收到货"
            }else{
                applysale_status.text = "已收到货"
                statusadapter?.checkedStr = "已收到货"
            }

            applysale_cause.text = it.reason
            reasonadapter?.checkedStr = it.reason

            applysale_explain_content.text = Editable.Factory.getInstance().newEditable("${it.remark}")
            applysale_explain_wordcount.text = "您还可在此输入${200-it.remark.length}字"
            applysale_explain_phone.text = Editable.Factory.getInstance().newEditable("${it.phone}")
            picsList.clear()
            var index= 0
            it.pic_url.split(",").forEach {
                if(it.isNotEmpty() && it != "add"){
                    picsList.add(index++,it)
                }
            }

            if(picsList.size <3){
                picsList.add("add")
            }

            picAdapter?.notifyDataSetChanged()
        }

    }

    private fun selectPic(){
        var intent= Intent(this@ApplyAfterSaleActivity, SelectPicActivity::class.java)
        intent.putExtra("width",105)
        intent.putExtra("height",105)
        startActivityForResult(intent, Consts.REQUEST_SELECT_PIC)
        overridePendingTransition(R.anim.enter_anim_alpha,0)
    }

    override fun addPic(data: String) {
        picsList.remove("add")
        if(picsList.size < 3){
            picsList.add(data)
        }
        if(picsList.size < 3){
            picsList.add("add")
        }
        picAdapter?.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if(requestCode == Consts.REQUEST_SELECT_PIC){
                presenter.uploadImage(data?.data)
            }
        }
    }

    override fun commitSuccess() {
        var orderNo= intent.getStringExtra("orderNo")
        var intent = Intent(this@ApplyAfterSaleActivity, AfterSaleDetailActivity::class.java)
        intent.putExtra("type",1)
        intent.putExtra("orderNo",orderNo)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        typeDialog = null
        statusDialog = null
        reasonDialog = null
    }

    private fun initTypeDialog() {
        typeDialog = BottomSheetDialog(this)
        var view: View = View.inflate(this,R.layout.dialog_applyaftersale_type,null)
        var name= view.findViewById<TextView>(R.id.dialog_apply_typename)
        name.text = "申请类型"
        var typeRecyclerview = view.findViewById<RecyclerView>(R.id.dialog_apply_recyclerview)
        typeRecyclerview.layoutManager = NoScrollLinearLayoutManager(this)
        typeRecyclerview.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this,1f)))
        var types:List<String> = resources.getStringArray(R.array.apply_aftersale_type).toList()
        typeadapter= ApplyDialogAdapter(types,types[0])
        typeRecyclerview.adapter = typeadapter
        typeadapter?.setOnItemChildClickListener({
            adapter,view,position->
            applysale_type.text = types[position]
            applyType = position
            typeadapter?.checkedStr = types[position]
            typeadapter?.notifyDataSetChanged()
            typeDialog?.dismiss()
        })
        var close= view.findViewById<TextView>(R.id.dialog_apply_close)
        close.onClick {
            typeDialog?.dismiss()
        }
        typeDialog?.setContentView(view)

    }
    private fun initStatusDialog(){
        statusDialog = BottomSheetDialog(this)
        var view: View = View.inflate(this,R.layout.dialog_applyaftersale_type,null)
        var name= view.findViewById<TextView>(R.id.dialog_apply_typename)
        name.text = "货物状态"
        var typeRecyclerview = view.findViewById<RecyclerView>(R.id.dialog_apply_recyclerview)
        typeRecyclerview.layoutManager = NoScrollLinearLayoutManager(this)
        typeRecyclerview.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this,1f)))
        var types:List<String> = resources.getStringArray(R.array.apply_aftersale_status).toList()
        statusadapter= ApplyDialogAdapter(types,types[0])
        typeRecyclerview.adapter = statusadapter
        statusadapter?.setOnItemChildClickListener({
            adapter,view,position->
            applysale_status.text = types[position]
            applyStatus = position
            statusadapter?.checkedStr = types[position]
            statusadapter?.notifyDataSetChanged()
            statusDialog?.dismiss()
        })
        var close= view.findViewById<TextView>(R.id.dialog_apply_close)
        close.onClick {
            statusDialog?.dismiss()
        }
        statusDialog?.setContentView(view)
    }
    private fun initReasonDialog(){
        reasonDialog = BottomSheetDialog(this)
        var view: View = View.inflate(this,R.layout.dialog_applyaftersale_type,null)
        var name= view.findViewById<TextView>(R.id.dialog_apply_typename)
        name.text = "退款原因"
        var typeRecyclerview = view.findViewById<RecyclerView>(R.id.dialog_apply_recyclerview)
        typeRecyclerview.layoutManager = NoScrollLinearLayoutManager(this)
        typeRecyclerview.addItemDecoration(SpacesItemDecorationtwo(0, ScreenUtil.dip2px(this,1f)))
        var types:List<String> = resources.getStringArray(R.array.apply_aftersale_reason).toList()
        reasonadapter= ApplyDialogAdapter(types,types[0])
        typeRecyclerview.adapter = reasonadapter
        reasonadapter?.setOnItemChildClickListener({
            adapter,view,position->
            applysale_cause.text = types[position]
            reasonadapter?.checkedStr = types[position]
            reasonadapter?.notifyDataSetChanged()
            reasonDialog?.dismiss()
        })
        var close= view.findViewById<TextView>(R.id.dialog_apply_close)
        close.onClick {
            reasonDialog?.dismiss()
        }
        reasonDialog?.setContentView(view)
    }


}
