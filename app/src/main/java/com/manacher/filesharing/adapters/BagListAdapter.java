package com.manacher.filesharing.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.manacher.filesharing.R;
import com.manacher.filesharing.models.FileLength;
import com.manacher.filesharing.utils.FileItem;
import com.manacher.filesharing.utils.FilesManager;

import java.util.ArrayList;

public class BagListAdapter extends RecyclerView.Adapter<BagListAdapter.BagListViewHolder> {
    private ArrayList<FileItem> list;
    private ArrayList<String> fileNamesList;
    private ArrayList<FileLength> fileLengthList;
    private ArrayList<Uri> fileDataList;

    private RelativeLayout collectorPlace;

    private Activity context;
    private FilesManager filesManager;

    private String tag;

    public BagListAdapter(ArrayList<FileItem> list,
                          ArrayList<String> fileNamesList,
                          ArrayList<FileLength> fileLengthList,
                          ArrayList<Uri> fileDataList,
                          Activity context,
                          RelativeLayout collectorPlace,
                          String tag) {
        this.list = list;
        this.fileNamesList = fileNamesList;
        this.fileLengthList = fileLengthList;
        this.fileDataList = fileDataList;
        this.context = context;
        this.collectorPlace = collectorPlace;
        this.tag = tag;
        filesManager = new FilesManager();
    }

//    public BagListAdapter(ArrayList<FileItem> list, RelativeLayout collectorPlace, Activity context, String tag) {
//        this.list = list;
//        this.collectorPlace = collectorPlace;
//        this.context = context;
//        filesManager = new FilesManager();
//        this.tag = tag;
//    }

    public static class BagListViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        ImageView fileIcon;
        Button closeButton;
        CardView parent;

        public BagListViewHolder(View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.coll_name);
            fileIcon = itemView.findViewById(R.id.coll_icon);
            closeButton = itemView.findViewById(R.id.delete_bag_item);
            parent = (CardView) itemView.findViewById(R.id.parent);
        }
    }


    @Override
    public BagListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bag_item_view, parent, false);
        return new BagListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BagListViewHolder holder, final int position) {
        final FileItem currentItem = list.get(position);

            holder.fileIcon.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_trasiton_anymation));
            holder.fileName.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_trasiton_anymation));


        holder.fileName.setText(currentItem.getFileName());
        holder.fileIcon.setImageDrawable(currentItem.getIcon());

        holder.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    list.remove(position);

                    fileNamesList.remove(position);
                    fileLengthList.remove(position);
                    fileDataList.remove(position);

                    if (list.isEmpty()){
                        collectorPlace.setVisibility(View.GONE);
                    }else {
                        collectorPlace.setVisibility(View.VISIBLE);
                    }
                    notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
