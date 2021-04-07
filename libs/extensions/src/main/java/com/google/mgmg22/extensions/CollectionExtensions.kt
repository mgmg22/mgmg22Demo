@file:kotlin.jvm.JvmName("CollectionExtensions")

package com.google.mgmg22.extensions

/**
 * Just read the instructions.
 * Created by Zen on 2020/10/26
 */

/**
 * 集合可用时执行
 */
inline fun <T> Collection<T>?.withNotNullOrEmpty(block: (Collection<T>) -> Unit) {
    if (!isNullOrEmpty()) {
        block(this!!)
    }
}