package com.pic.yourpics.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.pic.yourpics.R;
import com.pic.yourpics.activity.MainActivity;
import com.pic.yourpics.request.callback.OnRequestListener;
import com.pic.yourpics.request.parser.ImgurParser;
import com.pic.yourpics.service.Constants;

import static com.pic.yourpics.service.Constants.REQUEST_TYPE.*;

public class FragmentUpload extends Fragment implements OnRequestListener, View.OnClickListener {

    private View mView;
    private Bitmap mImage;

    private ImageView mPreview;
    private EditText mTitle;
    private EditText mDesc;
    private Button mSelect;
    private Button mUpload;
    private AlertDialog mAlertDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView != null)
            return mView;
        mView = inflater.inflate(R.layout.fragment_upload, container, false);

        mPreview = (ImageView) mView.findViewById(R.id.preview);
        mSelect = (Button) mView.findViewById(R.id.select);
        mUpload = (Button) mView.findViewById(R.id.upload);

        mUpload.setOnClickListener(this);
        mSelect.setOnClickListener(this);

        return mView;
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((MainActivity) getActivity()).startActivityForResult(galleryIntent, 21);
    }

    public void resultGallery(Bitmap image) {
        if (image.getWidth() > 4096 || image.getHeight() > 4096)
            alertDialog(getString(R.string.alert_error_title), getString(R.string.alert_image_large));
        else {
            mPreview.setBackground(new BitmapDrawable(getResources(), image));
            mImage = image;
        }
    }

    private void alertDialog(String title, String msg) {
        if (mAlertDialog != null) {
            mAlertDialog.setTitle(title);
            mAlertDialog.setMessage(msg);
            mAlertDialog.show();
        }
        mAlertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload:
                if (mImage != null)
                    new ImgurParser(getActivity(), this).postImgurImage(mImage);
                break;
            case R.id.select:
                openGallery();
                break;
        }
    }


    @Override
    public void onSuccess(String response, Constants.REQUEST_TYPE type) {
        switch (type) {
            case UPLOAD:
                alertDialog(getString(R.string.alert_success), getString(R.string.image_uploaded));
                break;
        }
    }

    @Override
    public void onError(String error) {
        alertDialog(getString(R.string.alert_error_title), getString(R.string.alert_error_desc));
    }
}
