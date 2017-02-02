package com.pic.yourpics;

public interface ConnectionState {
    void onConnectedService(String name);
    void onUriLoadedSuccessful(String url);
    void onDisconnectedService(String name);
}
