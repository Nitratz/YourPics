package com.pic.yourpics.model;

import java.util.ArrayList;

public class Album {

    private String mId;
    private boolean isAlbum;

    private long mTimeStamp;
    private String mTitle;
    private String mDesc;
    private String mLink;
    private int mUpVote;
    private int mDownVote;
    private int mPoints;
    private int mViews;
    private int mCommentCount;
    private boolean isFavorite;
    private String mVoted;
    private ArrayList<Image> mImageList;

    public String getId() {
        return mId;
    }

    public Album setId(String mId) {
        this.mId = mId;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }

    public Album setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public String getDesc() {
        return mDesc;
    }

    public Album setDesc(String mDesc) {
        this.mDesc = mDesc;
        return this;
    }

    public int getUpVote() {
        return mUpVote;
    }

    public Album setUpVote(int mUpVote) {
        this.mUpVote = mUpVote;
        return this;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String mLink) {
        this.mLink = mLink;
    }

    public int getDownVote() {
        return mDownVote;
    }

    public Album setDownVote(int mDownVote) {
        this.mDownVote = mDownVote;
        return this;
    }

    public int getPoints() {
        return mPoints;
    }

    public Album setPoints(int mPoints) {
        this.mPoints = mPoints;
        return this;
    }

    public int getViews() {
        return mViews;
    }

    public Album setViews(int mViews) {
        this.mViews = mViews;
        return this;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public Album setCommentCount(int mCommentCount) {
        this.mCommentCount = mCommentCount;
        return this;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public Album setFavorite(boolean favorite) {
        isFavorite = favorite;
        return this;
    }

    public String getVoted() {
        return mVoted;
    }

    public Album setVoted(String mVoted) {
        this.mVoted = mVoted;
        return this;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public Album setTimeStamp(long mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
        return this;
    }

    public boolean isAlbum() {
        return isAlbum;
    }

    public Album setIsAlbum(boolean album) {
        isAlbum = album;
        return this;
    }

    public ArrayList<Image> getImageList() {
        return mImageList;
    }

    public Album setImageList(ArrayList<Image> mImageList) {
        this.mImageList = mImageList;
        return this;
    }
}
