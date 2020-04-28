package com.artemissoftware.afroditedating.settings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.artemissoftware.afroditedating.R;


public class ChoosePhotoActivity extends AppCompatActivity {

    private static final String TAG = "ChoosePhotoActivity";
    private static final int GALLERY_FRAGMENT = 0;
    private static final int PHOTO_FRAGMENT = 1;

    //fragments
    private GalleryFragment mGalleryFragment;
    private PhotoFragment mPhotoFragment;

    //widgets


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo);


    }



}










