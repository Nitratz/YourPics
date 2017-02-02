package com.pic.yourpics.model.service;

import android.content.Context;
import android.support.test.espresso.core.deps.guava.base.Splitter;

import com.pic.yourpics.ConnectionState;
import com.pic.yourpics.model.ImgurToken;

import java.util.ArrayList;
import java.util.Map;

public class ImgurService extends AService {

    private String mUrl;

    public ImgurService(Context context) {
        super(context);
        mServiceName = "Imgur";
        mRedirectLink = "http://localhost/?state=imgur";
        mUrl = "https://api.imgur.com/oauth2/authorize?client_id=eb007454f35153b&response_type=token&state=imgur";
    }

    public ImgurService(Context context, String apiKey, String apiSecret) {
        super(context, apiKey, apiSecret);
        mServiceName = "Imgur";
        mRedirectLink = "http://localhost/?state=imgur";
        mUrl = "https://api.imgur.com/oauth2/authorize?client_id=eb007454f35153b&response_type=token&state=imgur";
    }

    @Override
    public void disconnectService() {

    }

    @Override
    public boolean onUserAuthorize(String url) {
        String[] urlSplitted = url.split("#");
        if (url.length() > mRedirectLink.length()) {
            if (urlSplitted[0].equals(mRedirectLink)) {
                ImgurToken token = new ImgurToken();
                url = url.split("#")[1];
                Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(url);
                token.setToken(map.get("access_token"))
                        .setRefreshToken(map.get("refresh_token"))
                        .setUserName(map.get("account_username"))
                        .setAccountId(map.get("account_id"));
                token.save();
                ArrayList<ImgurToken> list = (ArrayList<ImgurToken>) ImgurToken.find(ImgurToken.class, "account_id = ?", token.getAccountId());
                ImgurToken lol = list.get(0);
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
}
