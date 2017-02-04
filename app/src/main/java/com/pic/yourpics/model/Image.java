package com.pic.yourpics.model;

public class Image {

    private String mId;

    private String mLink;
    private int mWidth;
    private int mHeight;
    private String mDescription;
    private String mTitle;
    private String mType;
    private String mCommentCount;
    private String mUpVote;
    private String mDownVote;
    private String mPoints;

    public String getId() {
        return mId;
    }

    public Image setId(String mId) {
        this.mId = mId;
        return this;
    }

    public String getLink() {
        return mLink;
    }

    public Image setLink(String mLink) {
        this.mLink = mLink;
        return this;
    }

    public String getDescription() {
        return mDescription;
    }

    public Image setDescription(String mDescription) {
        this.mDescription = mDescription;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }

    public Image setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public String getType() {
        return mType;
    }

    public Image setType(String mType) {
        this.mType = mType;
        return this;
    }

    public String getCommentCount() {
        return mCommentCount;
    }

    public Image setCommentCount(String mCommentCount) {
        this.mCommentCount = mCommentCount;
        return this;
    }

    public String getUpVote() {
        return mUpVote;
    }

    public Image setUpVote(String mUpVote) {
        this.mUpVote = mUpVote;
        return this;
    }

    public String getDownVote() {
        return mDownVote;
    }

    public Image setDownVote(String mDownVote) {
        this.mDownVote = mDownVote;
        return this;
    }

    public String getPoStrings() {
        return mPoints;
    }

    public Image setPoints(String mPoints) {
        this.mPoints = mPoints;
        return this;
    }

    public int getWidth() {
        return mWidth;
    }

    public Image setWidth(int mWidth) {
        this.mWidth = mWidth;
        return this;
    }

    public int getHeight() {
        return mHeight;
    }

    public Image setHeight(int mHeight) {
        this.mHeight = mHeight;
        return this;
    }
}
