package com.pic.yourpics.service;

import android.content.Context;
import android.support.v4.app.Fragment;

public abstract class AService {

    protected Context mContext;
    protected Fragment mCurrentFragment;

    protected String mServiceName;
    protected String mToken;
    protected String mApiKey;
    protected String mApiSecret;
    protected String mRedirectLink;

    public AService(Context context) {
        mContext = context;
    }

    public AService(Context context, String apiKey, String apiSecret) {
        mContext = context;
        mApiKey = apiKey;
        mApiSecret = apiSecret;
    }

    public String getServiceName() {
        return mServiceName;
    }

    public void setServiceName(String mServiceName) {
        this.mServiceName = mServiceName;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

    public String getApiKey() {
        return mApiKey;
    }

    public void setApiKey(String mApiKey) {
        this.mApiKey = mApiKey;
    }

    public String getApiSecret() {
        return mApiSecret;
    }

    public void setApiSecret(String mApiSecret) {
        this.mApiSecret = mApiSecret;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setCurrentFragment(Fragment mCurrentFragment) {
        this.mCurrentFragment = mCurrentFragment;
    }

    public abstract void disconnectService();

    public abstract boolean onUserAuthorize(String url);

    public abstract void loadAuthLink();
}
