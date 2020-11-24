package com.google.mgmg22.libs_common.base

/**
 * @Description:
 * @Author:         沈晓顺
 * @CreateDate:     2020/4/24 10:13 AM
 */

data class BasePageList<T>(
        var model: T? = null,
        val totalRecord: Int = 0
)