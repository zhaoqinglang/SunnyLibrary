package com.android.ss.Sunnylibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.ITelephony;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
            Log.d("ssss","isMultiSim Exception = "+e);
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
            Log.d("ssss","isMultiSim Exception = "+e);
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


}
