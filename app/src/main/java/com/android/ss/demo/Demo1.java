package com.android.ss.demo;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.ss.Sunnylibrary.BaseActivity;
import com.android.ss.Sunnylibrary.BindLayout;
import com.android.ss.Sunnylibrary.OKHttpUtils;
import com.android.zhaoqinglang.ss.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;

@BindLayout(id = R.layout.activity_demo1)
public class Demo1 extends BaseActivity {
    @BindView(R.id.demo1_text)
    TextView mText;
    @BindView(R.id.demo1_btn)
    Button mBtn;
    @Override
    protected void onBaseActivityCreate() {
        super.onBaseActivityCreate();
        mBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getData(){

    }
}
