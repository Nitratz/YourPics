package com.pic.yourpics.service;

import android.content.Context;
import android.support.test.espresso.core.deps.guava.base.Splitter;

import com.pic.yourpics.service.callback.ConnectionState;
import com.pic.yourpics.service.tokens.ImgurToken;

import java.util.List;
import java.util.Map;

public class ImgurService extends AService {

    private ImgurToken mAuthToken;
    private String mUrl;

    public ImgurService(Context context) {
        super(context);
        getStoredToken();
        mServiceName = "Imgur";
        mRedirectLink = "http://localhost/?state=imgur";
        mUrl = "https://api.imgur.com/oauth2/authorize?client_id=eb007454f35153b&response_type=token&state=imgur";
    }

    public ImgurService(Context context, String apiKey, String apiSecret) {
        super(context, apiKey, apiSecret);
        getStoredToken();
        mServiceName = "Imgur";
        mRedirectLink = "http://localhost/?state=imgur";
        mUrl = "https://api.imgur.com/oauth2/authorize?client_id=eb007454f35153b&response_type=token&state=imgur";
    }

    @Override
    public boolean onUserAuthorize(String url) {
        String[] urlSplitted = url.split("#");
        if (url.length() > mRedirectLink.length()) {
            if (urlSplitted[0].equals(mRedirectLink)) {
                Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(urlSplitted[1]);
                mAuthToken.setToken(map.get("access_token"))
                        .setRefreshToken(map.get("refresh_token"))
                        .setUserName(map.get("account_username"))
                        .setAccountId(map.get("account_id"));
                mToken = mAuthToken.getToken();
                mAuthToken.save();
                ((ConnectionState) mCurrentFragment).onConnectedService(mServiceName);
                return true;
            }
        }
        return false;
    }

    @Override
    public void loadAuthLink() {
        ((ConnectionState) mCurrentFragment).onUriLoadedSuccessful(mUrl);
    }

    private void getStoredToken() {
        List<ImgurToken> tokens = ImgurToken.listAll(ImgurToken.class);
        if (tokens != null && tokens.size() > 0) {
            mAuthToken = tokens.get(0);
            isConnected = true;
        } else
            mAuthToken = new ImgurToken();
        mToken = mAuthToken.getToken();
    }

    @Override
    public void disconnectService() {
        mToken = null;
        mAuthToken = new ImgurToken();
        isConnected = false;
        ((ConnectionState) mCurrentFragment).onDisconnectedService(mServiceName);
    }
}
