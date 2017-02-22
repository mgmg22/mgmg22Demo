package com.google.mgmg22demo.util;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by uniware on 2016/9/22.
 */
public abstract class VolleyInterface {
    public Context context;
    public static Response.Listener<String> listener;
    public static Response.ErrorListener errorListener;

    public VolleyInterface(Context context, Response.Listener<String> listener,
                           Response.ErrorListener errorListener){
        this.context = context;
        this.listener = listener;
        this.errorListener = errorListener;
    }
    public abstract void onMySuccess(String result);
    public abstract void onMyError(VolleyError error);
    /**
     * 请求成功
     * @return
     */
    public Response.Listener<String> successListener(){
        listener = new Response.Listener<String>(){
            @Override
            public void onResponse(String arg0) {
                onMySuccess(arg0);
            }
        };
        return listener;
    }
    /**
     * 请求失败
     * @return
     */
    public Response.ErrorListener errorListener(){
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                onMyError(arg0);
            }
        };
        return errorListener;
    }
}