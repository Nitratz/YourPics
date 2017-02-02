package com.pic.yourpics.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.pic.yourpics.service.callback.ConnectionState;
import com.pic.yourpics.R;
import com.pic.yourpics.service.ServiceManager;
import com.pic.yourpics.service.AService;

import java.util.ArrayList;

public class FragmentProfile extends Fragment implements View.OnClickListener, ConnectionState {

    private Context mContext;
    private View mView;
    private ArrayList<AService> mServiceList;

    private ProgressDialog mProgress;
    private Button mImgurConnect;
    private Button mImgurDisconnect;
    private Button mImgurProfile;
    private Button mFlickrConnect;
    private Button mFlickrDisconnect;
    private Button mFlickrProfile;

    private WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ServiceManager.getInstance().setCurrentFragmentList(this);
        if (mView != null)
            return mView;
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = getActivity();
        mServiceList = ServiceManager.getInstance().getServiceList();

        mWebView = (WebView) mView.findViewById(R.id.webview);
        mImgurConnect = (Button) mView.findViewById(R.id.connect_imgur);
        mImgurDisconnect = (Button) mView.findViewById(R.id.disconnect_imgur);
        mImgurProfile = (Button) mView.findViewById(R.id.profile_imgur);
        mFlickrConnect = (Button) mView.findViewById(R.id.connect_flickr);
        mFlickrDisconnect = (Button) mView.findViewById(R.id.disconnect_flickr);
        mFlickrProfile = (Button) mView
                .findViewById(R.id.profile_flickr);
        mProgress = new ProgressDialog(mContext);

        mProgress.setCancelable(false);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setMessage(mContext.getString(R.string.loading_connect));
        mImgurConnect.setOnClickListener(this);
        mImgurDisconnect.setOnClickListener(this);
        mImgurProfile.setOnClickListener(this);
        mFlickrConnect.setOnClickListener(this);
        mFlickrDisconnect.setOnClickListener(this);
        mFlickrProfile.setOnClickListener(this);

        prepareWebView();
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_imgur:
                connectImgur();
                break;
            case R.id.disconnect_imgur:
                disConnectImgur();
                break;
            case R.id.profile_imgur:
                break;
            case R.id.connect_flickr:
                connectFlickr();
                break;
            case R.id.disconnect_flickr:
                disconnectFlickr();
                break;
            case R.id.profile_flickr:
                break;
        }
    }

    private void prepareWebView() {
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("myURL", url);
                for (AService service : mServiceList) {
                    if (service.onUserAuthorize(url))
                        break;
                }
            }

            public void onPageFinished(WebView view, String url) {
                mProgress.dismiss();
            }
        });
    }

    private void connectImgur() {
        mProgress.show();
        AService imgur = ServiceManager.getInstance().getServiceByName("Imgur");
        imgur.loadAuthLink();
    }

    private void disConnectImgur() {
        AService imgur = ServiceManager.getInstance().getServiceByName("Imgur");
        imgur.disconnectService();
    }

    private void connectFlickr() {
        mProgress.show();
        AService flickr = ServiceManager.getInstance().getServiceByName("Flickr");
        flickr.loadAuthLink();
    }

    private void disconnectFlickr() {
        AService flickr = ServiceManager.getInstance().getServiceByName("Flickr");
        flickr.disconnectService();
    }

    @Override
    public void onConnectedService(String name) {
        mWebView.setVisibility(View.GONE);
        switch (name) {
            case "Imgur":
                setImgurConnected();
                break;
            case "Flickr":
                setFlickrConnected();
                break;
        }
    }

    @Override
    public void onUriLoadedSuccessful(String url) {
        mWebView.setVisibility(View.VISIBLE);
        mWebView.loadUrl(url);
    }

    @Override
    public void onDisconnectedService(String name) {
        switch (name) {
            case "Imgur":
                setImgurDisconnected();
                break;
            case "Flickr":
                setFlickrDisConnected();
                break;
        }
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

    private void setFlickrConnected() {
        mFlickrDisconnect.setVisibility(View.VISIBLE);
        mFlickrConnect.setVisibility(View.GONE);
        mFlickrProfile.setVisibility(View.VISIBLE);
    }

    private void setFlickrDisConnected() {
        mFlickrDisconnect.setVisibility(View.GONE);
        mFlickrConnect.setVisibility(View.VISIBLE);
        mFlickrProfile.setVisibility(View.GONE);
    }
}
