package com.google.mgmg22.extensions

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
