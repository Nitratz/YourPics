package com.pic.yourpics.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.pic.yourpics.R;
import com.pic.yourpics.ServiceManager;
import com.pic.yourpics.fragments.FragmentFavorite;
import com.pic.yourpics.fragments.FragmentHome;
import com.pic.yourpics.fragments.FragmentProfile;
import com.pic.yourpics.fragments.FragmentUpload;
import com.pic.yourpics.model.service.AService;
import com.pic.yourpics.model.service.FlickrService;
import com.pic.yourpics.model.service.ImgurService;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;
    private Toolbar mToolbar;
    private ArrayList<AService> mServiceList;

    private FragmentProfile mFragProfile;
    private FragmentFavorite mFragFav;
    private FragmentHome mFragHome;
    private FragmentUpload mFragUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mServiceList = new ArrayList<>();
        fillServiceList();

        mFragProfile = new FragmentProfile();
        mFragHome = new FragmentHome();
        mFragUpload = new FragmentUpload();
        mFragFav = new FragmentFavorite();
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
                switch (tabId) {
                    case R.id.tab_home:
                        fTransaction.replace(R.id.fragment, mFragHome);
                        break;
                    case R.id.tab_fav:
                        fTransaction.replace(R.id.fragment, mFragFav);
                        break;
                    case R.id.tab_upload:
                        fTransaction.replace(R.id.fragment, mFragUpload);
                        break;
                    case R.id.tab_profile:
                        fTransaction.replace(R.id.fragment, mFragProfile);
                        break;
                }
                fTransaction.commit();
            }
        });
    }

    private void fillServiceList() {
        mServiceList.add(new FlickrService(this, "722996a4754d4aa80ce5c4956d8e35ac", "e835a0312d25855b"));
        mServiceList.add(new ImgurService(this, "eb007454f35153b", "b00197530b399a5a7dce6a16dcc7d98668c06a0c"));
        ServiceManager.getInstance().setServiceList(mServiceList);
    }
}
