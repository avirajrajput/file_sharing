package com.manacher.filesharing.sheets;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.manacher.filesharing.R;
import com.manacher.filesharing.activities.SplashActivity;
import com.manacher.filesharing.adapters.StoredDataListAdapter;
import com.manacher.filesharing.utils.FileItem;
import com.manacher.filesharing.utils.FilesManager;

import java.io.File;
import java.util.ArrayList;

public class AudioSheet extends BottomSheetDialogFragment {
    private Activity activity;
    private AudioSheet fContext = this;

    private StoredDataListAdapter adapter2;

    private ArrayList<File> fileList;

    private FileItem fileItem;
    private RelativeLayout fileView;
    private ImageView fileIcon;
    private TextView fileSize;
    private TextView fileName;

    public AudioSheet(Activity context, RelativeLayout fileView, ImageView fileIcon, TextView fileSize, TextView fileName, FileItem fileItem) {
        this.activity = context;
        this.fileView = fileView;
        this.fileIcon = fileIcon;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.fileItem = fileItem;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.audio_sheet, container, false);
        SplashActivity.setAudioSheet(this);

        FilesManager filesManager = new FilesManager(activity);
        EditText contactSearchBar = rootView.findViewById(R.id.File_ex_search_bar);

        fileList = new ArrayList<>();
        this.getMusic();

        RecyclerView listView = rootView.findViewById(R.id.listView);

        adapter2 = new StoredDataListAdapter(fileList, activity, "bSheet", fContext, fileView, fileIcon, fileSize, fileName, fileItem);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
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
        ContentResolver contentResolver = activity.getContentResolver();
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
