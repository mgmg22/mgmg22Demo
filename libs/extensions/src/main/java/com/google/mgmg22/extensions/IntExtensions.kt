@file:JvmName("IntExtensions")

package com.google.mgmg22.extensions

import android.content.res.Resources
import java.text.DecimalFormat

/**
 * Created on 2020/8/18,@author Zen.
 * E-mail:jyzen@foxmail.com
 * This is ground control to Major Tom.
 * Don't panic.
 */


/**
 * 保留两位小数
 */
fun Int.twoDecimal(): String = DecimalFormat("0.00").format(this)

/**
 * 超过max 显示 max+
 * eg:99+
 */
fun Int.ceiling(max: Int): String {
    return if (this > max) "${max}+" else "$this"
}

/**
 * 转dp
 */
val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

/**
 * 转sp
 */
val Int.sp: Int
    get() = (this * Resources.getSystem().displayMetrics.scaledDensity + 0.5f).toInt()

/**
 * 补零 eg. 1->01
 */
fun Int.zeroCompensation(): String {
    return if (this < 10) "0$this" else "$this"
}