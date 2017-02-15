package com.pic.yourpics.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.pic.yourpics.R;
import com.pic.yourpics.adapter.ViewAlbumAdapter;
import com.pic.yourpics.model.Album;
import com.pic.yourpics.model.Image;

import java.util.ArrayList;

public class ViewAlbumActivity extends AppCompatActivity {

    private ToggleButton mFav;
    private ToggleButton mUpVote;
    private ToggleButton mDownVote;

    private ArrayList<Image> mList;
    private ViewAlbumAdapter mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_album);
        getData();

        mRecycler = (RecyclerView) findViewById(R.id.recycler_view);
        mFav = (ToggleButton) findViewById(R.id.fav);
        mUpVote = (ToggleButton) findViewById(R.id.up_vote);
        mDownVote = (ToggleButton) findViewById(R.id.down_vote);

        mLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLayoutManager);
        mAdapter = new ViewAlbumAdapter(this, mList);
        mRecycler.setAdapter(mAdapter);

        mFav.setChecked(false);
        mFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mFav.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.favorite));
                else
                    mFav.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_white));
            }
        });
    }

    private void getData() {
        String data = (String) getIntent().getSerializableExtra("album");
        Album album = new Gson().fromJson(data, Album.class);
        if (album.isAlbum())
            mList = album.getImageList();
        else {
            mList = new ArrayList<>();
            Image img = new Image();
            img.setAspectRatio(album.getAspectRatio());
            img.setWidth(album.getCoverWidth())
                    .setHeight(album.getCoverHeight())
                    .setDescription(album.getTitle())
                    .setLink(album.getLink());
            mList.add(img);
        }
    }
}
