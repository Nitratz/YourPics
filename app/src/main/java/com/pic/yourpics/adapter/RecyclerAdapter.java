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
import com.pic.yourpics.model.Post;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Post> mListPost;

    public RecyclerAdapter(Context context, ArrayList<Post> list) {
        mContext = context;
        mListPost = list;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = mListPost.get(position);

        holder.mImage.setBackground(post.getDrawable());
        holder.mDesc.setText(post.getDesc());
    }

    @Override
    public int getItemCount() {
        return mListPost.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCard;
        private ImageView mImage;
        private TextView mDesc;

        ViewHolder(View v) {
            super(v);
            mImage = (ImageView) v.findViewById(R.id.image);
            mDesc = (TextView) v.findViewById(R.id.desc);
        }
    }
}