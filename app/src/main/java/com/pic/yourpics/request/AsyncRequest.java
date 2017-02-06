package com.pic.yourpics.request;

import android.os.AsyncTask;

import com.google.firebase.crash.FirebaseCrash;
import com.pic.yourpics.service.Constants;
import com.pic.yourpics.request.callback.OnRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class AsyncRequest extends AsyncTask<Request, Void, String> {

    private OkHttpClient mClient;
    private OnRequestListener mListener;
    private Constants.REQUEST_TYPE mType;

    AsyncRequest(OkHttpClient client, OnRequestListener listener, Constants.REQUEST_TYPE type) {
        mClient = client;
        mListener = listener;
        mType = type;
    }

    @Override
    protected String doInBackground(Request... params) {
        Request request = params[0];
        Response response = null;

        try {
            response = mClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            if (s == null) {
                mListener.onError("String is null");
                return;
            }
            JSONObject obj = new JSONObject(s);
            if (!obj.getBoolean("success"))
                mListener.onError(s);
            else
                mListener.onSuccess(s, mType);
        } catch (JSONException e) {
            FirebaseCrash.report(e);
            e.printStackTrace();
            mListener.onError("Error parsing JSON");
        }
    }
}
