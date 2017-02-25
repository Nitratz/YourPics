package com.pic.yourpics.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.crash.FirebaseCrash;
import com.pic.yourpics.request.parser.ImgurParser;
import com.pic.yourpics.service.Constants;
import com.pic.yourpics.R;
import com.pic.yourpics.adapter.HomeAdapter;
import com.pic.yourpics.model.Album;
import com.pic.yourpics.request.callback.OnRequestListener;
import com.pic.yourpics.service.AService;
import com.pic.yourpics.service.ServiceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentHome extends Fragment implements OnRequestListener {

    private Context mContext;
    private View mView;
    private AlertDialog mAlertDialog;
    private ArrayList<AService> mServiceList;
    private ImgurParser mImgurParser;

    private ArrayList<Album> mListAlbum;
    private RecyclerView mRecycler;

    private StaggeredGridLayoutManager mGridLayout;
    private HomeAdapter mAdapter;
    private boolean mPaused;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView != null)
            return mView;
        mPaused = false;
        mServiceList = ServiceManager.getInstance().getServiceList();
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity();
        mListAlbum = new ArrayList<>();
        mImgurParser = new ImgurParser(mContext, this);

        mRecycler = (RecyclerView) mView.findViewById(R.id.recycler_home);

        mGridLayout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(mGridLayout);
        mAdapter = new HomeAdapter(this, mContext, mListAlbum);
        mRecycler.setAdapter(mAdapter);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPaused = false;
        if (mListAlbum.size() == 0)
            mImgurParser.requestImgurGallery(0);

    }

    @Override
    public void onPause() {
        super.onPause();
        mPaused = true;
    }

    @Override
    public void onSuccess(String response, Constants.REQUEST_TYPE type) {
        switch (type) {
            case ALBUM:
                try {
                    mImgurParser.parserAlbumImages(mListAlbum, new JSONObject(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                }
                mAdapter.stopLoading();
                mAdapter.notifyDataSetChanged();
                break;
            case GALLERY:
                try {
                    mListAlbum.addAll(mImgurParser.parserGallery(new JSONObject(response)));
                } catch (JSONException e) {
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                }
                break;
        }
    }

    @Override
    public void onError(String error) {
        if (mPaused)
            return;
        if (mAlertDialog != null) {
            mAlertDialog.show();
        }
        mAdapter.stopLoading();
        mAlertDialog = new AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.alert_error_title))
                .setCancelable(false)
                .setMessage(getString(R.string.alert_error_desc))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
