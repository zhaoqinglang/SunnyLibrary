package com.android.ss.Sunnylibrary.view;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

public class RefreshHeaderView extends RelativeLayout implements RefreshHeader {
    ProgressBar mProgress;
    Context mContext;
    View mLayout;
    public RefreshHeaderView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public int onFinish(RefreshLayout refreshLayout, boolean success) {
//        if(success){
//            mProgress.setVisibility(View.GONE);
//        }
        return 200;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case PullDownToRefresh:
//                mProgress.setVisibility(GONE);
                break;
            case Refreshing:
                mProgress.setVisibility(VISIBLE);
                break;
            case ReleaseToRefresh:
                mProgress.setVisibility(VISIBLE);
                break;
        }
    }

    private void initView(){
//        mLayout = View.inflate(mContext, R.layout.refresh_header,this);
//        mProgress = (ProgressBar) mLayout.findViewById(R.id.refresh_layout_bar);
//        CircularProgressDrawable progressDrawable = new CircularProgressDrawable(mContext);
//        progressDrawable.setColorSchemeColors(Constants.getRefreshColor(mContext));
//        mProgress.setProgressDrawable(progressDrawable);
//        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {

    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {

    }

    @Override
    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {

    }
}
