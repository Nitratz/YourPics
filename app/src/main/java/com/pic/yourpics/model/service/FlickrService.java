package com.pic.yourpics.model.service;

import android.content.Context;
import android.os.Handler;
import android.support.test.espresso.core.deps.guava.base.Splitter;
import android.support.test.espresso.core.deps.guava.util.concurrent.Runnables;
import android.util.Log;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.pic.yourpics.ConnectionState;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

public class FlickrService extends AService {

    private OAuthInterface mInterfaceFlickr;
    private OAuthToken mToken;
    private OAuth mAuth;
    private String mState;

    private Thread mAuthThread;
    private Runnable mAuthRunnable;
    private Thread mTokenThread;
    private Runnable mTokenRunnable;
    private Handler mHandler;
    private boolean isDone = false;

    public FlickrService(Context context) {
        super(context);
        mRedirectLink = "http://localhost/";
        mState = "?state=flickr";
        mServiceName = "Flickr";
    }

    public FlickrService(Context context, String apiKey, String apiSecret) {
        super(context, apiKey, apiSecret);
        mRedirectLink = "http://localhost/";
        mState = "?state=flickr";
        mServiceName = "Flickr";
    }

    @Override
    public void disconnectService() {

    }

    @Override
    public boolean onUserAuthorize(String url) {
        isDone = false;
        int count = 0;
        String[] urlSplitted = url.split("&");

        if (urlSplitted[0].equals(mRedirectLink + mState)) {
            Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(url);
            if (mAuthRunnable == null)
                createAuthRunnable(map);
            if (mAuthThread == null)
                mAuthThread = new Thread(mAuthRunnable);
            mAuthThread.start();
            while (!isDone) {
                count++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (count == 2000) {
                    isDone = true;
                    return false;
                }
            }
            ((ConnectionState) mCurrentFragment).onConnectedService(mServiceName);
            return true;
        }
        return false;
    }

    @Override
    public void loadAuthLink() {
        try {
            isDone = false;
            Flickr flickr1 = new Flickr(mApiKey, mApiSecret, new REST());
            mInterfaceFlickr = flickr1.getOAuthInterface();
            mHandler = new Handler();
            if (mTokenRunnable == null)
                createTokenRunnable();
            if (mTokenThread == null)
                mTokenThread = new Thread(mTokenRunnable);
            mTokenThread.start();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void createTokenRunnable() {
        mTokenRunnable = new Runnable() {
            @Override
            public void run() {
                while (!isDone) {
                    try {
                        mToken = mInterfaceFlickr.getRequestToken(mRedirectLink + mState);
                    } catch (IOException | FlickrException e) {
                        e.printStackTrace();
                    }
                    if (mToken != null) {
                        isDone = true;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String authLink = mInterfaceFlickr.buildAuthenticationUrl(com.googlecode.flickrjandroid.auth.Permission.DELETE, mToken).toString();
                                    ((ConnectionState) mCurrentFragment).onUriLoadedSuccessful(authLink);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        };
    }

    private void createAuthRunnable(final Map<String, String> map) {
        mAuthRunnable = new Runnable() {
            @Override
            public void run() {
                while (!isDone) {
                    try {
                        mAuth = mInterfaceFlickr.getAccessToken(mToken.getOauthToken(), mToken.getOauthTokenSecret(), map.get("oauth_verifier"));

                        isDone = true;
                        Log.d("myNameFlickr", mAuth.getUser().getRealName());
                        Log.d("myIdFlickr", mAuth.getUser().getId());
                    } catch (IOException | FlickrException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}
