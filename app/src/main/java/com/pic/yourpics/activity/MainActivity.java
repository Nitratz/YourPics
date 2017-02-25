package com.pic.yourpics.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.pic.yourpics.service.Constants;
import com.pic.yourpics.R;
import com.pic.yourpics.activity.callback.OnNoTokenFound;
import com.pic.yourpics.service.ServiceManager;
import com.pic.yourpics.fragments.FragmentFavorite;
import com.pic.yourpics.fragments.FragmentHome;
import com.pic.yourpics.fragments.FragmentProfile;
import com.pic.yourpics.fragments.FragmentUpload;
import com.pic.yourpics.service.AService;
import com.pic.yourpics.service.FlickrService;
import com.pic.yourpics.service.ImgurService;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnNoTokenFound {

    private BottomBar mBottomBar;
    private Toolbar mToolbar;
    private ArrayList<AService> mServiceList;
    private FirebaseAnalytics mFirebaseAnalytics;

    private FragmentProfile mFragProfile;
    private FragmentFavorite mFragFav;
    private FragmentHome mFragHome;
    private FragmentUpload mFragUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
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
                switch (tabId) {
                    case R.id.tab_home:
                        analyticsBottomBar(R.id.tab_profile, "TabHome");
                        replaceFragment(R.id.fragment, mFragHome);
                        break;
                    case R.id.tab_fav:
                        analyticsBottomBar(R.id.tab_profile, "TabFavorite");
                        replaceFragment(R.id.fragment, mFragFav);
                        break;
                    case R.id.tab_upload:
                        analyticsBottomBar(R.id.tab_profile, "TabUpload");
                        replaceFragment(R.id.fragment, mFragUpload);
                        break;
                    case R.id.tab_profile:
                        analyticsBottomBar(R.id.tab_profile, "TabProfile");
                        replaceFragment(R.id.fragment, mFragProfile);
                        break;
                }
            }
        });
    }

    private void analyticsBottomBar(int id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(id));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, name);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void replaceFragment(int id, Fragment frag) {
        FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
        fTransaction.replace(id, frag);
        fTransaction.commit();
    }

    private void fillServiceList() {
        mServiceList.add(new FlickrService(this, Constants.FLICKR_API_KEY, Constants.FLICKR_API_SECRET));
        mServiceList.add(new ImgurService(this, Constants.IMGUR_API_KEY, Constants.IMGUR_API_SECRET));
        ServiceManager.getInstance().setServiceList(mServiceList);
    }

    @Override
    public void onFailedToLoadHome() {
        mBottomBar.selectTabAtPosition(3);
        replaceFragment(R.id.fragment, mFragProfile);
    }

    /**
     * @param data Intent get from onActivityResult
     * @return Bitmap get from data
     */
    private Bitmap getDataFromResult(Intent data) {
        if (data != null) {
            try {
                return MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 21:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap image = getDataFromResult(data);
                    if (image != null)
                        mFragUpload.resultGallery(image);
                }
                break;
        }
    }
}
