package com.android.ss.Sunnylibrary;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    private static String oldMsg ;
    /** Toast对象 */
    private static Toast mToast = null ;
    /** 第一次时间 */
    private static long oneTime = 0 ;
    /** 第二次时间 */
    private static long twoTime = 0 ;

    /**
     * 显示Toast
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message){
        if(mToast == null){
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            mToast.show() ;
            oneTime = System.currentTimeMillis() ;
        }else{
            twoTime = System.currentTimeMillis() ;
            if(message.equals(oldMsg)){
                if(twoTime - oneTime > Toast.LENGTH_SHORT){
                    mToast.show() ;
                }
            }else{
                oldMsg = message ;
                mToast.setText(message) ;
                mToast.show() ;
            }
        }
        oneTime = twoTime ;
    }
}
