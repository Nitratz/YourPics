package com.pic.yourpics.service.tokens;

import com.orm.SugarRecord;
import com.orm.dsl.Column;

public class FlickrToken extends SugarRecord {

    @Column(name="token")
    private String mToken;

    @Column(name="username")
    private String mUserName;

    @Column(name="account_id")
    private String mAccountId;

    public String getToken() {
        return mToken;
    }

    public FlickrToken setToken(String mToken) {
        this.mToken = mToken;
        return this;
    }

    public String getUserName() {
        return mUserName;
    }

    public FlickrToken setUserName(String mUserName) {
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
