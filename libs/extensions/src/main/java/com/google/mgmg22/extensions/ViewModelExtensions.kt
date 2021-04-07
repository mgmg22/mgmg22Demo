@file:JvmName("ViewModelExtensions")
package com.google.mgmg22.extensions

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created on 2020/8/19,@author Zen.
 * E-mail:jyzen@foxmail.com
 * This is ground control to Major Tom.
 * Don't panic.
 */

inline fun <reified T : ViewModel> viewModels(): ReadOnlyProperty<FragmentActivity, T> {
    return object : ReadOnlyProperty<FragmentActivity, T> {
        override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
            return ViewModelProvider(thisRef).get(T::class.java)
        }
    }
}