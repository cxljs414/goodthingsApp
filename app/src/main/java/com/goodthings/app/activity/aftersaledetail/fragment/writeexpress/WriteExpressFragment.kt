package com.goodthings.app.activity.aftersaledetail.fragment.writeexpress

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.goodthings.app.R
import com.goodthings.app.base.BaseFragment
import com.goodthings.app.bean.AfterSaleBean
import com.goodthings.app.bean.LogisBean
import com.goodthings.app.util.onClick
import com.goodthings.app.util.textChange
import kotlinx.android.synthetic.main.fragment_aftersale_writeexpress.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/24
 * 修改内容：
 * 最后修改时间：
 */
class WriteExpressFragment :
        BaseFragment<WriteExpressContract.WriteExPressView,WriteExpressContract.WriteExpressPresenter>(),
        WriteExpressContract.WriteExPressView{
    override var presenter: WriteExpressContract.WriteExpressPresenter = WriteExpressPresenterImpl()
    private var afterSaleBean: AfterSaleBean? = null
    private var selectLogis:LogisBean?= null
    private var expressNo=""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_aftersale_writeexpress,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterSaleBean = arguments?.getParcelable("saleBean")
        frag_aftersale_express_lc.text = "商家退货地址：\n 收货人：李春艳 \n 手机：15512124145 \n 地址：北京市丰台区西三环红木街"
        if(!afterSaleBean?.way_bill_no.isNullOrEmpty()){
            frag_aftersale_express_orderno.text = Editable.Factory.getInstance().newEditable(afterSaleBean?.way_bill_no)
        }
        presenter.start()
    }

    override fun showLogisList(logs: List<LogisBean>) {
        selectLogis = logs[0]
        var logisList:MutableList<String> = ArrayList()
        logs.forEach { logisList.add(it.name)}
        var adapter= ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,logisList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        frag_aftersale_express_company.adapter =adapter
        frag_aftersale_express_company.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectLogis= logs[position]
            }
        }

        frag_aftersale_express_orderno.textChange {
            expressNo= it
        }

        frag_aftersale_express_commit.onClick {
            if(frag_aftersale_express_orderno.text.toString().isNullOrEmpty()){
                showMessage("请填写快递单号")
            }else{
                presenter.expressCommit(selectLogis!!,frag_aftersale_express_orderno.text.toString(),afterSaleBean?.order_no!!)
            }
        }

    }
}