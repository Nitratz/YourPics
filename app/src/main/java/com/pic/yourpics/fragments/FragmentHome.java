package com.pic.yourpics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pic.yourpics.R;
import com.pic.yourpics.adapter.HomeAdapter;
import com.pic.yourpics.model.Post;

import java.util.ArrayList;

public class FragmentHome extends Fragment {

    private Context mContext;

    private ArrayList<Post> mListPost;
    private RecyclerView mRecycler;

    private StaggeredGridLayoutManager mGridLayout;
    private HomeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity();
        mListPost = new ArrayList<>();

        mRecycler = (RecyclerView) v.findViewById(R.id.recycler_home);
        mGridLayout = new StaggeredGridLayoutManager(2, 1);
        mRecycler.setLayoutManager(mGridLayout);
        mAdapter = new HomeAdapter(mContext, mListPost);
        mRecycler.setAdapter(mAdapter);
        fillList();

        return v;
    }

    private void fillList() {
        for (int i = 0; i < 6; i++) {
            Post post = new Post();
            post.setDesc("LOLLOOOLL");
            post.setTitle("LOOOLL");
            if (i % 2 == 0)
                post.setDrawable(ContextCompat.getDrawable(mContext, R.drawable._favorite));
            else
                post.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.capture));
            mListPost.add(post);
        }
    }
}
