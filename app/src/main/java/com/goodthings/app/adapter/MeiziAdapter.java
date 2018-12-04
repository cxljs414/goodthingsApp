package com.goodthings.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.goodthings.app.R;
import com.goodthings.app.application.Consts;
import com.goodthings.app.bean.HomeRecomSubBean;
import com.goodthings.app.view.RoundAngleImageView;

import java.util.List;


/**
 * Created by huxq17 on 2016/4/12.
 */
public class MeiziAdapter extends BaseCardAdapter {
    private List<HomeRecomSubBean> datas;
    private Context context;

    public MeiziAdapter(List<HomeRecomSubBean> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    public void setData(List<HomeRecomSubBean> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.card_item;
    }

    @Override
    public void onBindData(int position, View cardview) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        Glide.with(context)
                .load(Consts.IMAGEURL+datas.get(position).getCoverPic())
                .into((RoundAngleImageView) cardview.findViewById(R.id.iv_meizi));
    }

    /**
     * 如果可见的卡片数是3，则可以不用实现这个方法
     *
     * @return
     */
    @Override
    public int getVisibleCardCount() {
        return 3;
    }
}
