package com.google.mgmg22.libs_common.ext

import android.widget.Toast
import com.google.mgmg22.libs_common.base.BaseApplication
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * @author sxs
 * @date 2019/1/2
 */
private val onNextStub: (Any) -> Unit = {}

//重写rxkotlin,全局toast异常
private val onErrorStub: (Throwable) -> Unit = { Toast.makeText(BaseApplication.getContext(), it.message, Toast.LENGTH_SHORT).show() }
private val onCompleteStub: () -> Unit = {}

/**
 * Overloaded subscribe function that allows passing named parameters
 */
fun <T : Any> Observable<T>.subscribeNext(
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub,
        onNext: (T) -> Unit = onNextStub
): Disposable = subscribe(onNext, onError, onComplete)


