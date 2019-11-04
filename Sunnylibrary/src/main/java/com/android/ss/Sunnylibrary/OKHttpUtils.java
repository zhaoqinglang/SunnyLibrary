package com.android.ss.Sunnylibrary;

import android.util.Log;

import okhttp3.Callback;

public class OKHttpUtils {


    public static void postRequest(String url, String requestStr, Callback callback){
        new OKHttpInstance().postRequest(url,requestStr,callback);
    }
}
