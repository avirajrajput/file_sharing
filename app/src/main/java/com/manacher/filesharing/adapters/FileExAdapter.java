package com.manacher.filesharing.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.manacher.filesharing.R;
import com.manacher.filesharing.models.FileLength;
import com.manacher.filesharing.sheets.FileSheet;
import com.manacher.filesharing.utils.FileItem;
import com.manacher.filesharing.utils.FilesManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FileExAdapter extends RecyclerView.Adapter<FileExAdapter.FileExHolder> {
    private ArrayList<File> list;
    private Activity context;
//    private FireAuthService fireAuthService;

    private ArrayList<FileItem> bagList;
    private ArrayList<String> fileNamesList;
    private ArrayList<FileLength> fileLengthList;
    private ArrayList<Uri> fileDataList;
    private BagListAdapter adapter;
    private RelativeLayout collectorPlace;

    private File parentFile;

    private FilesManager filesManager;
    private String tag;

//
    private FileItem fileItem;
    private RelativeLayout fileView;
    private ImageView fileIcon;
    private TextView fileSize;
    private TextView fileName;
    private FileSheet bContext;

    public FileExAdapter(ArrayList<File> list,
                         Activity context,
                         ArrayList<FileItem> bagList,
                         ArrayList<String> fileNamesList,
                         ArrayList<FileLength> fileLengthList,
                         ArrayList<Uri> fileDataList,
                         BagListAdapter adapter,
                         RelativeLayout collectorPlace,
                         String tag) {
        this.list = list;
        this.context = context;
        this.adapter = adapter;
        this.collectorPlace = collectorPlace;
        this.bagList = bagList;
        this.fileNamesList = fileNamesList;
        this.fileLengthList = fileLengthList;
        this.fileDataList = fileDataList;
        this.tag = tag;

        filesManager = new FilesManager(context);


    }

    public FileExAdapter(ArrayList<File> list, String tag, FileItem fileItem, FileSheet bContext, Activity context, RelativeLayout fileView, ImageView fileIcon, TextView fileSize, TextView fileName){
        this.tag = tag;
        this.context = context;
        this.fileView = fileView;
        this.fileIcon = fileIcon;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.fileItem = fileItem;
        this.bContext = bContext;
        this.list = list;
        filesManager = new FilesManager(context);

    }

    public static class FileExHolder extends RecyclerView.ViewHolder {

        TextView fileName;
        ImageView fileIcon;
        TextView fileSize;
        CardView parent;

        LinearLayout sizeLayout;
//        ImageView addButton;
        public FileExHolder(View itemView) {
            super(itemView);

            fileName = (TextView) itemView.findViewById(R.id.send_data_name);
            fileIcon = (ImageView) itemView.findViewById(R.id.fileIcon);
            fileSize = (TextView) itemView.findViewById(R.id.file_Size);
            parent = (CardView) itemView.findViewById(R.id.parent);

            sizeLayout = (LinearLayout) itemView.findViewById(R.id.size_layout);


//            addButton = (ImageView) itemView.findViewById(R.id.addButton);
        }
    }

    @Override
    public FileExHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_ex_view, parent, false);
        return new FileExHolder(v);
    }
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(FileExHolder holder, final int position) {
        final File currentItem = list.get(position);

        if (!filesManager.isFolder(currentItem)){
        }else{
            holder.sizeLayout.setVisibility(View.GONE);
        }

//        holder.fileIcon.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_trasiton_anymation));
//        holder.parent.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_trasiton_anymation));

        holder.fileIcon.setImageDrawable(getFileIcon(currentItem.getPath()));
        holder.fileName.setText(currentItem.getName());
        holder.fileSize.setText(String.format("%.2f", ( currentItem.length()) * (0.00_00_00_95) ));

        holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        if (tag.equals("bSheet")) {

                            if (!filesManager.isFolder(currentItem)) {


                                fileItem.setIcon(getFileIcon(currentItem.getPath()));
                                fileItem.setFileName(currentItem.getName());
                                fileItem.setFilePath(String.valueOf(Uri.fromFile(currentItem)));
                                fileItem.setFileSize(currentItem.length());
                                fileItem.setProgress(0);

                                fileItem.setAbsolutePath(currentItem.getAbsolutePath());

//                                fileItem.setSenderId(fireAuthService.getUserId());

                                fileIcon.setImageDrawable(getFileIcon(currentItem.getPath()));
                                fileName.setText(currentItem.getName());
                                fileSize.setText(String.format("%.2f", (currentItem.length()) * (0.00_00_00_95)));

                                fileView.setVisibility(View.VISIBLE);
                                bContext.dismiss();


                            } else {
                                if (filesManager.isFolder(currentItem)) {

                                    list.clear();
                                    list.addAll(Arrays.asList(currentItem.listFiles()));

                                    setParentPath(currentItem);
                                    notifyDataSetChanged();
                                }
                            }

                        } else {
                            if (!filesManager.isFolder(currentItem)) {

                                if (!filesManager.isBagHasItem(bagList, currentItem.getName())) {

                                    fileNamesList.add(0, currentItem.getName());
                                    fileLengthList.add(0, new FileLength(currentItem.length()));
                                    fileDataList.add(0, Uri.fromFile(currentItem));

                                    bagList.add(0, new FileItem(currentItem.getName(),
                                            getFileIcon(currentItem.getPath()),
                                            currentItem.getAbsolutePath(),
                                            currentItem.length()));

                                    if (bagList.isEmpty()) {
                                        collectorPlace.setVisibility(View.GONE);

                                    } else {
                                        collectorPlace.setVisibility(View.VISIBLE);

                                    }

                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                if (filesManager.isFolder(currentItem)) {

                                    list.clear();
                                    list.addAll(Arrays.asList(currentItem.listFiles()));

                                    setParentPath(currentItem);
                                    notifyDataSetChanged();
                                }
                            }
                        }
                    }catch (Exception e){

                    }
                }

            });

    }

    public void filterList(ArrayList<File> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getFileIcon(String filePath) {

        if (filesManager.isFolder(new File(filePath))) {
            return(context.getResources().getDrawable(R.drawable.folder_icon));
        }

        else if (filesManager.getExtension(filePath).equals("pdf")) {

            return (context.getResources().getDrawable(R.drawable.pdf));


        } else if (filesManager.getExtension(filePath).equals("text")) {


            return(context.getResources().getDrawable(R.drawable.txt));


        } else if (filesManager.getExtension(filePath).equals("mp3")) {


            return(context.getResources().getDrawable(R.drawable.mp3));


        } else if (filesManager.getExtension(filePath).equals("jpg")) {

            return(context.getResources().getDrawable(R.drawable.jpg));


        } else if (filesManager.getExtension(filePath).equals("png")) {

            return(context.getResources().getDrawable(R.drawable.png));


        } else if (filesManager.getExtension(filePath).equals("mp4")) {

            return(context.getResources().getDrawable(R.drawable.video_file));


        } else if (filesManager.getExtension(filePath).equals("apk")) {
            return(context.getResources().getDrawable(R.drawable.apk));

        }
        else {
            return(context.getResources().getDrawable(R.drawable.file_icon2));
        }
    }


    public File getParentPath() {
        return parentFile;
    }

    public void setParentPath(File parentFile) {
        this.parentFile = parentFile;
    }
}
