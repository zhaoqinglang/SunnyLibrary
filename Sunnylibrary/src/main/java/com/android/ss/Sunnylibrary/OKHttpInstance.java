package com.android.ss.Sunnylibrary;

import android.util.Log;

import java.io.File;
import java.util.List;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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

    public void getRequest(String url, Callback callback){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
//                .addHeader(headKey,headValue)
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
    //修改
    public void uploadFiles(String url , String requestStr, List<String> files, Callback callback){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < files.size(); i++) { //对文件进行遍历
            File file = new File(files.get(i)); //生成文件
            //根据文件的后缀名，获得文件类型
//            String fileType = file.
            builder.addFormDataPart( //给Builder添加上传的文件
                    "files",  //请求的名字
                    file.getName(), //文件的文字，服务器端用来解析的
                    RequestBody.create(MediaType.parse("application/octet-stream"), file) //创建RequestBody，把上传的文件放入
            );
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }
}
