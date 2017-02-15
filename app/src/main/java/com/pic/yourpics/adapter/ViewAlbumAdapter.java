package com.pic.yourpics.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pic.yourpics.R;
import com.pic.yourpics.model.Image;

import java.util.ArrayList;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;

public class ViewAlbumAdapter extends RecyclerView.Adapter<ViewAlbumAdapter.ViewHolder> {

    private ArrayList<Image> mList;
    private Context mContext;

    public ViewAlbumAdapter(Context context, ArrayList<Image> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public ViewAlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_album, parent, false);
        return new ViewAlbumAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewAlbumAdapter.ViewHolder holder, int position) {
        Image img = mList.get(position);
        holder.mImage.setImageDrawable(null);

        holder.mImage.setAspectRatio(img.getAspectRatio());
        Glide.with(mContext).load(img.getLink())
                .diskCacheStrategy(SOURCE)
                .placeholder(new ColorDrawable(ContextCompat.getColor(mContext, R.color.colorAccent)))
                .into(holder.mImage);
        if (img.getDescription().equals("null"))
            holder.mDesc.setVisibility(View.GONE);
        else
            holder.mDesc.setText(img.getDescription());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCard;
        private ThumbnailImageView mImage;
        private TextView mDesc;

        ViewHolder(View v) {
            super(v);
            mCard = (CardView) v.findViewById(R.id.card_view);
            mImage = (ThumbnailImageView) v.findViewById(R.id.image);
            mDesc = (TextView) v.findViewById(R.id.desc);
        }
    }
}
