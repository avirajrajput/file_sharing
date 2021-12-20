package com.manacher.filesharing.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.manacher.filesharing.R;
import com.manacher.filesharing.models.FileLength;
import com.manacher.filesharing.utils.FileItem;


import java.util.ArrayList;
import java.util.List;

public class TransferFilesListAdapter extends BaseAdapter {
    private Activity context;


    private LayoutInflater layoutInflater;
    private List<FileItem> list;

    private ArrayList<String> fileNamesList;
    private ArrayList<FileLength> fileLengthList;
    private ArrayList<Uri> fileDataList;

    private String tag;
    private String typeTag;


    public TransferFilesListAdapter(Activity context, List<FileItem> list, String tag, String typeTag) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.tag = tag;
        this.context = context;
        this.typeTag = typeTag;

    }

    public TransferFilesListAdapter(Activity context, List<FileItem> list, ArrayList<String> fileNamesList,
                                    ArrayList<FileLength> fileLengthList, ArrayList<Uri> fileDataList, String tag){

        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.list = list;
        this.fileNamesList = fileNamesList;
        this.fileLengthList = fileLengthList;
        this.fileDataList = fileDataList;
        this.tag = tag;
        this.context = context;


    }

    public TransferFilesListAdapter(Activity context, List<FileItem> list, String tag){

        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.list = list;
        this.tag = tag;
        this.context = context;


    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();

            if (tag.equals(context.getString(R.string.online_tag))) {

//                if (list.get(position).getSenderId().equals(fireAuthService.getUserId())) {
//                    convertView = layoutInflater.inflate(R.layout.send_data_view, parent, false);
//
//                } else {
//                    convertView = layoutInflater.inflate(R.layout.receive_data_view, parent, false);
//
//                }
            } else if (tag.equals(context.getString(R.string.offline_tag))){

                if (typeTag.equals(context.getString(R.string.sender_tag))) {
                    convertView = layoutInflater.inflate(R.layout.send_data_view, parent, false);

                } else if (typeTag.equals(context.getString(R.string.receiver_tag))) {
                    convertView = layoutInflater.inflate(R.layout.receive_data_view, parent, false);

                }
            }


                listViewHolder.fileName = (TextView) convertView.findViewById(R.id.send_data_name);
                listViewHolder.fileIcon = (ImageView) convertView.findViewById(R.id.fileIcon);
                listViewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar_sending);

                listViewHolder.doneIcon = (ImageView) convertView.findViewById(R.id.progress_done);

                listViewHolder.fileSize = (TextView) convertView.findViewById(R.id.file_Size);
                listViewHolder.fileProgress = (TextView) convertView.findViewById(R.id.currentDataStatus);

                listViewHolder.deleteButton = (Button) convertView.findViewById(R.id.deleteButton);
                convertView.setTag(listViewHolder);



        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }

        setDataItem(listViewHolder, position);

//        if (tag.equals(context.getString(R.string.offline_tag))){
//
//            setDataItem(listViewHolder, position);
//
//        } else if (tag.equals(context.getString(R.string.online_tag))){
//
//                if (list.get(position).getSenderId().equals(fireAuthService.getUserId())) {
//
//                    setDataItem(listViewHolder, position);
//
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            listViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    list.remove(position);
//                                    fileDataList.remove(position);
//                                    fileLengthList.remove(position);
//                                    fileNamesList.remove(position);
//                                    notifyDataSetChanged();
//
////                                SendOnlineActivity.setDataConfigure(fileNamesList, fileLengthList, callee);
//
//                                }
//                            });
//                        }
//                    }).start();
//
//                } else {
//                    setDataItem(listViewHolder, position);
//
//                }
//
//            }

        return convertView;
    }

    @SuppressLint("DefaultLocale")
    private void setDataItem(ViewHolder listViewHolder, int position){
        listViewHolder.fileName.setText(list.get(position).getFileName());

        listViewHolder.fileIcon.setImageDrawable(list.get(position).getIcon());
        listViewHolder.progressBar.setProgress(list.get(position).getProgress());
        list.get(position).setProgressBar(listViewHolder.progressBar);
        listViewHolder.doneIcon.setImageDrawable(list.get(position).getDone());

        listViewHolder.fileSize.setText(String.format("%.2f", (list.get(position).getFileSize() * (0.00_00_00_95))));

        long currentSize = (long) (((list.get(position).getFileSize()
                * list.get(position).getProgress())
                / 100)
                * (0.00_00_00_95));

        listViewHolder.fileProgress.setText(String.valueOf(currentSize));

        listViewHolder.deleteButton.setVisibility(View.GONE);

    }

    static class ViewHolder {
        TextView fileName;
        ImageView fileIcon;
        ProgressBar progressBar;
        ImageView doneIcon;
        TextView fileSize;
        TextView fileProgress;

        Button deleteButton;
    }

    public void addItem(FileItem fileItem){
        this.list.add(fileItem);

    }
}
