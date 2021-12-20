package com.manacher.filesharing.sheets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.manacher.filesharing.R;
import com.manacher.filesharing.activities.SplashActivity;
import com.manacher.filesharing.utils.FileItem;

import java.io.File;
import static android.app.Activity.RESULT_OK;

public class PhotosSheet extends BottomSheetDialogFragment {

    private static int RESULT_LOAD_IMAGE = 89;
    private Button buttonLoadPicture;
    private View rootView;

    private Activity activity;

    private FileItem fileItem;

    private RelativeLayout fileView;
    private ImageView fileIcon;
    private TextView fileSize;
    private TextView fileNameTextView;
//    private FireAuthService fireAuthService;

    public PhotosSheet(Activity context, FileItem fileItem, RelativeLayout fileView, ImageView fileIcon, TextView fileSize, TextView fileName) {
        this.activity = context;
        this.fileItem = fileItem;
        this.fileView = fileView;
        this.fileIcon = fileIcon;
        this.fileSize = fileSize;
        this.fileNameTextView = fileName;
//        this.fireAuthService = new FireAuthService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.photos_sheet, container, false);
        SplashActivity.setPhotosSheet(this);
        buttonLoadPicture = rootView.findViewById(R.id.buttonLoadPicture);
        buttonLoadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhotos();
            }
        });

        return rootView;
    }

    private void selectPhotos(){

        Intent intent = new Intent();
        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && resultData != null) {

            ClipData clipData = resultData.getClipData();
            Uri uri;

            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    uri = clipData.getItemAt(i).getUri();
                    if (uri != null){
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };

                        Cursor cursor = activity.getContentResolver().query(uri,
                                filePathColumn, null, null, null);
                        if (cursor != null){
                            cursor.moveToFirst();
                        }

//                        String uriPath = PathUtil.getPath(activity, uri);

//                        if(uriPath != null) {
//                            File file1 =  new File(uriPath);
//                            setUpList(file1.getName(), file1.length(), uri, file1);
//                        }
                    }

                }
            }else{
                uri = resultData.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                if (uri != null){
                    Cursor cursor = activity.getContentResolver().query(uri,
                            filePathColumn, null, null, null);
                    if (cursor != null){
                        cursor.moveToFirst();
                    }

//                    String uriPath = PathUtil.getPath(activity, uri);
//
//                    if (uriPath != null){
//                        File file1 =  new File(uriPath);
//                        setUpList(file1.getName(), file1.length(), uri, file1);
//                    }
                }
            }
        }
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "DefaultLocale"})
    private void setUpList(String fileName, Long fileLength, Uri data, File file){

                if (fileName.toLowerCase().contains("jpeg") || fileName.toLowerCase().contains("jpg")){

                    fileItem.setIcon(activity.getResources().getDrawable(R.drawable.jpg));
                    fileItem.setFileName(fileName);
                    fileItem.setFilePath(String.valueOf(data));
                    fileItem.setFileSize(fileLength);
                    fileItem.setProgress(0);
//                    fileItem.setSenderId(fireAuthService.getUserId());

                    fileItem.setAbsolutePath(file.getAbsolutePath());

                    fileNameTextView.setText(fileName);
                    fileIcon.setImageDrawable(activity.getResources().getDrawable(R.drawable.jpg));

                    fileSize.setText(String.format("%.2f", (fileLength) * (0.00_00_00_95) ));

                    fileView.setVisibility(View.VISIBLE);
                    SplashActivity.getPhotosSheet().dismiss();


                }else if (fileName.toLowerCase().contains("png")){
                    fileItem.setIcon(activity.getResources().getDrawable(R.drawable.png));
                    fileItem.setFileName(fileName);
                    fileItem.setFilePath(String.valueOf(data));
                    fileItem.setFileSize(fileLength);
                    fileItem.setProgress(0);
//                    fileItem.setSenderId(fireAuthService.getUserId());

                    fileItem.setAbsolutePath(file.getAbsolutePath());

                    fileNameTextView.setText(fileName);

                    fileIcon.setImageDrawable(activity.getResources().getDrawable(R.drawable.png));

                    fileSize.setText(String.format("%.2f", (fileLength) * (0.00_00_00_95) ));

                    fileView.setVisibility(View.VISIBLE);
                    SplashActivity.getPhotosSheet().dismiss();

                }
                else{

                    fileItem.setIcon(activity.getResources().getDrawable(R.drawable.file_icon2));
                    fileItem.setFileName(fileName);
                    fileItem.setFilePath(String.valueOf(data));
                    fileItem.setFileSize(fileLength);
                    fileItem.setProgress(0);
//                    fileItem.setSenderId(fireAuthService.getUserId());

                    fileItem.setAbsolutePath(file.getAbsolutePath());

                    fileNameTextView.setText(fileName);

                    fileIcon.setImageDrawable(activity.getResources().getDrawable(R.drawable.file_icon2));

                    fileSize.setText(String.format("%.2f", (fileLength) * (0.00_00_00_95) ));

                    fileView.setVisibility(View.VISIBLE);
                    SplashActivity.getPhotosSheet().dismiss();

                }

    }
}
