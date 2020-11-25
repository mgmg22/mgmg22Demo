package com.google.mgmg22.lib_http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier


val DO_NOT_VERIFY = HostnameVerifier { _, _ -> true }


val retrofit: Retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(
            OkHttpClient.Builder()
                //TODO 拦截器从外部传入
//                    .addInterceptor(ChuckInterceptor(BaseApplication.getContext()))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(ParamsInterceptor())
                .addInterceptor(HeaderParamsInterceptor())
                .sslSocketFactory(AppSSLSocketFactory.getAllSSLSocketFactory())
                .hostnameVerifier(DO_NOT_VERIFY)
                .build()
        )
        //配置接口域名
//            .baseUrl(BuildConfig.NEW_API_HOST)
        .build()
}

