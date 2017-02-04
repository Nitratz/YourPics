package com.pic.yourpics.request.parser;

import android.content.Context;
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

import java.util.ArrayList;

import okhttp3.Request;

import static com.pic.yourpics.service.Constants.REQUEST_TYPE.ALBUM;
import static com.pic.yourpics.service.Constants.REQUEST_TYPE.GALLERY;

public class ImgurParser {

    private OnRequestListener mListener;
    private Context mContext;

    public ImgurParser(Context context, OnRequestListener listener) {
        mListener = listener;
        mContext = context;
    }

    public void requestImgurGallery() {
        AService imgur = ServiceManager.getInstance().getServiceByName("Imgur");
        if (imgur.getToken() == null) {
            ((OnNoTokenFound) mContext).onFailedToLoadHome();
            return;
        }
        Request request = new Request.Builder()
                .url(Constants.IMGUR_GALLERY + "hot/viral/0.json")
                .addHeader("Authorization", "Bearer " + imgur.getToken()).build();
        RequestManager.getInstance().newRequest(request, mListener, GALLERY);
    }

    private void requestImgurAlbum(String id) {
        AService imgur = ServiceManager.getInstance().getServiceByName("Imgur");

        Request request = new Request.Builder()
                .url(Constants.IMGUR_ALBUM + id)
                .addHeader("Authorization", "Bearer " + imgur.getToken()).build();
        RequestManager.getInstance().newRequest(request, mListener, ALBUM);
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
                requestImgurAlbum(album.getId());
            } else {
                album.setLink(jAlbum.getString("link"));
            }
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
                            .setCommentCount(img.getString("comment_count"));
                    imageList.add(image);
                }
                album.setImageList(imageList);
                break;
            }
        }
    }
}
