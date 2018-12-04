package com.goodthings.app.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView

import com.goodthings.app.R


class VerifyCodeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    private val editText: EditText
    private val textViews: Array<TextView?> = arrayOfNulls<TextView>(4)
    var editContent: String? = null
        private set


    private var inputCompleteListener: InputCompleteListener? = null

    init {
        View.inflate(context, R.layout.view_verify_code, this)

        textViews[0] = findViewById<View>(R.id.tv_0) as TextView
        textViews[1] = findViewById<View>(R.id.tv_1) as TextView
        textViews[2] = findViewById<View>(R.id.tv_2) as TextView
        textViews[3] = findViewById<View>(R.id.tv_3) as TextView
        editText = findViewById<View>(R.id.edit_text_view) as EditText

        editText.isCursorVisible = false//隐藏光标
        setEditTextListener()
    }

    private fun setEditTextListener() {
        editText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                editContent = editText.text.toString()

                if (inputCompleteListener != null) {
                    if (editContent!!.length >= MAX) {
                        inputCompleteListener!!.inputComplete()
                    } else {
                        inputCompleteListener!!.invalidContent()
                    }
                }

                for (i in 0 until MAX) {
                    if (i < editContent!!.length) {
                        textViews[i]?.text = editContent!![i].toString()
                    } else {
                        textViews[i]?.text = ""
                    }
                }
            }
        })
    }

    fun setInputCompleteListener(inputCompleteListener: InputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener
    }

    interface InputCompleteListener {

        fun inputComplete()

        fun invalidContent()
    }

    companion object {
        private val MAX = 4
    }

}