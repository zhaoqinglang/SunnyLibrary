package com.android.ss.Sunnylibrary.view;

import android.content.Context;
import android.util.AttributeSet;

import com.android.ss.Sunnylibrary.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import okhttp3.internal.Util;

public class MySmartRefreshLayout extends SmartRefreshLayout {
    Context mContext;
    public MySmartRefreshLayout(Context context) {
        super(context);
        mContext = context;
        initHeaderView();
        initFooterView();
        setEnableRefresh(true);//是否启用下拉刷新功能
        setEnableLoadmore(true);//是否启用上拉加载功能
        setHeaderTriggerRate(1.0f);
        setFooterTriggerRate(1.0f);
    }

    public MySmartRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initHeaderView();
        initFooterView();
        setEnableRefresh(true);//是否启用下拉刷新功能
        setEnableLoadmore(true);//是否启用上拉加载功能
        setHeaderTriggerRate(1.0f);
        setFooterTriggerRate(1.0f);
    }

    public MySmartRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initHeaderView();
        initFooterView();
        setEnableRefresh(true);//是否启用下拉刷新功能
        setEnableLoadmore(true);//是否启用上拉加载功能
        setHeaderTriggerRate(1.0f);
        setFooterTriggerRate(1.0f);
    }

    private void initHeaderView(){
        setRefreshHeader(new RefreshHeaderView(mContext), SmartRefreshLayout.LayoutParams
                .MATCH_PARENT, Utils.dp2px(mContext,50));
    }

    private void initFooterView(){
        setRefreshFooter(new RefreshFooterView(mContext),SmartRefreshLayout.LayoutParams
                .MATCH_PARENT,Utils.dp2px(mContext,50));
    }
}
