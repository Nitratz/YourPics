package com.pic.yourpics.model;

import com.orm.SugarRecord;
import com.orm.dsl.Column;

public class ImgurToken extends SugarRecord {
    @Column(name="token")
    private String mToken;

    @Column(name="refresh_token")
    private String mRefreshToken;

    @Column(name="username")
    private String mUserName;

    @Column(name="account_id")
    private String mAccountId;

    public String getToken() {
        return mToken;
    }

    public ImgurToken setToken(String mToken) {
        this.mToken = mToken;
        return this;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    public ImgurToken setRefreshToken(String mRefreshToken) {
        this.mRefreshToken = mRefreshToken;
        return this;
    }

    public String getUserName() {
        return mUserName;
    }

    public ImgurToken setUserName(String mUserName) {
        this.mUserName = mUserName;
        return this;
    }

    public String getAccountId() {
        return mAccountId;
    }

    public void setAccountId(String mAccountId) {
        this.mAccountId = mAccountId;
    }
}
