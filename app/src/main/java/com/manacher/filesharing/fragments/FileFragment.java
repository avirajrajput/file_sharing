package com.manacher.filesharing.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


import com.manacher.filesharing.R;
import com.manacher.filesharing.activities.SendSelectActivity;
import com.manacher.filesharing.adapters.BagListAdapter;
import com.manacher.filesharing.models.FileLength;
import com.manacher.filesharing.services.FilesUtil;
import com.manacher.filesharing.services.PathUtil;
import com.manacher.filesharing.utils.FileItem;
import com.manacher.filesharing.utils.FilesManager;


import java.io.File;
import java.util.ArrayList;


public class FileFragment extends Fragment {
    private Button button ;
    private SendSelectActivity context;


    private BagListAdapter adapter2;

    private ArrayList<String> fileNamesList;
    private ArrayList<FileLength> fileLengthList;
    private ArrayList<Uri> fileDataList;
    private ArrayList<FileItem> bagList;
    private LinearLayout collectorPlace;

    final static int FILE_TRANSFER_CODE = 69;

    private FilesManager filesManager;


    public FileFragment(SendSelectActivity context,
                        ArrayList<FileItem> bagList,
                        BagListAdapter adapter2,
                        ArrayList<String> fileNamesList,
                        ArrayList<FileLength> fileLengthList,
                        ArrayList<Uri> fileDataList,
                        LinearLayout collectorPlace) {
        this.context = context;
        this.bagList = bagList;
        this.adapter2 = adapter2;

        this.fileNamesList = fileNamesList;
        this.fileLengthList = fileLengthList;
        this.fileDataList = fileDataList;
        this.collectorPlace = collectorPlace;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_file, container, false);

         this.initialized(rootView);

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, (android.os.Parcelable) null);
                startActivityForResult(intent, FILE_TRANSFER_CODE);
            }
        });

         return rootView;
    }

    private void initialized(View rootView){
        button = (Button)rootView.findViewById(R.id.select_button) ;
        filesManager = new FilesManager(context);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        Log.d("POM", "onActivityResult: ");

        if (requestCode == FILE_TRANSFER_CODE
                && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.

            ClipData clipData = resultData.getClipData();
            Uri uri;
            if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        uri = clipData.getItemAt(i).getUri();
                        String filePath = PathUtil.getPath(context.getApplicationContext(), clipData.getItemAt(i).getUri());

                        assert filePath != null;
                        String fileName = FilesUtil.getFileName(filePath);
                        Log.d("asd", "onActivityResult: " +fileName);
                        Log.d("asd", "onActivityResult: " +new File(filePath).length());
                        Log.d("asd", "onActivityResult: " +uri);

                        setUpList(fileName, new File(filePath).length(), uri);

                        Log.d("File_URI", clipData.getItemAt(i).getUri().toString());
                        Log.d("File_Path", fileName);
                    }

                }  else {
                        uri = resultData.getData();

                        // Perform operations on the document using its URI.
                        String filePath = PathUtil.getPath(context.getApplicationContext(), uri);

                        assert filePath != null;
                        String fileName = FilesUtil.getFileName(filePath);

                        setUpList(fileName, new File(filePath).length(), uri);

                    }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setUpList(String fileName, Long fileLength, Uri data){

        if (!filesManager.isBagHasItem(bagList, fileName)){
            fileNamesList.add(0,fileName);
            fileLengthList.add(0, new FileLength(fileLength));
            fileDataList.add(0, data);

            if (fileName.toLowerCase().contains("pdf")){
                bagList.add(0, new FileItem(fileName, context.getResources().getDrawable(R.drawable.pdf), "",fileLength));
                if (bagList.isEmpty()){
                    collectorPlace.setVisibility(View.GONE);
                }else {
                    collectorPlace.setVisibility(View.VISIBLE);
                }
                adapter2.notifyDataSetChanged();

            }else if (fileName.toLowerCase().contains("text") || fileName.toLowerCase().contains("txt")){
                bagList.add(0, new FileItem(fileName, context.getResources().getDrawable(R.drawable.txt), "",fileLength));
                if (bagList.isEmpty()){
                    collectorPlace.setVisibility(View.GONE);
                }else {
                    collectorPlace.setVisibility(View.VISIBLE);
                }
                adapter2.notifyDataSetChanged();

            }else if (fileName.toLowerCase().contains("mp3")){
                bagList.add(0, new FileItem(fileName, context.getResources().getDrawable(R.drawable.mp3), "",fileLength));
                if (bagList.isEmpty()){
                    collectorPlace.setVisibility(View.GONE);
                }else {
                    collectorPlace.setVisibility(View.VISIBLE);
                }
                adapter2.notifyDataSetChanged();

            }else if (fileName.toLowerCase().contains("jpeg") || fileName.toLowerCase().contains("jpg")){
                bagList.add(0, new FileItem(fileName, context.getResources().getDrawable(R.drawable.jpg), "",fileLength));
                if (bagList.isEmpty()){
                    collectorPlace.setVisibility(View.GONE);
                }else {
                    collectorPlace.setVisibility(View.VISIBLE);
                }
                adapter2.notifyDataSetChanged();

            }else if (fileName.toLowerCase().contains("png")){
                bagList.add(0, new FileItem(fileName, context.getResources().getDrawable(R.drawable.png), "",fileLength));
                if (bagList.isEmpty()){
                    collectorPlace.setVisibility(View.GONE);
                }else {
                    collectorPlace.setVisibility(View.VISIBLE);
                }
                adapter2.notifyDataSetChanged();

            }else if (fileName.toLowerCase().contains("mp4") || fileName.toLowerCase().contains("3gp")){
                bagList.add(0, new FileItem(fileName, context.getResources().getDrawable(R.drawable.video_file), "",fileLength));
                if (bagList.isEmpty()){
                    collectorPlace.setVisibility(View.GONE);
                }else {
                    collectorPlace.setVisibility(View.VISIBLE);
                }
                adapter2.notifyDataSetChanged();

            }
            else{
                bagList.add(0, new FileItem(fileName, context.getResources().getDrawable(R.drawable.file_icon2), "",fileLength));
                if (bagList.isEmpty()){
                    collectorPlace.setVisibility(View.GONE);
                }else {
                    collectorPlace.setVisibility(View.VISIBLE);
                }
                adapter2.notifyDataSetChanged();
            }
        }
    }
}
