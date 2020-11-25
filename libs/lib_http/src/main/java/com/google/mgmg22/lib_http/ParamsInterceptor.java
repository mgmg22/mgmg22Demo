package com.google.mgmg22.lib_http;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.google.mgmg22.lib_http.HeaderParamsInterceptor.getSign;

/**
 * Time:2019/6/24
 * Description:
 */
public class ParamsInterceptor implements Interceptor {

    private final Map<String, String> DefaultParams;

    public ParamsInterceptor() {
        //定义公共参数
        this.DefaultParams = new HashMap<>();
//        DefaultParams.put(IFieldConstants.VERSION, PackageUtils.getVersion().replace("-debug", ""));
//        DefaultParams.put(IFieldConstants.SOURCE, IFieldConstants.ANDROID);
//        DefaultParams.put(IFieldConstants.ANDROID_CHANNEL, "mzdk");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //拿到原来的request
        Request oldrequest = chain.request();
        //拿到请求的url
        String url = oldrequest.url().toString();
        //判断是get还是post
        if (oldrequest.method().equalsIgnoreCase("GET")) {
            if (DefaultParams != null && DefaultParams.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder(url);
                //拼接公共请求参数
                for (Map.Entry<String, String> entry : DefaultParams.entrySet()) {
                    stringBuilder.append("&" + entry.getKey() + "=" + entry.getValue());
                }
                url = stringBuilder.toString();
                //如果之前的url没有？号，我们需要手动给他添加一个？号
                if (!url.contains("?")) {
                    url = url.replaceFirst("&", "?");
                }
                //依据原来的request构造一个新的request
                Request request = oldrequest.newBuilder()
                        .url(url)
                        .build();

                return chain.proceed(request);
            }
        } else {
            if (DefaultParams != null && DefaultParams.size() > 0) {
                RequestBody body = oldrequest.body();
                if (body != null && body instanceof FormBody) {
                    FormBody formBody = (FormBody) body;
                    //1.把原来的的body里面的参数添加到新的body中
                    FormBody.Builder builder = new FormBody.Builder();
                    //为了防止重复添加相同的key和value
                    Map<String, String> temmap = new HashMap<>();
                    for (int i = 0; i < formBody.size(); i++) {
                        builder.add(formBody.encodedName(i), formBody.encodedValue(i));
                        temmap.put(formBody.encodedName(i), formBody.encodedValue(i));
                    }
                    //2.把公共请求参数添加到新的body中
                    for (Map.Entry<String, String> entry : DefaultParams.entrySet()) {
                        if (!temmap.containsKey(entry.getKey())) {
                            builder.add(entry.getKey(), entry.getValue());
                            temmap.put(entry.getKey(), entry.getValue());
                        }
                    }
//                    temmap.put(IFieldConstants.TOKEN, MzdkApplication.getInstance().getToken() != null ? MzdkApplication.getInstance().getToken() : "");
//                    temmap.put(IFieldConstants.USERID, PreferenceUtils.getString(IConstants.TEMP_USERID, ""));
                    String sign = getSign(temmap);
                    builder.add("sign", sign);
                    FormBody newFormBody = builder.build();
                    //依据原来的request构造一个新的request,
                    Request newRequest = oldrequest.newBuilder()
                            .post(newFormBody)
                            .build();
                    return chain.proceed(newRequest);
                }
            }
        }
        return chain.proceed(oldrequest);
    }
}

