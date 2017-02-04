package com.pic.yourpics.service;

import android.content.Context;
import android.os.Handler;
import android.support.test.espresso.core.deps.guava.base.Splitter;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.pic.yourpics.service.callback.ConnectionState;
import com.pic.yourpics.service.tokens.FlickrToken;
import com.pic.yourpics.service.tokens.ImgurToken;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

public class FlickrService extends AService {

    private FlickrToken mAuthToken;
    private OAuthInterface mInterfaceFlickr;
    private OAuthToken mOAuthToken;
    private OAuth mAuth;
    private String mUrl;
    private String mState;

    private Thread mAuthThread;
    private Runnable mAuthRunnable;
    private Thread mTokenThread;
    private Runnable mTokenRunnable;
    private Handler mTokenHandler;
    private Handler mAuthHandler;
    private boolean isDone = false;

    public FlickrService(Context context) {
        super(context);
        getStoredToken();
        mRedirectLink = "http://localhost/";
        mState = "?state=flickr";
        mServiceName = "Flickr";
    }

    public FlickrService(Context context, String apiKey, String apiSecret) {
        super(context, apiKey, apiSecret);
        getStoredToken();
        mRedirectLink = "http://localhost/";
        mState = "?state=flickr";
        mServiceName = "Flickr";
    }

    @Override
    public boolean onUserAuthorize(String url) {
        isDone = false;
        int count = 0;
        String[] urlSplitted = url.split("&");

        if (urlSplitted[0].equals(mRedirectLink + mState)) {
            if (mTokenThread != null)
                mTokenThread.interrupt();
            Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(url);
            if (mAuthRunnable == null)
                createAuthRunnable(map);
            if (mAuthThread == null)
                mAuthThread = new Thread(mAuthRunnable);
            mAuthThread.start();
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
            mTokenHandler = new Handler();
            if (mTokenRunnable == null)
                createTokenRunnable();
            if (mTokenThread == null)
                mTokenThread = new Thread(mTokenRunnable);
            mTokenThread.start();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }
    }

    private void createTokenRunnable() {
        mTokenRunnable = new Runnable() {
            @Override
            public void run() {
                while (!isDone) {
                    try {
                        mOAuthToken = mInterfaceFlickr.getRequestToken(mRedirectLink + mState);
                    } catch (IOException | FlickrException e) {
                        e.printStackTrace();
                    }
                    if (mOAuthToken != null) {
                        isDone = true;
                        Log.d("myFlickrToken", "Token Flickr done !");
                        mTokenHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mUrl = mInterfaceFlickr.buildAuthenticationUrl(com.googlecode.flickrjandroid.auth.Permission.DELETE, mOAuthToken).toString();
                                    ((ConnectionState) mCurrentFragment).onUriLoadedSuccessful(mUrl);
                                } catch (MalformedURLException e) {
                                    FirebaseCrash.report(e);
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
                        mAuth = mInterfaceFlickr.getAccessToken(mOAuthToken.getOauthToken(), mOAuthToken.getOauthTokenSecret(), map.get("oauth_verifier"));

                        isDone = true;
                        mAuthToken.setToken(mOAuthToken.getOauthToken());
                        mAuthToken.setAccountId(mAuth.getUser().getId());
                        mAuthToken.setUserName(mAuth.getUser().getRealName());
                        mAuthToken.save();
                        mTokenHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((ConnectionState) mCurrentFragment).onConnectedService(mServiceName);
                            }
                        });
                    } catch (IOException | FlickrException e) {
                        FirebaseCrash.report(e);
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private void getStoredToken() {
        List<FlickrToken> tokens = ImgurToken.listAll(FlickrToken.class);
        if (tokens != null && tokens.size() > 0) {
            mAuthToken = tokens.get(0);
            isConnected = true;
        } else
            mAuthToken = new FlickrToken();
        mToken = mAuthToken.getToken();
    }

    @Override
    public void disconnectService() {
        mToken = null;
        mAuthToken.delete();
        mAuthToken = new FlickrToken();
        isConnected = false;
        ((ConnectionState) mCurrentFragment).onDisconnectedService(mServiceName);
    }
}
