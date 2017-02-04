package com.pic.yourpics.service;

public class Constants {

    public static final String IMGUR_API_KEY = "eb007454f35153b";
    public static final String IMGUR_API_SECRET = "b00197530b399a5a7dce6a16dcc7d98668c06a0c";

    public static final String FLICKR_API_KEY = "722996a4754d4aa80ce5c4956d8e35ac";
    public static final String FLICKR_API_SECRET = "e835a0312d25855b";

    public static enum REQUEST_TYPE {
        GALLERY,
        ALBUM,
    }

    public static final String IMGUR_GALLERY = "https://api.imgur.com/3/gallery/";
    public static final String IMGUR_ALBUM = "\thttps://api.imgur.com/3/gallery/album/";
}
