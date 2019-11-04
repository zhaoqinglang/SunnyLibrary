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

import okhttp3.Call;
import okhttp3.Callback;
import butterknife.BindView;
import okhttp3.Response;
import okhttp3.ResponseBody;

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
                getData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mid",String.valueOf(168842));
            jsonObject.put("pagesize",String.valueOf(50));
            jsonObject.put("page",String.valueOf(1));
            OKHttpUtils.postRequest(UrlsConstant.MY_SHARED,jsonObject.toString(),new Callback(){
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("ssss","onFailure call = "+call.toString());
                    Log.d("ssss","onFailure IOException = "+e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.d("ssss","onResponse call = "+call.toString());
                    Log.d("ssss","onResponse Response = "+response);
                    ResponseBody responseBody = response.body();
                    Log.d("ssss","responseBody = "+responseBody.string());
                }
            });
        }catch (Exception e){
            Log.d("ssss","e = "+e);
            e.printStackTrace();
        }
    }
}
