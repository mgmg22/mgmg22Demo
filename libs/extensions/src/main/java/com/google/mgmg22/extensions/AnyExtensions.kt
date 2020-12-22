@file:JvmName("AnyExtensions")
package com.google.mgmg22.extensions

import android.text.Spannable
import android.text.SpannableString

/**
 * Created on 2020/8/18,@author Zen.
 * E-mail:jyzen@foxmail.com
 * This is ground control to Major Tom.
 * Don't panic.
 */


/**
 *
 * 快速构建一个富文本SpannableString
 * 再也不用计算容易写错的start,end 索引了,很好用!
 * 对一段text可同时设置多个span
 */
fun quickBuildSpan(text: CharSequence, vararg spans: Any): CharSequence {
    return SpannableString(text).apply {
        spans.forEach {
            setSpan(it, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}