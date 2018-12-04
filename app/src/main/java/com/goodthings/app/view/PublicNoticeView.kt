package com.goodthings.app.view


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ViewFlipper
import com.goodthings.app.R
import com.goodthings.app.bean.TittleList
import com.goodthings.app.inerfaces.OnFilpperClickListener


class PublicNoticeView : LinearLayout {
    private var mContext: Context? = null
    private var mViewFlipper: ViewFlipper? = null
    private var mScrollTitleView: View? = null
    private var onFilpperClickListener: OnFilpperClickListener? = null


    constructor(context: Context) : super(context) {
        mContext = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
        init()
    }

    private fun init() {
        bindLinearLayout()
        bindNotices()
    }

    fun setOnFilpperClickListener(listener: OnFilpperClickListener){
        this.onFilpperClickListener = listener
    }


    /**
     * 初始化自定义的布局
     */
    private fun bindLinearLayout() {
        mScrollTitleView = LayoutInflater.from(mContext).inflate(R.layout.scrollnoticebar, null)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT)
        addView(mScrollTitleView, params)
        mViewFlipper = mScrollTitleView!!.findViewById(R.id.id_scrollNoticeTitle) as ViewFlipper
        mViewFlipper!!.inAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom)
        mViewFlipper!!.outAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_top)
        mViewFlipper!!.startFlipping()
        mViewFlipper!!.setOnClickListener {
            if (onFilpperClickListener != null) {
                onFilpperClickListener!!.onFilpperClicked(TittleList.newsList[mViewFlipper!!.displayedChild])
            }
            /*Intent intent = new Intent(getContext(), NewListActivity.class);
                intent.putExtra("hm_news_id", );
                getContext().startActivity(intent);*/
        }


    }

    /**
     * 网络请求内容后进行适配
     */
    fun bindNotices() {

        mViewFlipper!!.removeAllViews()
        //int i = 0;
        for (i in TittleList.tittleList.indices) {
            val text = TittleList.tittleList[i]
            val textView = TextView(mContext)
            textView.text = text
            textView.setTextColor(resources.getColor(R.color.black))
            textView.textSize = 12f
            textView.setSingleLine()
            textView.maxEms = 18
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT)
            mViewFlipper!!.addView(textView, layoutParams)


        }
        /*while (i < TittleList.tittleList.size()) {
            String text = TittleList.tittleList.get(i);
            TextView textView = new TextView(mContext);
            textView.setText(text);
            textView.setTextColor(getResources().getColor(R.color.public_text_color));
            textView.setTextSize(12);
            textView.setSingleLine();
            textView.setMaxEms(18);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            mViewFlipper.addView(textView, layoutParams);
            i++;
        }*/
    }
}
