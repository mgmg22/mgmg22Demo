package com.google.mgmg22.extensions

import java.util.*

/**
 * 和业务相关的扩展写在这里，如登陆用户信息等
 * @Description:
 * @Author:         mgmg22
 * @CreateDate:     2019-12-04 17:29
 */


//手机号格式
fun String.isPhone() = this.startsWith("1") && this.trim().length == 11

//布尔字符串"Y","N"
fun String?.string2Boolean() = (this != null) && (this == "Y")

/**
 * 比较内容是否一致
 */
infix fun Any.sameAs(other: Any) = this == other

/**
 * 随机数
 */
fun ClosedRange<Int>.random() = Random().nextInt((endInclusive + 1) - start) + start

/**
 * 手机号码带空格显示
 */
fun String.formatPhone() = "${this.substring(0, 3)} ${"****"} ${this.substring(7, 11)}"
