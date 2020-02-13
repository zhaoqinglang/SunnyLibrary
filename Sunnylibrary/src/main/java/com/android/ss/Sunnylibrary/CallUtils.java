package com.android.ss.Sunnylibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.ITelephony;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

public class CallUtils {

    //拨电话
    public static void Call(Context context, String phoneNum, int id){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        if (isMultiSim(context)){
            intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, getSimNum(context).get(id));
        }
        intent.setData(data);
        context.startActivity(intent);
    }
    //挂断
    public static void EndCall(Context context){
        try{
            getITelephony(context).endCall();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ITelephony getITelephony(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context
                .getSystemService(TELEPHONY_SERVICE);
        Class c = TelephonyManager.class;
        Method getITelephonyMethod = null;
        try {
            getITelephonyMethod = c.getDeclaredMethod("getITelephony",
                    (Class[]) null); // 获取声明的方法
            getITelephonyMethod.setAccessible(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            ITelephony iTelephony = (ITelephony) getITelephonyMethod.invoke(
                    mTelephonyManager, (Object[]) null); // 获取实例
            return iTelephony;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //是否是双卡
    public static boolean isMultiSim(Context context){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            return false;
        }
        boolean result = false;
        try{
            TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
            if(telecomManager != null){
                List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();
                result = phoneAccountHandleList.size() >= 2;
            }
        }catch (Exception e){
            Log.d(Utils.TAG,"isMultiSim Exception = "+e);
            e.printStackTrace();
        }
        return result;
    }
    //获取sim卡列表
    public static List<PhoneAccountHandle> getSimNum(Context context){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            return null;
        }
        List<PhoneAccountHandle> phoneAccountHandleList = new ArrayList<>();
        try{
            TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
            if(telecomManager != null){
                phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();
            }
        }catch (Exception e){
            Log.d(Utils.TAG,"isMultiSim Exception = "+e);
            e.printStackTrace();
        }
        return phoneAccountHandleList;
    }
    //选择sim卡弹框
    public static void showSelectSimDialog(Context context, String phoneNum, DialogInterface.OnClickListener okInterface
            , DialogInterface.OnClickListener cancleInterface){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("请选择电话卡")
                .setPositiveButton("卡1",okInterface)
                .setNegativeButton("卡2",cancleInterface);
        builder.create().show();
    }

    //获取IMEI号
    public synchronized static String getIMEIid(Context context) {
        TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
        String ID= TelephonyMgr.getDeviceId();
        return ID;
    }

    //发送短信
    public static void sendSms(Context context, String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + (TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", TextUtils.isEmpty(content) ? "" : content);
        context.startActivity(intent);
    }

    //获取手机联系人
    public static List<HashMap<String, String>> getAllContactInfo(Context context) {
        SystemClock.sleep(3000);
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        // 1.获取内容解析者
        ContentResolver resolver = context.getContentResolver();
        // 2.获取内容提供者的地址:com.android.contacts
        // raw_contacts表的地址 :raw_contacts
        // view_data表的地址 : data
        // 3.生成查询地址
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri date_uri = Uri.parse("content://com.android.contacts/data");
        // 4.查询操作,先查询raw_contacts,查询contact_id
        // projection : 查询的字段
        Cursor cursor = resolver.query(raw_uri, new String[] { "contact_id" },
                null, null, null);
        // 5.解析cursor
        while (cursor.moveToNext()) {
            // 6.获取查询的数据
            String contact_id = cursor.getString(0);
            // cursor.getString(cursor.getColumnIndex("contact_id"));//getColumnIndex
            // : 查询字段在cursor中索引值,一般都是用在查询字段比较多的时候
            // 判断contact_id是否为空
            if (!TextUtils.isEmpty(contact_id)) {//null   ""
                // 7.根据contact_id查询view_data表中的数据
                // selection : 查询条件
                // selectionArgs :查询条件的参数
                // sortOrder : 排序
                // 空指针: 1.null.方法 2.参数为null
                Cursor c = resolver.query(date_uri, new String[] { "data1",
                                "mimetype" }, "raw_contact_id=?",
                        new String[] { contact_id }, null);
                HashMap<String, String> map = new HashMap<String, String>();
                // 8.解析c
                while (c.moveToNext()) {
                    // 9.获取数据
                    String data1 = c.getString(0);
                    String mimetype = c.getString(1);
                    // 10.根据类型去判断获取的data1数据并保存
                    if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                        // 电话
                        map.put("phone", data1);
                    } else if (mimetype.equals("vnd.android.cursor.item/name")) {
                        // 姓名
                        map.put("name", data1);
                    }
                }
                // 11.添加到集合中数据
                list.add(map);
                // 12.关闭cursor
                c.close();
            }
        }
        // 12.关闭cursor
        cursor.close();
        return list;
    }

}
