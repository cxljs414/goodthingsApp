package com.goodthings.app.view;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/3
 * 修改内容：
 * 最后修改时间：
 */

public class ChangeSpeedScroller extends Scroller {
    private int mDuration=250;

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public ChangeSpeedScroller(Context context){
        super(context);
    }
    public ChangeSpeedScroller(Context context, Interpolator interpolator){
        super(context,interpolator);
    }
    public ChangeSpeedScroller(Context context, Interpolator interpolator,boolean flywheel){
        super(context,interpolator,flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy);
    }
}
