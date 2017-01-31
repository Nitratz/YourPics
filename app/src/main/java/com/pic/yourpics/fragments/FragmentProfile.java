package com.pic.yourpics.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.espresso.core.deps.guava.base.Splitter;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.pic.yourpics.R;
import com.pic.yourpics.model.Account;
import com.pic.yourpics.model.ImgurToken;

import java.util.ArrayList;
import java.util.Map;

public class FragmentProfile extends Fragment implements View.OnClickListener {

    private Context mContext;

    private Button mImgurConnect;
    private Button mImgurDisconnect;
    private Button mImgurProfile;
    private Button mFlickrConnect;
    private Button mFlickrDisconnect;
    private Button mFlickrProfile;

    private WebView mWebView;
    private String mRedirect = "http://localhost";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = getActivity();

        mWebView = (WebView) v.findViewById(R.id.webview);
        mImgurConnect = (Button) v.findViewById(R.id.connect_imgur);
        mImgurDisconnect = (Button) v.findViewById(R.id.disconnect_imgur);
        mImgurProfile = (Button) v.findViewById(R.id.profile_imgur);
        mFlickrConnect = (Button) v.findViewById(R.id.connect_flickr);
        mFlickrDisconnect = (Button) v.findViewById(R.id.disconnect_flickr);
        mFlickrProfile = (Button) v.findViewById(R.id.profile_flickr);

        mImgurConnect.setOnClickListener(this);
        mImgurDisconnect.setOnClickListener(this);
        mImgurProfile.setOnClickListener(this);
        mFlickrConnect.setOnClickListener(this);
        mFlickrDisconnect.setOnClickListener(this);
        mFlickrProfile.setOnClickListener(this);

        if (isImgurConnected())
            setImgurConnected();
        prepareWebView();
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_imgur:
                mWebView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=eb007454f35153b&response_type=token&state=ok");
                mWebView.setVisibility(View.VISIBLE);
                break;
            case R.id.disconnect_imgur:
                setImgurDisconnected();
                break;
            case R.id.profile_imgur:
                break;
            case R.id.connect_flickr:
                break;
            case R.id.disconnect_flickr:
                break;
            case R.id.profile_flickr:
                break;
        }
    }

    private void prepareWebView() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("myURL", url);
                if (url.length() > mRedirect.length())
                    if (url.substring(0, 16).equals(mRedirect)) {
                        onImgurConnexionSuccess(url);
                    }
            }
        });
    }

    private void setImgurConnected() {
        mImgurDisconnect.setVisibility(View.VISIBLE);
        mImgurConnect.setVisibility(View.GONE);
        mImgurProfile.setVisibility(View.VISIBLE);
    }

    private void setImgurDisconnected() {
        mImgurDisconnect.setVisibility(View.GONE);
        mImgurConnect.setVisibility(View.VISIBLE);
        mImgurProfile.setVisibility(View.GONE);
    }

    private boolean isImgurConnected() {
        ArrayList<ImgurToken> list = (ArrayList<ImgurToken>) ImgurToken.listAll(ImgurToken.class);
        if (list.size() > 0) {
            setImgurConnected();
            return true;
        }
        return false;
    }

    private void onImgurConnexionSuccess(String url) {
        ImgurToken token = new ImgurToken();
        url = url.substring(27, url.length());
        Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(url);
        token.setToken(map.get("access_token"))
                .setRefreshToken(map.get("refresh_token"))
                .setUserName(map.get("account_username"))
                .setAccountId(map.get("account_id"));
        token.save();
        mWebView.setVisibility(View.GONE);
        setImgurConnected();
        ArrayList<ImgurToken> list = (ArrayList<ImgurToken>) ImgurToken.find(ImgurToken.class, "account_id = ?", token.getAccountId());
        ImgurToken lol = list.get(0);
    }
}
