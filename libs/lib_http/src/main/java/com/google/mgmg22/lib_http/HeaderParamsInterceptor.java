package com.google.mgmg22.lib_http;


import com.google.mgmg22.lib_util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * post请求添加header参数
 */
public class HeaderParamsInterceptor implements Interceptor {
    public static Map<String, String> DefaultParams = new HashMap<>();

    public HeaderParamsInterceptor() {
        //自定义的公共参数如：版本号等
//        DefaultParams.put(IFieldConstants.VERSION, PackageUtils.getVersion().replace("-debug", ""));
//        DefaultParams.put(IFieldConstants.SOURCE, IFieldConstants.ANDROID);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
//        DefaultParams.put(IFieldConstants.TOKEN, MzdkApplication.getInstance().getToken() != null ? MzdkApplication.getInstance().getToken() : "");
//        DefaultParams.put(IFieldConstants.USERID, PreferenceUtils.getString(IConstants.TEMP_USERID, ""));
        Map<String, String> temMap = new HashMap<>();
        for (Map.Entry<String, String> entry : DefaultParams.entrySet()) {
            if (!temMap.containsKey(entry.getKey())) {
                temMap.put(entry.getKey(), entry.getValue());
            }
        }
        Request.Builder headerParamsBuilder = oldRequest.newBuilder();
        for (Map.Entry<String, String> entry : DefaultParams.entrySet()) {
            headerParamsBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        RequestBody body = oldRequest.body();
        if (body instanceof FormBody) {
            FormBody formBody = (FormBody) body;
            FormBody.Builder builder = new FormBody.Builder();
            for (int i = 0; i < formBody.size(); i++) {
                builder.add(formBody.encodedName(i), formBody.encodedValue(i));
                temMap.put(formBody.encodedName(i), formBody.encodedValue(i));
            }
            builder.add("sign", getSign(temMap));
            FormBody newFormBody = builder.build();
            Request newRequest = headerParamsBuilder
                    .post(newFormBody)
                    .build();
            return chain.proceed(newRequest);
        } else if ("GET".equals(oldRequest.method())) {
            return chain.proceed(addGetParams(headerParamsBuilder.build(), temMap));
        } else {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(bodyToString(body));
//                Log.e("http jsonObject", jsonObject.toString());
                Iterator iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    String value = jsonObject.getString(key);
//                    Log.e("http kv", key + "    " + value);
                    if (!temMap.containsKey(key)) {
                        temMap.put(key, value);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            headerParamsBuilder.addHeader("sign", getSign(temMap));
            Request newRequest = headerParamsBuilder
                    .build();
            return chain.proceed(newRequest);
        }
    }

    private final String bodyToString(RequestBody body) {
        Buffer buffer = new Buffer();
        try {
            body.writeTo(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.readUtf8();
    }

    //get请求 添加公共参数 签名
    private static Request addGetParams(Request request, Map<String, String> defaultHeadsMap) {
        //添加公共参数
        HttpUrl httpUrl = request.url()
                .newBuilder()
                .build();
        Set<String> nameSet = httpUrl.queryParameterNames();
        ArrayList<String> nameList = new ArrayList<>();
        nameList.addAll(nameSet);
        Collections.sort(nameList);
        for (int i = 0; i < nameList.size(); i++) {
            defaultHeadsMap.put(nameList.get(i), httpUrl.queryParameterValues(nameList.get(i)).get(0));
        }
        //添加签名
        httpUrl = httpUrl.newBuilder()
                .addQueryParameter("sign", getSign(defaultHeadsMap))
                .build();
        request = request.newBuilder().url(httpUrl).build();
        return request;
    }


    /**
     * 使用反射的方式，从RequestParams里面获取urlParams信息
     */
    public static String getSign(Map<String, String> params) {

        /**
         * 混码前缀
         */
        String CODE_BEGIN = "meizhuangdaka.com?&";

        String code;
        Set<String> urlParams = params.keySet();
        String[] result = new String[params.size()];
        int index = 0;
        for (String key : urlParams) {
            result[index] = key + "=" + params.get(key);
//            Log.e("http sign", key + "=" + params.get(key));
            index++;
        }
        Arrays.sort(result);
        StringBuffer buffer = new StringBuffer();
        String temp = null;
        for (int i = 0, len = result.length; i < len; ++i) {
            temp = result[i];
            if ("image=FILE".equals(temp)) {
                continue;
            }
            buffer.append(result[i]);
            buffer.append("?&");
        }
        code = buffer.toString();
        return MD5.hexdigest(CODE_BEGIN + code);
    }

}

