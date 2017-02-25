package com.pic.yourpics.request.parser;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.pic.yourpics.activity.callback.OnNoTokenFound;
import com.pic.yourpics.model.Album;
import com.pic.yourpics.model.Image;
import com.pic.yourpics.request.RequestManager;
import com.pic.yourpics.request.callback.OnRequestListener;
import com.pic.yourpics.service.AService;
import com.pic.yourpics.service.Constants;
import com.pic.yourpics.service.ServiceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import com.pic.yourpics.service.Constants.REQUEST_TYPE;

public class ImgurParser {

    private OnRequestListener mListener;
    private Context mContext;

    public ImgurParser(Context context, OnRequestListener listener) {
        mListener = listener;
        mContext = context;
    }

    public void postImgurImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        Log.d("mybyte", image.getByteCount() + " wow");
        if (image.getByteCount() > 10000000)
            return;
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        AService imgur = ServiceManager.getInstance().getServiceByName("Imgur");
        if (imgur.getToken() == null) {
            ((OnNoTokenFound) mContext).onFailedToLoadHome();
            return;
        }

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", encodedImage)
                .addFormDataPart("type", "base64")
                .build();

        Request request = new Request.Builder()
                .url(Constants.IMGUR_BASE + "/image")
                .addHeader("Authorization", "Bearer " + imgur.getToken())
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();
        RequestManager.getInstance().newRequest(request, mListener, REQUEST_TYPE.UPLOAD);
    }

    public void requestImgurGallery(int page) {
        AService imgur = ServiceManager.getInstance().getServiceByName("Imgur");
        if (imgur.getToken() == null) {
            ((OnNoTokenFound) mContext).onFailedToLoadHome();
            return;
        }
        Request request = new Request.Builder()
                .url(Constants.IMGUR_GALLERY + "hot/viral/" + page + ".json")
                .addHeader("Authorization", "Bearer " + imgur.getToken()).build();
        RequestManager.getInstance().newRequest(request, mListener, REQUEST_TYPE.GALLERY);
    }

    private void requestImgurAlbum(String id) {
        AService imgur = ServiceManager.getInstance().getServiceByName("Imgur");

        Request request = new Request.Builder()
                .url(Constants.IMGUR_ALBUM + id)
                .addHeader("Authorization", "Bearer " + imgur.getToken()).build();
        RequestManager.getInstance().newRequest(request, mListener, REQUEST_TYPE.ALBUM);
    }

    public ArrayList<Album> parserGallery(JSONObject object) throws JSONException {
        JSONArray data = object.getJSONArray("data");
        ArrayList<Album> listAlbum = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            Album album = new Album();
            JSONObject jAlbum = data.getJSONObject(i);
            album.setId(jAlbum.getString("id"))
                    .setIsAlbum(jAlbum.getBoolean("is_album"))
                    .setTitle(jAlbum.getString("title"))
                    .setDesc(jAlbum.getString("description"))
                    .setCommentCount(jAlbum.getInt("comment_count"))
                    .setDownVote(jAlbum.getInt("downs"))
                    .setUpVote(jAlbum.getInt("ups"))
                    .setFavorite(jAlbum.getBoolean("favorite"))
                    .setPoints(jAlbum.getInt("points"))
                    .setTimeStamp(jAlbum.getLong("datetime"))
                    .setViews(jAlbum.getInt("views"))
                    .setVoted(jAlbum.getString("vote"));
            if (album.isAlbum()) {
                album.setCoverWidth(jAlbum.getInt("cover_width"))
                        .setCoverHeight(jAlbum.getInt("cover_height"));
                requestImgurAlbum(album.getId());
            } else {
                album.setCoverWidth(jAlbum.getInt("width"))
                        .setCoverHeight(jAlbum.getInt("height"))
                        .setLink(jAlbum.getString("link"));
            }
            album.setAspectRatio();
            listAlbum.add(album);
        }
        return listAlbum;
    }

    public void parserAlbumImages(ArrayList<Album> listAlbum, JSONObject object) throws JSONException {
        JSONObject data = object.getJSONObject("data");
        JSONArray images = data.getJSONArray("images");
        ArrayList<Image> imageList = new ArrayList<>();

        String id = data.getString("id");
        for (Album album : listAlbum) {
            if (album.getId().equals(id)) {
                for (int i = 0; i < images.length(); i++) {
                    Image image = new Image();
                    JSONObject img = images.getJSONObject(i);
                    image.setTitle(img.getString("title"))
                            .setWidth(img.getInt("width"))
                            .setHeight(img.getInt("height"))
                            .setDescription(img.getString("description"))
                            .setId(img.getString("id"))
                            .setLink(img.getString("link"))
                            .setPoints(img.getString("points"))
                            .setDownVote(img.getString("downs"))
                            .setUpVote(img.getString("ups"))
                            .setType(img.getString("type"))
                            .setCommentCount(img.getString("comment_count"))
                            .setAspectRatio();
                    imageList.add(image);
                }
                album.setImageList(imageList);
                break;
            }
        }
    }
}
