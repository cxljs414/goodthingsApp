package com.goodthings.app.activity.aftersaledetail.fragment.aftersaleresult

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goodthings.app.R
import com.goodthings.app.activity.express.ExpressActivity
import com.goodthings.app.bean.AfterSaleBean
import com.goodthings.app.util.onClick
import com.goodthings.app.util.toast
import com.trello.rxlifecycle2.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_aftersale_result.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/24
 * 修改内容：
 * 最后修改时间：
 */
class AfterSaleResultFragment : RxFragment() {
    private var afterSaleBean: AfterSaleBean? = null
    private var state:Int = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_aftersale_result,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterSaleBean = arguments?.getParcelable("saleBean")
        state = arguments?.getInt("state")!!
        afterSaleBean.let {
            when(state){
                1,3 -> {
                    frag_aftersale_result_waiting.visibility = View.VISIBLE
                    frag_aftersale_result_success.visibility = View.GONE
                    frag_aftersale_result_cancel.visibility = View.GONE
                }

                2 -> {
                    frag_aftersale_result_waiting.visibility = View.GONE
                    frag_aftersale_result_success.visibility = View.VISIBLE
                    frag_aftersale_result_cancel.visibility = View.GONE
                }

                5 -> {
                    frag_aftersale_result_waiting.visibility = View.GONE
                    frag_aftersale_result_success.visibility = View.GONE
                    frag_aftersale_result_cancel.visibility = View.VISIBLE
                }
            }

            if(it?.apply_type == 0){
                frag_aftersale_result_express.visibility = View.VISIBLE
                frag_aftersale_result_clfs.text = "我要退款退货"
            }else{
                frag_aftersale_result_express.visibility = View.GONE
                frag_aftersale_result_clfs.text = "我要退款（无需退货）"
            }

            frag_aftersale_result_tkyy.text = it?.reason
            frag_aftersale_result_tksm.text = it?.remark
            frag_aftersale_result_tkje.text = "￥${it?.return_amt}"

            frag_aftersale_result_express.onClick {
                if(afterSaleBean?.logis_no.isNullOrEmpty()){
                    context?.toast("暂无物流信息")
                    return@onClick
                }
                var intent= Intent(activity,ExpressActivity::class.java)
                intent.putExtra("orderNo",afterSaleBean?.order_no)
                intent.putExtra("billNo",afterSaleBean?.way_bill_no)
                intent.putExtra("shipperCode",afterSaleBean?.logis_no)
                intent.putExtra("loginCompany",afterSaleBean?.logis_company)
                startActivity(intent)
            }
        }
    }
}