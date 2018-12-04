package com.goodthings.app.activity.aftersaledetail

import android.os.Bundle
import com.goodthings.app.R
import com.goodthings.app.activity.aftersaledetail.fragment.aftersaleresult.AfterSaleResultFragment
import com.goodthings.app.activity.aftersaledetail.fragment.checking.AfterSaleCheckingfragment
import com.goodthings.app.activity.aftersaledetail.fragment.writeexpress.WriteExpressFragment
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.AfterSaleBean
import com.goodthings.app.util.onClick
import kotlinx.android.synthetic.main.top_bar.*

class AfterSaleDetailActivity :
        BaseActivity<AfterSaleDetailContract.AfterSaleDetailView,AfterSaleDetailContract.AfterSaleDetailPresenter>(),
        AfterSaleDetailContract.AfterSaleDetailView{
    override var presenter: AfterSaleDetailContract.AfterSaleDetailPresenter= AfterSaleDetailPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_sale_detail)
        toptitle.text = "售后详情"
        topback.onClick { onBackPressed() }
        presenter.loadAfterSaleOrderDetail()
    }

    override fun showContent(saleBean: AfterSaleBean?) {
        //type=1是拼团，状态关联cstatus
        //0初始页面，6进入填写快递单号页面，1退款中页面
        var transaction= supportFragmentManager.beginTransaction()
        saleBean?.let {
            if(it.type == 1){//拼团
                when(it.cstatus){
                    0,4 -> {//审核中
                        var checkingfragment= AfterSaleCheckingfragment()
                        var argument= Bundle()
                        argument.putParcelable("saleBean",it)
                        checkingfragment.arguments = argument
                        transaction.add(R.id.aftersale_framelayout,checkingfragment)
                    }
                    1,3,5 -> { //正在处理
                        toptitle.text = "售后处理结果"
                        var fragment= AfterSaleResultFragment()
                        var argument= Bundle()
                        argument.putParcelable("saleBean",it)
                        argument.putInt("state",it.cstatus)
                        fragment.arguments = argument
                        transaction.add(R.id.aftersale_framelayout,fragment)
                    }
                    2 -> { //退款成功
                        toptitle.text = "售后处理结果"
                        var fragment= AfterSaleResultFragment()
                        var argument= Bundle()
                        argument.putParcelable("saleBean",it)
                        argument.putInt("state",it.cstatus)
                        fragment.arguments = argument
                        transaction.add(R.id.aftersale_framelayout,fragment)
                    }
                    6 -> {//审核完成
                        if(it.apply_type == 0){//退款退货
                            //填写快递单号页面
                            toptitle.text = "填写快递号"
                            var fragment= WriteExpressFragment()
                            var argument= Bundle()
                            argument.putParcelable("saleBean",it)
                            fragment.arguments = argument
                            transaction.add(R.id.aftersale_framelayout,fragment)
                        }else{
                            toptitle.text = "售后处理结果"
                            var fragment= AfterSaleResultFragment()
                            var argument= Bundle()
                            argument.putParcelable("saleBean",it)
                            argument.putInt("state",it.cstatus)
                            fragment.arguments = argument
                            transaction.add(R.id.aftersale_framelayout,fragment)
                        }
                    }
                }
            }
            transaction.commit()
        }
    }
}
