package com.android.ss.Sunnylibrary;

import android.util.Log;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OKHttpInstance {

    private static OkHttpClient mOkHttpClient;
    private OKHttpConfig mOKHttpConfig;

    public OKHttpInstance() {
        mOKHttpConfig = new OKHttpConfig();
        createOkHttpClient();
    }

    public OKHttpInstance(OKHttpConfig okHttpConfig) {
        mOKHttpConfig = okHttpConfig;
        createOkHttpClient();
    }

    private void createOkHttpClient(){
        if (mOkHttpClient == null){
            synchronized (OkHttpClient.class){
                mOkHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(mOKHttpConfig.getmConnectTime(),mOKHttpConfig.getmTimeUnit())
                        .readTimeout(mOKHttpConfig.getmReadTime(),mOKHttpConfig.getmTimeUnit())
                        .writeTimeout(mOKHttpConfig.getmWriteTime(),mOKHttpConfig.getmTimeUnit())
                        .retryOnConnectionFailure(mOKHttpConfig.ismRetryOnConnectionFailure())
                        .build();
            }
        }
    }

    public void getRequest(String url, String headKey,String headValue, Callback callback){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .addHeader(headKey,headValue)
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }
    public void postRequest(String url, String requestStr, Callback callback){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, requestStr))
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }
}
