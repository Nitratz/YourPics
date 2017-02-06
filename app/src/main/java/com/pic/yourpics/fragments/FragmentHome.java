package com.pic.yourpics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.crash.FirebaseCrash;
import com.pic.yourpics.request.parser.ImgurParser;
import com.pic.yourpics.service.Constants;
import com.pic.yourpics.R;
import com.pic.yourpics.activity.callback.OnNoTokenFound;
import com.pic.yourpics.adapter.HomeAdapter;
import com.pic.yourpics.model.Album;
import com.pic.yourpics.request.RequestManager;
import com.pic.yourpics.request.callback.OnRequestListener;
import com.pic.yourpics.service.AService;
import com.pic.yourpics.service.ServiceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Request;

import static com.pic.yourpics.service.Constants.REQUEST_TYPE.ALBUM;
import static com.pic.yourpics.service.Constants.REQUEST_TYPE.GALLERY;

public class FragmentHome extends Fragment implements OnRequestListener {

    private Context mContext;
    private View mView;
    private ArrayList<AService> mServiceList;
    private ImgurParser mImgurParser;

    private ArrayList<Album> mListAlbum;
    private RecyclerView mRecycler;

    private StaggeredGridLayoutManager mGridLayout;
    private HomeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView != null)
            return mView;
        mServiceList = ServiceManager.getInstance().getServiceList();
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity();
        mListAlbum = new ArrayList<>();
        mImgurParser = new ImgurParser(mContext, this);

        mRecycler = (RecyclerView) mView.findViewById(R.id.recycler_home);

        mGridLayout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(mGridLayout);
        mAdapter = new HomeAdapter(mContext, mListAlbum);
        mRecycler.setAdapter(mAdapter);
        mImgurParser.requestImgurGallery();

        return mView;
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
        Log.d("myError", error);
    }
}
