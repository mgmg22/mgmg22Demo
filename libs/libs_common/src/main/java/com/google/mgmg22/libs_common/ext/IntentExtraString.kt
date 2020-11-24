package com.google.mgmg22.libs_common.ext

import android.content.Intent
import kotlin.reflect.KProperty

/**
 * 通过委托简化intent传值
 * @Author:         luchong
 * @CreateDate:     2019/8/15 17:09
 */
class IntentExtraString(private val key: String? = null) {
    private val KProperty<*>.extraName: String
        get() = this@IntentExtraString.key ?: name

    operator fun getValue(intent: Intent, property: KProperty<*>): String? =
            intent.getStringExtra(property.extraName)

    operator fun setValue(intent: Intent, property: KProperty<*>, value: String?) {
        intent.putExtra(property.extraName, value)
    }
}
