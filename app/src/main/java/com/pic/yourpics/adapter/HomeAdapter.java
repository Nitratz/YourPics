package com.pic.yourpics.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pic.yourpics.R;
import com.pic.yourpics.model.Album;

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

        holder.mImage.setBackground(album.getDrawable());
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