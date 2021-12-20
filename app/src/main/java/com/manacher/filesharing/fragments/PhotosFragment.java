/**
 * Author Aviraj Rajput
 *
 * **/


package com.manacher.filesharing.fragments;

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
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.manacher.filesharing.R;
import com.manacher.filesharing.adapters.BagListAdapter;
import com.manacher.filesharing.models.FileLength;
import com.manacher.filesharing.services.PathUtil;
import com.manacher.filesharing.utils.FileItem;
import com.manacher.filesharing.utils.FilesManager;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class PhotosFragment extends Fragment {

    private static int RESULT_LOAD_IMAGE = 89;
    private Button buttonLoadPicture;
    private View rootView;

    private Activity context;

    private ArrayList<String> fileNamesList;
    private ArrayList<FileLength> fileLengthList;
    private ArrayList<Uri> fileDataList;
    private ArrayList<FileItem> bagList;
    private RelativeLayout collectorPlace;

    private BagListAdapter adaptor;
    private FilesManager filesManager;

//    private FireAuthService fireAuthService;

    private String tag;

    public PhotosFragment(Activity context,
                          ArrayList<FileItem> bagList,
                          BagListAdapter adaptor,
                          ArrayList<String> fileNamesList,
                          ArrayList<FileLength> fileLengthList,
                          ArrayList<Uri> fileDataList,
                          RelativeLayout collectorPlace,
                          String tag) {
        this.context = context;
        this.bagList = bagList;
        this.adaptor = adaptor;

        this.fileNamesList = fileNamesList;
        this.fileLengthList = fileLengthList;
        this.fileDataList = fileDataList;
        this.collectorPlace = collectorPlace;
        this.tag = tag;

//        this.fireAuthService = new FireAuthService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        filesManager = new FilesManager(context);

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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
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

                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    assert uri != null;
                    Cursor cursor = context.getContentResolver().query(uri,
                            filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    String uriPath = PathUtil.getPath(context, uri);

                    assert uriPath != null;
                    File file1 =  new File(uriPath);
                    setUpList(file1.getName(), file1.length(), uri, file1);
                }
            }else{
                uri = resultData.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                assert uri != null;
                Cursor cursor = context.getContentResolver().query(uri,
                        filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                String uriPath = PathUtil.getPath(context, uri);

                assert uriPath != null;
                File file1 =  new File(uriPath);
                setUpList(file1.getName(), file1.length(), uri, file1);
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setUpList(String fileName, Long fileLength, Uri data, File file){

        if (tag.equals(getString(R.string.offline_tag))){

            if (!filesManager.isBagHasItem(bagList, fileName)){
                fileNamesList.add(0, fileName);
                fileLengthList.add(0, new FileLength(fileLength));
                fileDataList.add(0, data);

                if (fileName.toLowerCase().contains("jpeg") || fileName.toLowerCase().contains("jpg")){
                    bagList.add(0, new FileItem(fileName, context.getResources().getDrawable(R.drawable.jpg), String.valueOf(data), fileLength));
                    if (bagList.isEmpty()){
                        collectorPlace.setVisibility(View.GONE);
                    }else {
                        collectorPlace.setVisibility(View.VISIBLE);
                    }
                    adaptor.notifyDataSetChanged();


                }else if (fileName.toLowerCase().contains("png")){
                    bagList.add(0, new FileItem(fileName, context.getResources().getDrawable(R.drawable.png), String.valueOf(data), fileLength));
                    if (bagList.isEmpty()){
                        collectorPlace.setVisibility(View.GONE);
                    }else {
                        collectorPlace.setVisibility(View.VISIBLE);
                    }
                    adaptor.notifyDataSetChanged();

                }
                else{
                    bagList.add(0, new FileItem(fileName, context.getResources().getDrawable(R.drawable.file_icon2), String.valueOf(data), fileLength));
                    if (bagList.isEmpty()){
                        collectorPlace.setVisibility(View.GONE);
                    }else {
                        collectorPlace.setVisibility(View.VISIBLE);
                    }
                    adaptor.notifyDataSetChanged();
                }
            }


        } else if (tag.equals(getString(R.string.online_tag))){

//            if (!filesManager.isBagHasItem(bagList, fileName)){
//                fileNamesList.add(0, fileName);
//                fileLengthList.add(0, new FileLength(fileLength));
//                fileDataList.add(0, data);
//
//                if (fileName.toLowerCase().contains("jpeg") || fileName.toLowerCase().contains("jpg")){
//                    bagList.add(0, new FileItem(fileName,
//                            context.getResources().getDrawable(R.drawable.jpg ),
//                            file.getAbsolutePath(), fileLength,
//                            fireAuthService.getUserId()));
//
//                    if (bagList.isEmpty()){
//                        collectorPlace.setVisibility(View.GONE);
//                    }else {
//                        collectorPlace.setVisibility(View.VISIBLE);
//                    }
//                    adaptor.notifyDataSetChanged();
//
//
//                }else if (fileName.toLowerCase().contains("png")){
//                    bagList.add(0, new FileItem(fileName,
//                            context.getResources().getDrawable(R.drawable.png),
//                            file.getAbsolutePath(), fileLength,file.getAbsolutePath(), fireAuthService.getUserId()));
//                    if (bagList.isEmpty()){
//                        collectorPlace.setVisibility(View.GONE);
//                    }else {
//                        collectorPlace.setVisibility(View.VISIBLE);
//                    }
//                    adaptor.notifyDataSetChanged();
//
//                }
//                else{
//                    bagList.add(0, new FileItem(fileName,
//                            context.getResources().getDrawable(R.drawable.file_icon2),
//                            file.getAbsolutePath(), fileLength, file.getAbsolutePath(),
//                            fireAuthService.getUserId()));
//                    if (bagList.isEmpty()){
//                        collectorPlace.setVisibility(View.GONE);
//                    }else {
//                        collectorPlace.setVisibility(View.VISIBLE);
//                    }
//                    adaptor.notifyDataSetChanged();
//                }
//            }

        }
    }
}
