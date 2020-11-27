package com.google.mgmg22.extensions

import android.view.View
import kotlin.math.abs

abstract class SafeOnClickListener constructor(private val interval: Long) : View.OnClickListener {

    private var lastOnClickTime: Long = 0

    override fun onClick(v: View?) {
        if (abs(System.currentTimeMillis() - lastOnClickTime) >= interval) {
            onSafeClick(v)
            lastOnClickTime = System.currentTimeMillis()
        } else {
            onInterceptClick(v)
        }
    }

    abstract fun onSafeClick(v: View?)
    open fun onInterceptClick(v: View?) {

    }
}