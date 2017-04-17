package com.google.mgmg22demo;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class MyApplication extends Application {
    private static Gson mGson = new Gson();
    private static RequestQueue queue;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        // 建立Volley全局请求队列
        queue = Volley.newRequestQueue(getApplicationContext()); // 实例化RequestQueue对象
    }

    public static RequestQueue getHttpQueue() {
        return queue;
    }

    public static Gson getGson() {
        return mGson;
    }

//    public static Context getContext() {
//        return mContext;
//    }

}
