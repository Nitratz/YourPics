package com.pic.yourpics.fragments;

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

import com.pic.yourpics.R;

import java.util.Map;

public class FragmentProfile extends Fragment {

    private WebView mWebView;
    private String mRedirect = "http://localhost";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        mWebView = (WebView) v.findViewById(R.id.webview);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("myURL", url);
                if (url.length() > mRedirect.length())
                    if (url.substring(0, 16).equals(mRedirect)) {
                        url = url.substring(27, url.length());
                        Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(url);
                        String token = map.get("access_token");
                        mWebView.setVisibility(View.GONE);
                    }
            }
        });
        mWebView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=eb007454f35153b&response_type=token&state=ok");

        return v;
    }
}
