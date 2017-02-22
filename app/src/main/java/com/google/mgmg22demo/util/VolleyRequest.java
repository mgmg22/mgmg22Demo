package com.google.mgmg22demo.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.mgmg22demo.MyApplication;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by uniware on 2016/9/22.
 */
public class VolleyRequest {
    public static StringRequest request;
    public static Context context;

    /**
     * GET请求
     *
     * @param context
     * @param url
     * @param tag
     * @param vi
     */
    public static void RequestGet(Context context, String url, String tag, VolleyInterface vi) {
        MyApplication.getHttpQueue().cancelAll(tag); // 将tag标签的请求都取消掉，放在重复请求
        StringRequest request = new StringRequest(Request.Method.GET, url, vi.successListener(), vi.errorListener());

        request.setTag(tag);
        MyApplication.getHttpQueue().add(request);
        MyApplication.getHttpQueue().start();
    }

    /**
     * POST请求
     *
     * @param context
     * @param url
     * @param tag
     * @param hashmap
     * @param vi
     */
    public static void RequestPost(Context context, String url, String tag, final HashMap<String, String> hashmap, VolleyInterface vi) {
        MyApplication.getHttpQueue().cancelAll(tag); // 将tag标签的请求都取消掉，放在重复请求
        StringRequest request = new StringRequest(Request.Method.GET, url, vi.successListener(), vi.errorListener()) {
            @Override
            protected Map<String, String> getParams() {
                return hashmap;
            }
        };
        request.setTag(tag);
        MyApplication.getHttpQueue().add(request);
        MyApplication.getHttpQueue().start();
    }
}