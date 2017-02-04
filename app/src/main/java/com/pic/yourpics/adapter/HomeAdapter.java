package com.pic.yourpics.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pic.yourpics.R;
import com.pic.yourpics.model.Album;
import com.pic.yourpics.model.Image;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Album> mListAlbum;

    public HomeAdapter(Context context, ArrayList<Album> list) {
        mContext = context;
        mListAlbum = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Album album = mListAlbum.get(position);
        if (album.isAlbum() && album.getImageList() != null) {
            Image cover = album.getImageList().get(0);
            Glide.with(mContext).load(cover.getLink())
                    .placeholder(R.drawable.capture)
                    .override(cover.getWidth(), cover.getHeight())
                    .into(holder.mImage);
        } else
            Glide.with(mContext).load(album.getLink())
                    .placeholder(R.drawable.capture)
                    .into(holder.mImage);
        holder.mTitle.setText(album.getTitle());
    }

    @Override
    public int getItemCount() {
        return mListAlbum.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCard;
        private ImageView mImage;
        private TextView mTitle;

        ViewHolder(View v) {
            super(v);
            mImage = (ImageView) v.findViewById(R.id.image);
            mTitle = (TextView) v.findViewById(R.id.title);
        }
    }
}