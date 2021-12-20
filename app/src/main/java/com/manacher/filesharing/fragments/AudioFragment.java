package com.manacher.filesharing.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.manacher.filesharing.R;
import com.manacher.filesharing.adapters.BagListAdapter;
import com.manacher.filesharing.adapters.StoredDataListAdapter;
import com.manacher.filesharing.models.FileLength;
import com.manacher.filesharing.utils.FileItem;
import com.manacher.filesharing.utils.FilesManager;

import java.io.File;
import java.util.ArrayList;


public class AudioFragment extends Fragment {
    private Activity context;

    private ArrayList<String> fileNamesList;
    private ArrayList<FileLength> fileLengthList;
    private ArrayList<Uri> fileDataList;
    private ArrayList<FileItem> bagList;

    private BagListAdapter adapter;
    private RelativeLayout collectorPlace;

    private StoredDataListAdapter adapter2;

    private ArrayList<File> fileList;

    private String tag;

    public AudioFragment(Activity context,
                         ArrayList<FileItem> bagList,
                         BagListAdapter adapter2,
                         ArrayList<String> fileNamesList,
                         ArrayList<FileLength> fileLengthList,
                         ArrayList<Uri> fileDataList,
                         RelativeLayout collectorPlace,
                         String tag) {

        this.context = context;
        this.bagList = bagList;
        this.adapter = adapter2;

        this.fileNamesList = fileNamesList;
        this.fileLengthList = fileLengthList;
        this.fileDataList = fileDataList;
        this.collectorPlace = collectorPlace;

        this.tag = tag;


    }

//    public AudioFragment(Activity context,
//                         ArrayList<FileItem> bagList,
//                         BagListAdapter adapter2,
//                         RelativeLayout collectorPlace,
//                         String tag) {
//
//        this.context = context;
//        this.bagList = bagList;
//        this.adapter = adapter2;
//
//        this.collectorPlace = collectorPlace;
//        this.tag = tag;
//    }

    @SuppressLint("SdCardPath")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_audio, container, false);
        FilesManager filesManager = new FilesManager(context);
        EditText contactSearchBar = rootView.findViewById(R.id.File_ex_search_bar);

        fileList = new ArrayList<>();
        this.getMusic();

        RecyclerView listView = rootView.findViewById(R.id.listView);

            adapter2 = new StoredDataListAdapter(fileList,
                    context,
                    FilesManager.TAG_SELECT_FILE,
                    bagList,
                    fileNamesList,
                    fileLengthList,
                    fileDataList,
                    adapter,
                    collectorPlace,
                    tag);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            listView.setLayoutManager(layoutManager);
            listView.setAdapter(adapter2);

        contactSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

         return rootView;
    }

    private void getMusic(){
        ContentResolver contentResolver = context.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        @SuppressLint("Recycle") Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()){
            int songData = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);

            do{

                String currentData = songCursor.getString(songData);
                fileList.add(new File(currentData));

            }while(songCursor.moveToNext());
        }
    }

    private void filter(String text){
        ArrayList<File> filterList = new ArrayList<>();

        for (File item : fileList){
            if (item.getName().toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);
            }
        }
       adapter2.filterList(filterList);
    }
}

