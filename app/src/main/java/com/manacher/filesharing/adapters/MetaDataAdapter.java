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

import com.bumptech.glide.Glide;

import com.manacher.filesharing.R;
import com.manacher.filesharing.models.FileMeta;
import com.manacher.filesharing.utils.FilesManager;


import java.util.List;


public class MetaDataAdapter extends BaseAdapter {

    private Activity context;
    private LayoutInflater layoutInflater;
    private List<FileMeta> list;
    private FilesManager filesManager;




//    public MetaDataAdapter(Activity context, List<FileMeta> metaList, User callee) {
//        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.list = metaList;
//        this.filesManager = new FilesManager(context);
//        this.fireAuthService = new FireAuthService();
//        this.callee = callee;
//        this.context = context;
//    }

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
        try{


            if (convertView == null) {
                listViewHolder = new ViewHolder();

                Log.d("JKjK", "getView: list.get(position) > "  +list.get(position));
//                if (list.get(position).getSenderId().equals(fireAuthService.getUserId())){
//
//                    convertView = layoutInflater.inflate(R.layout.meta_data_view, parent, false);
//
//                }else {
//
//                    convertView = layoutInflater.inflate(R.layout.meta_data_view_receive, parent, false);
//
//                }

                listViewHolder.picture = (ImageView) convertView.findViewById(R.id.picture);

                listViewHolder.fileName = (TextView) convertView.findViewById(R.id.send_data_name);

                listViewHolder.fileIcon = (ImageView) convertView.findViewById(R.id.fileIcon);

                listViewHolder.fileSize = (TextView) convertView.findViewById(R.id.file_Size);

                convertView.setTag(listViewHolder);
            } else {
                listViewHolder = (ViewHolder) convertView.getTag();
            }

            listViewHolder.fileName.setText(list.get(position).getFileName());

            listViewHolder.fileIcon.setImageDrawable(filesManager.getIcon(list.get(position).getFileName()));

            listViewHolder.fileSize.setText(String.format("%.2f", (list.get(position).getmFileSize() * (0.00_00_00_95))));


//            if (list.get(position).getSenderId().equals(fireAuthService.getUserId())){
//
//                Glide.with(context)
//                        .load(fireAuthService.getCurrentUser().getPhotoUrl())
//                        .into(listViewHolder.picture);
//
//
//            }else {
//                Glide.with(context)
//                        .load(callee.getDpURL())
//                        .into(listViewHolder.picture);
//
//            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {
        TextView fileName;
        ImageView fileIcon;
        TextView fileSize;

        ImageView picture;
    }
}
