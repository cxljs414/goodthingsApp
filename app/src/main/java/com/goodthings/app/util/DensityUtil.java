package com.goodthings.app.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DensityUtil {

    /**
     * dp->px
     *
     * @param _context context
     * @param _dpValue dp
     * @return
     */
    public static float dip2px(Context _context, float _dpValue) {
        float scale = _context.getResources().getDisplayMetrics().density;
        return _dpValue * scale;
    }

    /**
     * 获取 DisplayMetrics
     *
     * @param _context context
     * @return DisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context _context) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        ((Activity) _context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics;
    }


    /**
     * dp转px
     */
    public static int dp2px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px转dp
     */
    public static float px2dp(Context context, int px) {
        return (px / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 将sp值转换为px值
     */
    public static int sp2px(Context context, float sp) {
        return (int) (sp * context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

}