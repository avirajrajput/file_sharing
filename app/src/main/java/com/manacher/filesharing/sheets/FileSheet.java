package com.manacher.filesharing.sheets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.manacher.filesharing.R;
import com.manacher.filesharing.activities.SplashActivity;
import com.manacher.filesharing.adapters.FileExAdapter;
import com.manacher.filesharing.utils.FileItem;
import com.manacher.filesharing.utils.FilesManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class FileSheet extends BottomSheetDialogFragment {
    @SuppressLint("SdCardPath")
    private final String rootPath = "/sdcard";

    private Activity activity;
    private FileSheet bContext = this;

    private ArrayList<File> fileList;
    private File parentFile;

    private FilesManager filesManager;
    private FileExAdapter adapter;
    private FloatingActionButton backButton;

    private FileItem fileItem;
    private RelativeLayout fileView;
    private ImageView fileIcon;
    private TextView fileSize;
    private TextView fileName;

    public FileSheet(Activity context, FileItem fileItem, RelativeLayout fileView, ImageView fileIcon, TextView fileSize, TextView fileName) {

        this.activity = context;
        this.fileItem = fileItem;
        this.fileView = fileView;
        this.fileIcon = fileIcon;
        this.fileSize = fileSize;
        this.fileName = fileName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.file_sheet, container, false);
        EditText contactSearchBar = rootView.findViewById(R.id.File_ex_search_bar);
        this.initialization(rootView);
        SplashActivity.setFileSheet(this);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (adapter.getParentPath() != null){
                    if (!adapter.getParentPath().getPath().equals(rootPath)){
                        parentFile = adapter.getParentPath().getParentFile();
                        adapter.setParentPath(parentFile);
                        fill(parentFile);
                    }
                }
            }
        });

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

    private void initialization(View rootView){
        filesManager = new FilesManager(this.activity);

        parentFile = new File(this.rootPath);

        fileList = new ArrayList<>(Arrays.asList(filesManager.getFileList(parentFile)));

        backButton = rootView.findViewById(R.id.back_fab);


        adapter = new FileExAdapter(fileList, "bSheet", fileItem, bContext, activity, fileView, fileIcon, fileSize, fileName);


        RecyclerView listView = rootView.findViewById(R.id.files_listView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(adapter);

    }

    private void fill(File currentFile){
        if (filesManager.isFolder(currentFile)){
            fileList.clear();
            fileList.addAll(Arrays.asList(filesManager.getFileList(currentFile)));
            parentFile = currentFile;

            adapter.notifyDataSetChanged();
        }
    }

    private void filter(String text){
        ArrayList<File> filterList = new ArrayList<>();

        for (File item : fileList){
            if (item.getName().toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);
            }
        }
        adapter.filterList(filterList);
    }

}
