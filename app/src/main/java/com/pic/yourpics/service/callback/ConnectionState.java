package com.pic.yourpics.service.callback;

public interface ConnectionState {
    void onConnectedService(String name);
    void onUriLoadedSuccessful(String url);
    void onDisconnectedService(String name);
}
