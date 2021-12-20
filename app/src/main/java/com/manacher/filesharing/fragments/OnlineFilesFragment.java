package com.manacher.filesharing.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.manacher.filesharing.R;
import com.manacher.filesharing.adapters.StoredDataListAdapter;
import com.manacher.filesharing.utils.FilesManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class OnlineFilesFragment extends Fragment {
    private Activity context;
    private FilesManager filesManager;
    private RecyclerView listView;

    private ArrayList<File> fileList;
    private StoredDataListAdapter adapter;

    private TextView emptyStatus;

    @SuppressLint("SdCardPath")
    private final String folder = Environment.getExternalStorageDirectory() +"/SendKite" +"/Online";

    public OnlineFilesFragment( Activity context) {
        this.context = context;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.online_files_fragment, container, false);
        emptyStatus = rootView.findViewById(R.id.empty_status);
        filesManager = new FilesManager(context);
        listView = rootView.findViewById(R.id.list_view_online);
//        listener();
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
