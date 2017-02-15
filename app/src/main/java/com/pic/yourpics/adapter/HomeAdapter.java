package com.pic.yourpics.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pic.yourpics.R;
import com.pic.yourpics.activity.ViewAlbumActivity;
import com.pic.yourpics.model.Album;
import com.pic.yourpics.model.Image;

import java.util.ArrayList;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements View.OnClickListener {

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
        holder.mImage.setImageDrawable(null);
        holder.mImage.setAspectRatio(1.0f);

        if (album.isAlbum() && album.getImageList() != null) {
            Image cover = album.getImageList().get(0);
            holder.mImage.setAspectRatio(cover.getAspectRatio());
            Glide.with(mContext).load(cover.getLink())
                    .diskCacheStrategy(SOURCE)
                    .placeholder(new ColorDrawable(ContextCompat.getColor(mContext, R.color.colorAccent)))
                    .into(holder.mImage);
        } else {
            holder.mImage.setAspectRatio(album.getAspectRatio());
            Glide.with(mContext).load(album.getLink())
                    .diskCacheStrategy(SOURCE)
                    .placeholder(new ColorDrawable(ContextCompat.getColor(mContext, R.color.colorAccent)))
                    .into(holder.mImage);
        }
        holder.mTitle.setText(album.getTitle());
        holder.mCard.setTag(position);
        holder.mCard.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mListAlbum.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCard;
        private ThumbnailImageView mImage;
        private TextView mTitle;

        ViewHolder(View v) {
            super(v);
            mCard = (CardView) v.findViewById(R.id.card_view);
            mImage = (ThumbnailImageView) v.findViewById(R.id.image);
            mTitle = (TextView) v.findViewById(R.id.title);
        }
    }

    @Override
    public void onClick(View v) {
        int pos = (Integer) v.getTag();
        Gson gson = new Gson();
        String data;
        Intent intent = new Intent(mContext, ViewAlbumActivity.class);
        Album album = mListAlbum.get(pos);
        data = gson.toJson(album);
        intent.putExtra("album", data);
        mContext.startActivity(intent);
    }
}