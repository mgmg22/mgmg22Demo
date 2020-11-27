package com.google.mgmg22.libs_common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText

/**
 * @Author shenxiaoshun
 * @Date 2020/11/27
 */
class SearchEditText : EditText {
    private var mListener: OnTextChangedListener? = null
    private var mStartText = "" // 记录开始输入前的文本内容
    private val mAction = Runnable {
        if (mListener != null) {
            // 判断最终和开始前是否一致
            if (mStartText != text.toString()) {
                mStartText = text.toString() // 更新 mStartText
                mListener!!.onTextChanged(mStartText)
            }
        }
    }

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    /**
     * 在 LIMIT 时间内连续输入不触发文本变化
     */
    fun setOnTextChangedListener(listener: OnTextChangedListener?) {
        mListener = listener
    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        // 移除上一次的回调
        removeCallbacks(mAction)
        postDelayed(mAction, LIMIT)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(mAction)
    }

    interface OnTextChangedListener {
        fun onTextChanged(text: String?)
    }

    companion object {
        private const val LIMIT: Long = 1000
    }
}