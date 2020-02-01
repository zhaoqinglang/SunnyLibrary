package com.android.ss.Sunnylibrary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class HttpUtil {
    //先定义一个String类型来接收接口相同的部分
    public static final String BASE_URL = "http://10.0.101.3:9999";
//    private static final String BASE_URL = "http://call.proxy.zhoushijt.com";
//    private static final String BASE_URL = "http://call.zhoushijt.com";
//    private static final String BASE_URL = "http://call.demo.zhoushijt.cn/";
    public static final String URL_GET_CONFIG = "/api/app/config";//获取配置
    public static final String URL_REDEEM = "/api/app/redeem";//激活充值卡
    public static final String URL_FEEDBACK_LIST = "/api/feedback/all";//工单列表
    public static final String URL_FEEDBACK_CREATE = "/api/feedback/create";//创建工单
    public static final String URL_TASK_CREATE = "/api/task/start";//开始呼叫任务
    public static final String URL_TASK_STOP = "/api/task/stop";//停止呼叫任务
    public static final String URL_TASK_GET = "/api/task/get";//获取呼叫号码
    public static final String URL_UPLOAD= "/api/app/upload";//上传文件
    public static final String Content_Type = "application/json";//上传参数类型
    public static final String URL_FEEDBACK_DEATIL = "/api/feedback/detail";//工单列表
    public static final String UPDATE_APK_NAME = "newApk.apk";
    public static final String UPDATE_APK_PATH = "/storage/emulated/0/update_cache/";
    public static final String KEY_MD5 = "sign";
    public static String AI = "AI";
    //建立静态的AsyncHttpClient
    private static AsyncHttpClient client = new AsyncHttpClient();
    //AsyncHttpClient中有get和post方法，需要用到public方法来修饰，以便调用
    public  static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    public  static void get(Context context,String url, List<Header> headers, AsyncHttpResponseHandler responseHandler){
        for (Header header : headers){
            client.addHeader(header.getName(),header.getValue());
        }
        client.addHeader("AI-Agent",AI);
        client.get(getAbsoluteUrl(url), responseHandler);
    }
    public  static void get(String url,AsyncHttpResponseHandler responseHandler){
        client.get(url, responseHandler);
    }
    //post方法中HttpEntity参数是后面发送JSON格式所用到的一个方法
    public static void post(Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        client.post(context,getAbsoluteUrl(url),entity, contentType, responseHandler);
    }
    public static void post(Context context, String url,  List<Header> headers,HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        for (Header header : headers){
            client.addHeader(header.getName(),header.getValue());
        }
        client.addHeader("AI-Agent",AI);
        client.post(context,getAbsoluteUrl(url),entity, contentType, responseHandler);
    }

    public static void post(Context context, String url,  List<Header> headers,RequestParams params, AsyncHttpResponseHandler responseHandler) {
        for (Header header : headers){
            client.addHeader(header.getName(),header.getValue());
        }
        client.addHeader("AI-Agent",AI);
        client.post(context,getAbsoluteUrl(url),params, responseHandler);
    }

    //单独写一个方法添加URL
    private static String getAbsoluteUrl(String url) {
        return BASE_URL + url;
    }

    public static String getByte2String(byte[] srtbyte){
        String res = "";
        try{
            res = new String(srtbyte,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public static void installApk(Context context,String downloadApk) {
        Log.i("sssss", "开始执行安装: " + downloadApk);
        File apkFile = new File(downloadApk);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.w("sssss", "版本大于 N ，开始使用 fileProvider 进行安装");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    context
                    , "com.android.ssproject.UPDATE_APP_FILE_PROVIDER"
                    , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Log.w("sssss", "正常进行安装");
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
    /**
     * 32位MD5加密
     * @param content -- 待加密内容
     * @return
     */
    public static String md5Decode32(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException",e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10){
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    //上传文件
    public static String uploadFile(Context context,File file, String RequestURL) {
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(30 * 1000);
            conn.setConnectTimeout(30 * 1000);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置编码
            conn.setRequestProperty("MEI",CallUtils.getIMEIid(context));
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if (file != null) {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"image\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + "UTF-8" + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                Log.e("sssss", "response code:" + res);
                // if(res==200)
                // {
                Log.e("sssss", "request success");
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                result = sb1.toString();
                Log.e("sssss", "result : " + result);
                // }
                // else{
                // Log.e("sssss", "request error");
                // }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    //下载文件，并显示进度
    //调用HttpUtil.get之后，就会开始下载，同时onProgress会不停的回调用于显示进度
    private void startDownLoad(String uri){
        HttpUtil.get(uri, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                FileOutputStream outputStream = null;
                BufferedOutputStream bufferedOutputStream = null;
                try{
                    File dir = new File(HttpUtil.UPDATE_APK_PATH);
                    if (!dir.exists()){
                        Log.d("sssss","mkdirs = "+dir.mkdirs());
                    }
                    File file = new File(HttpUtil.UPDATE_APK_PATH , HttpUtil.UPDATE_APK_NAME);
                    if (file.exists()){
                        file.delete();
                    }
                    if (!file.exists()){
                        if (file.createNewFile()){
                            outputStream = new FileOutputStream(file);
                            bufferedOutputStream = new BufferedOutputStream(outputStream);
                            bufferedOutputStream.write(responseBody);
                            bufferedOutputStream.flush();
                        }
                    }
                }catch (Exception e){
                    Log.d("sssss","DownLoad Exception = "+e);
                    e.printStackTrace();
                }finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);//进度的百分比，后面加%显示
            }
        });
    }
}
