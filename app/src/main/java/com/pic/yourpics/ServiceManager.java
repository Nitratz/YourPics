package com.pic.yourpics;

import android.support.v4.app.Fragment;

import com.pic.yourpics.model.service.AService;

import java.util.ArrayList;

public class ServiceManager {

    private ArrayList<AService> mServiceList;

    private static ServiceManager INSTANCE = null;

    private ServiceManager() {
    }

    public static ServiceManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ServiceManager();
        return INSTANCE;
    }

    public ArrayList<AService> getServiceList() {
        return mServiceList;
    }

    public void setServiceList(ArrayList<AService> mServiceList) {
        this.mServiceList = mServiceList;
    }

    public AService getServiceByName(String name) {
        for (AService service : mServiceList) {
            if (service.getServiceName().equals(name))
                return service;
        }
        return null;
    }

    public void setCurrentFragmentList(Fragment frag) {
        for (AService service : mServiceList) {
            service.setCurrentFragment(frag);
        }
    }
}
