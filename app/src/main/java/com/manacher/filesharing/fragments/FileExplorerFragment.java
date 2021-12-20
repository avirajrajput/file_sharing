package com.manacher.filesharing.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.manacher.filesharing.R;
import com.manacher.filesharing.adapters.BagListAdapter;
import com.manacher.filesharing.adapters.FileExAdapter;
import com.manacher.filesharing.models.FileLength;
import com.manacher.filesharing.utils.FileItem;
import com.manacher.filesharing.utils.FilesManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class FileExplorerFragment extends Fragment {
    private Activity context;
    @SuppressLint("SdCardPath")
    private final String rootPath = "/sdcard";

    private ArrayList<File> fileList;
    private File parentFile;

    private FilesManager filesManager;
    private FileExAdapter adapter;

    private BagListAdapter adapter2;
    private ArrayList<String> fileNamesList;
    private ArrayList<FileLength> fileLengthList;
    private ArrayList<Uri> fileDataList;
    private ArrayList<FileItem> bagList;
    private RelativeLayout collectorPlace;

    private FloatingActionButton backButton;

    private String tag;


    public FileExplorerFragment(Activity context,
                        ArrayList<FileItem> bagList,
                        BagListAdapter adapter2,
                        ArrayList<String> fileNamesList,
                        ArrayList<FileLength> fileLengthList,
                        ArrayList<Uri> fileDataList,
                        RelativeLayout collectorPlace,
                                String tag) {

        this.context = context;
        this.bagList = bagList;
        this.adapter2 = adapter2;

        this.fileNamesList = fileNamesList;
        this.fileLengthList = fileLengthList;
        this.fileDataList = fileDataList;
        this.collectorPlace = collectorPlace;

        this.tag = tag;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_file_explorer, container, false);
            EditText contactSearchBar = rootView.findViewById(R.id.File_ex_search_bar);
            this.initialization(rootView);

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setBackButton();

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

    public void setBackButton(){
        if (adapter.getParentPath() != null){
            if (!adapter.getParentPath().getPath().equals(rootPath)){
                parentFile = adapter.getParentPath().getParentFile();
                adapter.setParentPath(parentFile);
                fill(parentFile);
            }
        }
    }

    private void initialization(View rootView){
        filesManager = new FilesManager(this.context);

        parentFile = new File(this.rootPath);

        fileList = new ArrayList<>(Arrays.asList(filesManager.getFileList(parentFile)));

        backButton = rootView.findViewById(R.id.back_fab);


                adapter = new FileExAdapter(fileList,
                        context,
                        bagList,
                        fileNamesList,
                        fileLengthList,
                        fileDataList,
                        adapter2,
                        collectorPlace,
                        tag);


        RecyclerView listView = rootView.findViewById(R.id.files_listView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(adapter);

    }


    //    private void openBottomSheet(int position){
//        DeleteBottomSheet bottomSheet = new DeleteBottomSheet(context, fileList, position, adapter);
//        bottomSheet.show(((FragmentActivity)context).getSupportFragmentManager(), "exampleBottomSheet");
//        adapter.notifyDataSetChanged();
//    }
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