package com.android.ss.Sunnylibrary;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;

import androidx.core.content.FileProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class Utils {
    //sharedpreference
    public static final String SPName = "zhaoqinglang";

    public static void put(Context context, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(SPName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof Serializable) {
            saveOject(context, key, object);
        } else {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    private static boolean saveOject(Context context, String objectkey, Object object) {
        SharedPreferences settings = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(object);
            // 将字节流编码成base64的字符窜
            String oAuth_Base64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(objectkey, oAuth_Base64);
            return editor.commit();
        } catch (IOException e) {
            return false;
        }
    }

    public static Object get(Context context, String key, Object defaultObject) {

        SharedPreferences sp = context.getSharedPreferences(SPName,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else if (defaultObject instanceof Serializable) {
            return getObject(context, key, defaultObject);
        }

        return defaultObject;
    }


    private static Object getObject(Context context, String objectkey, Object obj) {
        SharedPreferences settings = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        String strBase64 = settings.getString(objectkey, "");
        //读取字节
        byte[] base64 = Base64.decode(strBase64.getBytes(), Base64.DEFAULT);
        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        //再次封装
        try {
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                Object object = bis.readObject();
                if (object == null) {
                    return obj;
                }
                return object;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static void remove(Context context, String key) {
        remove(context, SPName, key);
    }

    public static void remove(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        clear(context, SPName);
    }

    public static void clear(Context context, String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {

        return contains(context, SPName, key);
    }

    public static boolean contains(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

    //get app inf
    public static int getAppVersionCode(Context context){
        int version = 0;
        try{
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS).versionCode;
        }catch (Exception e){
            e.printStackTrace();
        }
        return version;
    }

    public static String getAppVersionName(Context context){
        String name = "";
        try{
            name = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
        return name;
    }

    //判断包名是否存在
    public static boolean isAppExist(Context context,String pkgName) {
        ApplicationInfo info;
        try {
            info = context.getPackageManager().getApplicationInfo(pkgName, 0);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            info = null;
        }

        return info != null;
    }

    //如果一个apk中包含另一个apk
    //安装第二apk时，需要将apk复制到根目录下，再安装
    public static boolean copyApkFromAssets(Context context) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open("callbackApp.apk");
            File file = new File("/storage/emulated/0/callbackApp.apk");
            if (file.exists()){
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            Log.d("ssss","copyApkFromAssets IOException= "+e);
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    //将第二apk复制到手机存储之后，获取未安装的第二apk版本号
    public static int getCallbackAppToInstallVerison(Context context){
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageArchiveInfo("/storage/emulated/0/callbackApp.apk",PackageManager.GET_ACTIVITIES);
            return info.versionCode;
        }
        catch (Exception e) {
            Log.d("ssss","getCallbackAppToInstallVerison Exception= "+e);
            e.printStackTrace();
            info = null;
        }
        return -1;
    }

    //获取手机安卓版本号
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;

    }
    //通过contenturi获取file路径
    public static String getFilePathFromContentUri(Uri selectedVideoUri, ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
    //安装apk
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

    //获取UA（userAgent）
    public static String getUA(Context context){
        WebView webView = new WebView(context);
        return webView.getSettings().getUserAgentString();
    }
}
