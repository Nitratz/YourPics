package com.pic.yourpics.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

class ThumbnailImageView extends ImageView {
    private float mAspectRatio;

    public ThumbnailImageView(Context context) {
        super(context);
    }

    public ThumbnailImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThumbnailImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAspectRatio(float aspectRatio) {
        mAspectRatio = aspectRatio;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int)((float)width / mAspectRatio);
        setMeasuredDimension(width, height);
    }
}
