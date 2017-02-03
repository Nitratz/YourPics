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

import static com.pic.yourpics.service.Constants.REQUEST_TYPE.GALLERY;

public class FragmentHome extends Fragment implements OnRequestListener {

    private Context mContext;
    private View mView;
    private ArrayList<AService> mServiceList;

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

        mRecycler = (RecyclerView) mView.findViewById(R.id.recycler_home);

        mGridLayout = new StaggeredGridLayoutManager(2, 1);
        mRecycler.setLayoutManager(mGridLayout);
        mAdapter = new HomeAdapter(mContext, mListAlbum);
        mRecycler.setAdapter(mAdapter);
        requestImgur();

        return mView;
    }

    private void requestImgur() {
        AService imgur = ServiceManager.getInstance().getServiceByName("Imgur");
        if (imgur.getToken() == null) {
            ((OnNoTokenFound) mContext).onFailedToLoadHome();
            return;
        }
        Request request = new Request.Builder()
                .url(Constants.IMGUR_GALLERY + "/hot/viral/0")
                .addHeader("Authorization", "Bearer " + imgur.getToken()).build();
        RequestManager.getInstance().newRequest(request, this, GALLERY);
    }

    @Override
    public void onSuccess(String response, Constants.REQUEST_TYPE type) {
        switch (type) {
            case ALBUM:
                break;
            case GALLERY:
                try {
                    parserGallery(new JSONObject(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onError(String error) {
        Log.d("myError", error);
    }

    private void parserGallery(JSONObject object) throws JSONException {
        JSONArray data = object.getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {
            Album album = new Album();
            JSONObject jAlbum = data.getJSONObject(i);
            album.setId(jAlbum.getString("id"))
                    .setIsAlbum(jAlbum.getBoolean("is_album"))
                    .setTitle(jAlbum.getString("title"))
                    .setDesc(jAlbum.getString("description"))
                    .setCommentCount(jAlbum.getInt("comment_count"))
                    .setDownVote(jAlbum.getInt("downs"))
                    .setUpVote(jAlbum.getInt("ups"))
                    .setFavorite(jAlbum.getBoolean("favorite"))
                    .setPoints(jAlbum.getInt("points"))
                    .setTimeStamp(jAlbum.getLong("datetime"))
                    .setViews(jAlbum.getInt("views"))
                    .setVoted(jAlbum.getString("vote"));
            if (i % 2 == 0)
                album.setDrawable(ContextCompat.getDrawable(mContext, R.drawable._favorite));
            else
                album.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.capture));
            mListAlbum.add(album);
        }
        mAdapter.notifyDataSetChanged();
    }
}
