package com.android.ss.Sunnylibrary;

import android.util.Log;

import java.util.List;

import okhttp3.Callback;

public class OKHttpUtils {

    public static void getRequest(String baseUrl,String contentUrl,Callback callback){
        new OKHttpInstance().getRequest(baseUrl + contentUrl,callback);
    }

    public static void postRequest(String baseUrl,String contentUrl, String requestStr, Callback callback){
        new OKHttpInstance().postRequest(baseUrl + contentUrl,requestStr,callback);
    }

    public static void uploadFiles(String baseUrl, String contentUrl, String requestStr, List<String> files, Callback callback){
        new OKHttpInstance().uploadFiles(baseUrl + contentUrl,requestStr, files, callback);
    }

}
