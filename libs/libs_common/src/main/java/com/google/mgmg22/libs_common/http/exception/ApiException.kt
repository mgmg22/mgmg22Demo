package com.google.mgmg22.libs_common.http.exception

import com.google.gson.JsonElement

/**
 * 和服务端约定的错误
 */
class ApiException(message: String?, cause: Throwable?) : Exception(message, cause) {
    var code: Int? = 0
    var msg: String? = null
    var errorModel: JsonElement? = null
    var errorCause: Throwable? = null

    constructor(code: Int, message: String?, cause: Throwable?) : this(message, cause) {
        this.code = code
        this.msg = message
        this.errorCause = cause
    }

    constructor(code: Int, msg: String, error: JsonElement?) : this(code, msg, Throwable()) {
        this.code = code
        this.msg = msg
        this.errorModel = error
    }

}