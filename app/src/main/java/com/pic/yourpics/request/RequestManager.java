package com.pic.yourpics.request;

import com.pic.yourpics.service.Constants;
import com.pic.yourpics.request.callback.OnRequestListener;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RequestManager {

    private OkHttpClient mClient;

    private static RequestManager INSTANCE = null;

    private RequestManager() {
        mClient = new OkHttpClient();
    }

    public static RequestManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RequestManager();
        return INSTANCE;
    }

    public void newRequest(Request request, OnRequestListener listener, Constants.REQUEST_TYPE type) {
        new AsyncRequest(mClient, listener, type).execute(request);
    }
}
