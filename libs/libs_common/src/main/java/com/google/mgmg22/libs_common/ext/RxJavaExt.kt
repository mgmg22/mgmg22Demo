package com.google.mgmg22.libs_common.ext

import com.google.mgmg22.libs_common.base.BasePageList
import com.google.mgmg22.libs_common.http.exception.ApiException
import com.google.mgmg22.libs_common.http.model.BaseResult
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


fun <T> Observable<BaseResult<T>>.dispatchDefault(): Observable<T> =
        this.subscribeOn(Schedulers.io())
                .flatMap { tBaseModel ->
                    if (tBaseModel.success) {
                        Observable.just(tBaseModel.model!!)
                    } else {
//                        if (!getToken().isNullOrEmpty() && (tBaseModel.code.toInt() == 100210 || tBaseModel.code.toInt() == 100205 || tBaseModel.code.toInt() == 3054 || tBaseModel.code.toInt() == 3024)) {
//                            EventBus.getDefault().post(MessageEvent("TO_LOGIN"))
//                        }
                        Observable.error(ApiException(tBaseModel.code.toInt(), tBaseModel.message, tBaseModel.errorModel))
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())

//分页加载列表
fun <T> Observable<BaseResult<T>>.dispatchWithTotal(): Observable<BasePageList<T>> =
        this.subscribeOn(Schedulers.io())
                .flatMap { tBaseModel ->
                    if (tBaseModel.success) {
                        Observable.just(BasePageList(tBaseModel.model!!, tBaseModel.totalRecord))
                    } else {
//                        if (!getToken().isNullOrEmpty() && (tBaseModel.code.toInt() == 100210 || tBaseModel.code.toInt() == 100205 || tBaseModel.code.toInt() == 3054)) {
//                            EventBus.getDefault().post(MessageEvent("TO_LOGIN"))
//                        }
                        Observable.error(ApiException(tBaseModel.code.toInt(), tBaseModel.message, tBaseModel.errorModel))
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())

//后端可能不返回module
fun <T> Observable<BaseResult<T>>.dispatchAny(): Observable<Int> =
        this.subscribeOn(Schedulers.io())
                .flatMap { tBaseModel ->
                    if (tBaseModel.success) {
                        Observable.just(tBaseModel.totalRecord)
                    } else {
//                        if (!getToken().isNullOrEmpty() && (tBaseModel.code.toInt() == 100210 || tBaseModel.code.toInt() == 100205 || tBaseModel.code.toInt() == 3054)) {
//                            EventBus.getDefault().post(MessageEvent("TO_LOGIN"))
//                        }
                        Observable.error(ApiException(tBaseModel.code.toInt(), tBaseModel.message, tBaseModel.errorModel))
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())


fun <T> Single<T>.dispatchDefault(): Single<T> =
        this.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())


fun <T> Flowable<T>.dispatchDefault(): Flowable<T> =
        this.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

fun Completable.dispatchDefault(): Completable =
        this.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
