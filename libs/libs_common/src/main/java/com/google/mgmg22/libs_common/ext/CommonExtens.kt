package com.google.mgmg22.libs_common.ext

import com.google.mgmg22.libs_common.component.PreferenceUtils

/**
 * 和业务相关的扩展写在这里，如登陆用户信息等
 * @Description:
 * @Author:         沈晓顺
 * @CreateDate:     2019-12-04 17:29
 */
fun getToken(): String? {
    val userToken: String = PreferenceUtils.getString("token", "")
    return if (userToken.isNotEmpty()) {
        userToken
    } else null
}

//手机号格式
fun String.isPhone() = this.startsWith("1") && this.trim().length == 11

//布尔字符串"Y","N"
fun String?.string2Boolean() = (this != null) && (this == "Y")
