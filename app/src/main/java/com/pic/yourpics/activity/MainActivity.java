package com.pic.yourpics.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.pic.yourpics.R;
import com.pic.yourpics.fragments.FragmentFavorite;
import com.pic.yourpics.fragments.FragmentHome;
import com.pic.yourpics.fragments.FragmentProfile;
import com.pic.yourpics.fragments.FragmentUpload;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;
    private Toolbar mToolbar;

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

        mFragProfile = new FragmentProfile();
        mFragHome = new FragmentHome();
        mFragUpload = new FragmentUpload();
        mFragFav = new FragmentFavorite();
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        mBottomBar.setOnTabSelectListener(tabId -> {
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
        });
    }
}
