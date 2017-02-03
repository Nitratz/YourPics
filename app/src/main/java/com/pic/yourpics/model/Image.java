package com.pic.yourpics.model;

public class Image {

    private String mId;

    private String mLink;
    private String mDescription;
    private String mTitle;
    private String mType;
    private String mCommentCount;
    private String mUpVote;
    private String mDownVote;
    private String mPoints;

    public String getmId() {
        return mId;
    }

    public Image setmId(String mId) {
        this.mId = mId;
        return this;
    }

    public String getmLink() {
        return mLink;
    }

    public Image setmLink(String mLink) {
        this.mLink = mLink;
        return this;
    }

    public String getmDescription() {
        return mDescription;
    }

    public Image setmDescription(String mDescription) {
        this.mDescription = mDescription;
        return this;
    }

    public String getmTitle() {
        return mTitle;
    }

    public Image setmTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public String getmType() {
        return mType;
    }

    public Image setmType(String mType) {
        this.mType = mType;
        return this;
    }

    public String getmCommentCount() {
        return mCommentCount;
    }

    public Image setmCommentCount(String mCommentCount) {
        this.mCommentCount = mCommentCount;
        return this;
    }

    public String getmUpVote() {
        return mUpVote;
    }

    public Image setmUpVote(String mUpVote) {
        this.mUpVote = mUpVote;
        return this;
    }

    public String getmDownVote() {
        return mDownVote;
    }

    public Image setmDownVote(String mDownVote) {
        this.mDownVote = mDownVote;
        return this;
    }

    public String getmPoints() {
        return mPoints;
    }

    public Image setmPoints(String mPoints) {
        this.mPoints = mPoints;
        return this;
    }
}
