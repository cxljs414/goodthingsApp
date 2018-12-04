package com.goodthings.app.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.goodthings.app.activity.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/26
 * 修改内容：
 * 最后修改时间：
 */

public class URLImageParser implements Html.ImageGetter {
    TextView mTextView;
    Context context;
    private int screenWidth;
    private List<URLDrawable> urlDrawables;
    public URLImageParser(Context mcontext,TextView textView,int screenWidth) {
        this.context = mcontext;
        this.mTextView = textView;
        this.screenWidth = screenWidth;
        urlDrawables = new ArrayList();
    }
    @Override
    public Drawable getDrawable(String source) {
        Log.i("main","source:"+source);
        final URLDrawable urlDrawable = new URLDrawable();
        urlDrawables.add(urlDrawable);
        Glide.with(context)
                .load(source)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap loadedImage, GlideAnimation<? super Bitmap> glideAnimation) {
                        int width = loadedImage.getWidth();
                        int height = loadedImage.getHeight();
                        // 计算缩放比例
                        float scaleWidth = ((float) screenWidth) / width;
                        // 取得想要缩放的matrix参数
                        Matrix matrix = new Matrix();
                        matrix.postScale(scaleWidth, scaleWidth);
                        loadedImage = Bitmap.createBitmap(loadedImage, 0, 0, width, height, matrix, true);
                        urlDrawable.bitmap = loadedImage;
                        urlDrawable.setBounds(0, 0,screenWidth ,loadedImage.getHeight());//loadedImage.getWidth()
                        mTextView.invalidate();
                        mTextView.setText(mTextView.getText());
                    }
                });
        return urlDrawable;
    }

    class URLDrawable extends BitmapDrawable {
        protected Bitmap bitmap;
        @Override
        public void draw(Canvas canvas) {
            if (bitmap != null && !bitmap.isRecycled()) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }

    public void destory(){
       if (!urlDrawables.isEmpty()){
           for (URLDrawable urlDrawable : urlDrawables){
               if (urlDrawable != null && urlDrawable.bitmap!=null){
                   urlDrawable.bitmap.recycle();
                   urlDrawable = null;
               }
           }
       }
    }
}