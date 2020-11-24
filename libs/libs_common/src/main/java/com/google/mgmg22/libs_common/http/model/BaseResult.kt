package com.google.mgmg22.libs_common.http.model

import com.google.gson.JsonElement

/**
 * 基础的返回数据格式
 * @author sxs
 * @date 2019/1/2
 */
open class BaseResult<T> {
    val code: String = ""
    val message: String = ""
    val success: Boolean = false
    var model: T? = null
    var errorModel: JsonElement? = null
    val totalRecord: Int = 0
}