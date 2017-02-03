package com.pic.yourpics.request.callback;

import com.pic.yourpics.service.Constants;

public interface OnRequestListener {
    void onSuccess(String response, Constants.REQUEST_TYPE type);
    void onError(String error);
}
