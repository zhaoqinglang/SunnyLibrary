package com.android.ss.Sunnylibrary;


import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {
    private static Toast mToast;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindLayout bindLayoutId=getClass().getAnnotation(BindLayout.class);
        if(bindLayoutId!=null){
            setContentView(bindLayoutId.id());
        }else{
            throw new RuntimeException("不能使用空的布局ID。");
        }
        ButterKnife.bind(this);
        onBaseActivityCreate();
    }

    protected void onBaseActivityCreate(){}
    public void showToast(String msg) {
        if (null == mToast) {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.BOTTOM, 0, 0);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
