@file:JvmName("IntentExtensions")
package com.google.mgmg22.extensions

import android.content.Intent
import android.text.TextUtils
import java.io.Serializable

/**
 * Created on 2020/8/18,@author Zen.
 * E-mail:jyzen@foxmail.com
 * This is ground control to Major Tom.
 * Don't panic.
 */

/**
 *支持intent不确定数量的传参
 */
fun Intent.putParams(vararg pairs: Pair<String, Any?>) {
    pairs.forEach {
        when (it.second) {
            is Boolean -> putExtra(it.first, it.second as Boolean)
            is Int -> putExtra(it.first, it.second as Int)
            is String -> putExtra(it.first, it.second as String)
            is Serializable -> putExtra(it.first, it.second as Serializable)
        }
    }
}

fun Intent.getStringExtra(str: String, defaultResult: String): String {
    val originResult = getStringExtra(str)
    return if (TextUtils.isEmpty(originResult)) {
        defaultResult
    } else {
        originResult?:""
    }
}