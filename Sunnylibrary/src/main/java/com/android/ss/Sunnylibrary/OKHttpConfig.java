package com.android.ss.Sunnylibrary;

import java.util.concurrent.TimeUnit;

public class OKHttpConfig {
    private long mConnectTime = 60;
    private long mReadTime = 60;
    private long mWriteTime = 60;
    private boolean mRetryOnConnectionFailure = true;
    private TimeUnit mTimeUnit = TimeUnit.SECONDS;

    public OKHttpConfig() {
    }

    public OKHttpConfig(long mConnectTime, long mReadTime, long mWriteTime, boolean mRetryOnConnectionFailure, TimeUnit mTimeUnit) {
        this.mConnectTime = mConnectTime;
        this.mReadTime = mReadTime;
        this.mWriteTime = mWriteTime;
        this.mRetryOnConnectionFailure = mRetryOnConnectionFailure;
        this.mTimeUnit = mTimeUnit;
    }

    public long getmConnectTime() {
        return mConnectTime;
    }

    public void setmConnectTime(long mConnectTime) {
        this.mConnectTime = mConnectTime;
    }

    public long getmReadTime() {
        return mReadTime;
    }

    public void setmReadTime(long mReadTime) {
        this.mReadTime = mReadTime;
    }

    public long getmWriteTime() {
        return mWriteTime;
    }

    public void setmWriteTime(long mWriteTime) {
        this.mWriteTime = mWriteTime;
    }

    public boolean ismRetryOnConnectionFailure() {
        return mRetryOnConnectionFailure;
    }

    public void setmRetryOnConnectionFailure(boolean mRetryOnConnectionFailure) {
        this.mRetryOnConnectionFailure = mRetryOnConnectionFailure;
    }

    public TimeUnit getmTimeUnit() {
        return mTimeUnit;
    }

    public void setmTimeUnit(TimeUnit mTimeUnit) {
        this.mTimeUnit = mTimeUnit;
    }
}
