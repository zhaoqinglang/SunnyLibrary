package com.android.ss.Sunnylibrary;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;

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
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class Utils {
    //sharedpreference
    public static final String TAG = "zql";
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

    //获取当前app信息
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
            Log.d(TAG,"copyApkFromAssets IOException= "+e);
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
            Log.d(TAG,"getCallbackAppToInstallVerison Exception= "+e);
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
    //安装apk，需要自己判断安装路径是否存在
    public static void installApk(Context context,String downloadApk) {
        Log.i(TAG, "开始执行安装: " + downloadApk);
        File apkFile = new File(downloadApk);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.w(TAG, "版本大于 N ，开始使用 fileProvider 进行安装");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    context
                    , "com.android.ssproject.UPDATE_APP_FILE_PROVIDER"
                    , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Log.w(TAG, "正常进行安装");
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    //获取UA（userAgent）
    public static String getUA(Context context){
        WebView webView = new WebView(context);
        return webView.getSettings().getUserAgentString();
    }

    //dp转px
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //px转dp
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //获取设备MAC地址,需添加权限<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    public static String getMacAddress(Context context) {
        String macAddress = null;
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (null != info ) {
            macAddress = info.getMacAddress();
            if (null != macAddress) {
                macAddress = macAddress.replace(":", "");
            }
        }
        return macAddress;
    }

    //获取设备厂商，如Xiaomi
    public static String getManufacturer() {
        String MANUFACTURER = Build.MANUFACTURER;
        return MANUFACTURER;
    }

    //获取设备型号，如MI2SC
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    //获取设备SD卡是否可用
    public static boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    //获取设备SD卡路径
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    //判断是否网络连接,需添加权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    public static boolean isNetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    //判断wifi是否连接状态,需添加权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    //获取移动网络运营商名称
    public static String getNetworkOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getNetworkOperatorName() : null;
    }

    //卸载指定包名的App，需要自行判断包名是否存在
    public boolean uninstallApp(Context context, String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + packageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    //appinfo类，用于获取完整的app信息
    public static class AppInfo {

        private String name;
        private Drawable icon;
        private String packagName;
        private String versionName;
        private int versionCode;
        private boolean isSD;
        private boolean isUser;

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public boolean isSD() {
            return isSD;
        }

        public void setSD(boolean SD) {
            isSD = SD;
        }

        public boolean isUser() {
            return isUser;
        }

        public void setUser(boolean user) {
            isUser = user;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackagName() {
            return packagName;
        }

        public void setPackagName(String packagName) {
            this.packagName = packagName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        /**
         * @param name        名称
         * @param icon        图标
         * @param packagName  包名
         * @param versionName 版本号
         * @param versionCode 版本Code
         * @param isSD        是否安装在SD卡
         * @param isUser      是否是用户程序
         */
        public AppInfo(String name, Drawable icon, String packagName,
                       String versionName, int versionCode, boolean isSD, boolean isUser) {
            this.setName(name);
            this.setIcon(icon);
            this.setPackagName(packagName);
            this.setVersionName(versionName);
            this.setVersionCode(versionCode);
            this.setSD(isSD);
            this.setUser(isUser);
        }
    }
    /**
     * 获取当前App信息
     * AppInfo（名称，图标，包名，版本号，版本Code，是否安装在SD卡，是否是用户程序）
     */
    public static AppInfo getAppInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            //此处替换包名是否可以获取其他app信息，待验证
            pi = pm.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi != null ? getBean(pm, pi) : null;
    }

    /**
     * 得到AppInfo的Bean
     */
    private static AppInfo getBean(PackageManager pm, PackageInfo pi) {
        ApplicationInfo ai = pi.applicationInfo;
        String name = pi.packageName;
        Drawable icon = ai.loadIcon(pm);
        String packageName = pi.packageName;
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        boolean isSD = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != ApplicationInfo.FLAG_SYSTEM;
        boolean isUser = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != ApplicationInfo.FLAG_SYSTEM;
        return new AppInfo(name, icon, packageName, versionName, versionCode, isSD, isUser);
    }

    //打开指定包名的App
    public static boolean openAppByPackageName(Context context, String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            PackageManager pm = context.getPackageManager();
            Intent launchIntentForPackage = pm.getLaunchIntentForPackage(packageName);
            if (launchIntentForPackage != null) {
                context.startActivity(launchIntentForPackage);
                return true;
            }
        }
        return false;
    }

    //打开指定包名的App应用信息界面
    public static boolean openAppInfo(Context context, String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            Intent intent = new Intent();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + packageName));
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 判断当前App处于前台还是后台
     * 需添加<uses-permission android:name="android.permission.GET_TASKS"/>
     * 并且必须是系统应用该方法才有效
     */
    public static boolean isAppBackgroundUSYS(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    //获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //获取ActionBar高度
    public static int getActionBarHeight(Activity activity) {
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }
        return 0;
    }

    //动态隐藏软键盘
    public static void hideSoftInput(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideSoftInput(Context context, EditText edit) {
        edit.clearFocus();
        InputMethodManager inputmanger = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    //动态显示软键盘
    public static void showSoftInput(Context context, EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(edit, 0);
    }

    //验证手机号
    private static final String REGEX_MOBILE = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
    //验证座机号,正确格式：xxx/xxxx-xxxxxxx/xxxxxxxx
    private static final String REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}";
    //验证邮箱
    private static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    //验证url
    private static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";
    //验证汉字
    private static final String REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$";
    //验证用户名,取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
    private static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";
    //验证IP地址
    private static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

    //If u want more please visit http://toutiao.com/i6231678548520731137/

    /**
     * @param string 待验证文本
     * @return 是否符合手机号格式
     */
    public static boolean isMobile(String string) {
        return isMatch(REGEX_MOBILE, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合座机号码格式
     */
    public static boolean isTel(String string) {
        return isMatch(REGEX_TEL, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合邮箱格式
     */
    public static boolean isEmail(String string) {
        return isMatch(REGEX_EMAIL, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合网址格式
     */
    public static boolean isURL(String string) {
        return isMatch(REGEX_URL, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合汉字
     */
    public static boolean isChz(String string) {
        return isMatch(REGEX_CHZ, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合用户名
     */
    public static boolean isUsername(String string) {
        return isMatch(REGEX_USERNAME, string);
    }

    /**
     * @param regex  正则表达式字符串
     * @param string 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    public static boolean isMatch(String regex, String string) {
        return !TextUtils.isEmpty(string) && Pattern.matches(regex, string);
    }

    //获取服务是否开启
    public static boolean isRunningService(String className, Context context) {
        // 进程的管理者,活动的管理者
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取正在运行的服务，最多获取1000个
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
        // 遍历集合
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            ComponentName service = runningServiceInfo.service;
            if (className.equals(service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public static final int PICK_PHOTO = 100;
    public static final int TAKE_PHOTO = 101;
    //从图库获取图片
    public static void getPhotoFromGallery(Activity activity){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent,PICK_PHOTO);
    }

    //从相机获取图片
    public static void getPhototFromCamera(String path,Activity activity){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(path);
        if (file.exists()){
            file.delete();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(activity, "com.camera.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent,TAKE_PHOTO);
    }
}
