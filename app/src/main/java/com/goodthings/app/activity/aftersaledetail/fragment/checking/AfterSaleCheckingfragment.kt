package com.goodthings.app.activity.aftersaledetail.fragment.checking

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goodthings.app.R
import com.goodthings.app.activity.applyaftersale.ApplyAfterSaleActivity
import com.goodthings.app.base.BaseFragment
import com.goodthings.app.bean.AfterSaleBean
import com.goodthings.app.util.onClick
import kotlinx.android.synthetic.main.fragment_aftersale_checking.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/23
 * 修改内容：
 * 最后修改时间：
 */
class AfterSaleCheckingfragment :
        BaseFragment<AfterSaleCheckingContract.AfterSaleCheckingView, AfterSaleCheckingContract.AfterSaleCheckingPresenter>(),
        AfterSaleCheckingContract.AfterSaleCheckingView {
    override var presenter: AfterSaleCheckingContract.AfterSaleCheckingPresenter = AfterSaleCheckingPresenterImpl()
    private var afterSaleBean:AfterSaleBean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_aftersale_checking,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterSaleBean = arguments?.getParcelable("saleBean")
        afterSaleBean.let {

            if(afterSaleBean?.type == 1){
                when(afterSaleBean?.cstatus){
                    0 ->{
                        frag_aftersale_checking_image.visibility = View.VISIBLE
                        frag_aftersale_checking_title.text = "等待商家处理售后申请"
                        frag_aftersale_checking_content.text = "退款申请流程：\n 1.发起退款申请 \n 2.商家确认后退款到您的账户，如果商家未处理，请及时与商家联系"
                        frag_aftersale_checking_cancel.visibility = View.VISIBLE
                        frag_aftersale_checking_update.visibility = View.VISIBLE
                        frag_aftersale_checking_again.visibility = View.GONE

                    }
                    4 -> {//被驳回
                        frag_aftersale_checking_image.visibility = View.GONE
                        frag_aftersale_checking_title.text = "售后申请被商家驳回"
                        frag_aftersale_checking_content.text = "驳回原因："+afterSaleBean?.crefund_fail_reason
                        frag_aftersale_checking_cancel.visibility = View.GONE
                        frag_aftersale_checking_update.visibility = View.GONE
                        frag_aftersale_checking_again.visibility = View.VISIBLE
                    }
                }
            }

            if(it?.apply_type == 0){
                frag_aftersale_checking_clfs.text ="退款退货"
            }else{
                frag_aftersale_checking_clfs.text ="退款"
            }
            frag_aftersale_checking_tkyy.text = it?.reason
            frag_aftersale_checking_tksm.text = it?.remark
            frag_aftersale_checking_tkje.text = "${it?.return_amt}"
            frag_aftersale_checking_tksj.text = it?.create_time


            frag_aftersale_checking_cancel.onClick {
                presenter.cancelApply(afterSaleBean?.applyId)
            }

            frag_aftersale_checking_update.onClick {
                var intent= Intent(activity,ApplyAfterSaleActivity::class.java)
                intent.putExtra("applyId",afterSaleBean?.applyId)
                intent.putExtra("type",afterSaleBean?.type)
                intent.putExtra("orderNo",afterSaleBean?.order_no)
                intent.putExtra("returnOrderNo",afterSaleBean?.return_order_no)
                context?.startActivity(intent)
                activity?.finish()
            }

            frag_aftersale_checking_again.onClick {
                presenter.againApply(afterSaleBean?.applyId)
            }
        }

    }

}