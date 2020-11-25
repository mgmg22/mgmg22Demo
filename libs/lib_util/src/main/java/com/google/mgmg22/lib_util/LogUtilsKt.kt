package com.google.mgmg22.lib_util

import android.util.Log
import java.util.HashMap
import java.util.Locale

/**
 * 用于修复logD打印的调用方法位置不正确
 * @Author: luchong
 * @CreateDate: 2019/9/24 16:37
 */
class LogUtilsKt private constructor() {
    init {
        throw UnsupportedOperationException(this.javaClass.simpleName + " cannot be instantiated")
    }

    companion object {
        fun e(tag: String, message: String) {
            Log.e(tag, message)
        }

        fun d(tag: String, message: String?) {
            if (message == null) {
                return
            }
            Log.d(tag, message)
        }

        fun i(tag: String, message: String?) {
            if (message == null) {
                return
            }
            Log.i(tag, message)
        }

        private const val APP_TAG = "NALA_"

        private val sCachedTag = HashMap<String, String>()

        fun i(message: String) {
            Log.i(
                buildTag(
                    APP_TAG
                ),
                buildMessage(
                    message
                )
            )
        }

        fun d(message: String) {
            Log.d(
                buildTag(
                    APP_TAG
                ),
                buildMessage(
                    message
                )
            )
        }

        fun e(message: String) {
            Log.e(
                buildTag(
                    APP_TAG
                ),
                buildMessage(
                    message
                )
            )
        }

        private fun buildTag(tag: String): String? {
            val key = String.format(Locale.US, "%s@%s", tag, Thread.currentThread().name)

            if (!sCachedTag.containsKey(key)) {
                if (APP_TAG == tag) {
                    sCachedTag[key] = String.format(Locale.US, "|%s|",
                            tag
                    )
                } else {
                    sCachedTag[key] = String.format(Locale.US, "|%s_%s|",
                        APP_TAG,
                            tag
                    )
                }
            }
            return sCachedTag[key]
        }

        private fun buildMessage(message: String): String {
            val traceElements = Thread.currentThread().stackTrace
            // 通过logD扩展方法调用时下标为5
            if (traceElements == null || traceElements.size < 5) {
                return message
            }
            val traceElement = traceElements[5]
            return String.format(Locale.US, "%s.%s(%s:%d) %s",
                    traceElement.className.substring(traceElement.className.lastIndexOf(".") + 1),
                    traceElement.methodName,
                    traceElement.fileName,
                    traceElement.lineNumber,
                    message
            )
        }
    }
}
