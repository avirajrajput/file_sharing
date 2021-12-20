package com.manacher.filesharing.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.manacher.filesharing.R;
import com.manacher.filesharing.adapters.StoredDataListAdapter;
import com.manacher.filesharing.utils.FilesManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


public class OfflineFilesFragment extends Fragment {
    private Activity context;
    private FilesManager filesManager;
    private RecyclerView listView;
//    String[] filesName;
    private ArrayList<File> fileList;
    private StoredDataListAdapter adapter;

    private TextView emptyStatus;

    @SuppressLint("SdCardPath")
    private final String folder = Environment.getExternalStorageDirectory() +"/FileSharing" +"/Offline";

    public OfflineFilesFragment(Activity context) {
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.offline_files_fragment, container, false);

        filesManager = new FilesManager(context);
        emptyStatus = rootView.findViewById(R.id.empty_status);
        listView = rootView.findViewById(R.id.list_view_offline);
        getFilesList(new File(folder));

        setEmptyStatus();
        return rootView;
    }

    private void getFilesList(File file){
        if (filesManager.isFolder(file)){
            if (filesManager.isFileExists(file)){

                fileList = new ArrayList<>(Arrays.asList(filesManager.getFileList(file)));

                adapter = new StoredDataListAdapter(fileList, context, FilesManager.TAG_OPEN_FILE);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                listView.setLayoutManager(layoutManager);

                listView.setAdapter(adapter);
            }
        }
    }

    private void setEmptyStatus(){
        if (fileList != null){
            if (fileList.isEmpty()){
                emptyStatus.setVisibility(View.VISIBLE);
            }else{
                emptyStatus.setVisibility(View.GONE);
            }
        }
    }

}
