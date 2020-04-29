package com.artemissoftware.afroditedating.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.artemissoftware.afroditedating.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;



public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    //constant
    private static final int CAMERA_REQUEST_CODE = 5090;
    private static final int NEW_PHOTO_REQUEST = 3567;

    //widgets

    //vars
    private Uri mOutputUri;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        Log.d(TAG, "onCreateView: started.");

        //open the camera to take a picture
        Button btnLaunchCamera = (Button) view.findViewById(R.id.btnLaunchCamera);
        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to launch camera.");
                openCamera();
            }
        });


        return view;
    }

    private void openCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mOutputUri = FileProvider.getUriForFile(
                getActivity(),
                "com.artemissoftware.afroditedating.provider",
                getOutputMediaFile());


        //mOutputUri = Uri.fromFile(getOutputMediaFile());
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }


    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AfroditeDating");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE){
            Log.d(TAG, "onActivityResult: done taking a photo.");
            Log.d(TAG, "onActivityResult: output uri: " + mOutputUri);
            if(data != null){
                getActivity().setResult(NEW_PHOTO_REQUEST,
                        data.putExtra(getString(R.string.intent_new_camera_photo), mOutputUri.toString()));
                getActivity().finish();
            }
            else{

                Intent photo = new Intent();
                photo.putExtra(getString(R.string.intent_new_camera_photo), mOutputUri.toString());
                getActivity().setResult(NEW_PHOTO_REQUEST, photo);
                getActivity().finish();
            }
        }
    }
}

































