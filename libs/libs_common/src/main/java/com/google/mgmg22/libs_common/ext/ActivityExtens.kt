package com.google.mgmg22.libs_common.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment

/**
 * 封装startActivity,通过扩展Intent属性传值
 * @Author:         luchong
 * @CreateDate:     2019/8/16 14:28
 */
inline fun <reified T : Context> Context.getIntent(extra: Intent.() -> Unit?): Intent =
        Intent(this, T::class.java).apply {
            extra()
        }

inline fun <reified T : Activity> Activity.startKtxActivity(extra: Intent.() -> Unit) =
        startActivity(getIntent<T>(extra))

inline fun <reified T : Activity> Fragment.startKtxActivity(extra: Intent.() -> Unit) =
        activity?.let {
            startActivity(it.getIntent<T>(extra))
        }

inline fun <reified T : Activity> Context.startKtxActivity(extra: Intent.() -> Unit) =
        startActivity(getIntent<T>(extra))

inline fun <reified T : Activity> View.startKtxActivity(crossinline extra: Intent.() -> Unit) =
        this.setOnClickListener { this.context.startKtxActivity<T>(extra) }

inline fun <reified T : Activity> Activity.startKtxActivityForResult(extra: Intent.() -> Unit, requestCode: Int) =
        startActivityForResult(getIntent<T>(extra), requestCode)
