package com.example.yzt.wm_english;


import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by YZT on 2017/1/26.
 */

public class HttpUtil {

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

    public static void postJson( String url,String json,okhttp3.Callback callback){
        //
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(10, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(10, TimeUnit.SECONDS);
        client.newBuilder().writeTimeout(10, TimeUnit.SECONDS);
        RequestBody requestBody = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);


    }
}
