package com.manacher.filesharing.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.manacher.filesharing.R;


public class DeviceListAdapter extends BaseAdapter {
    public LayoutInflater layoutInflater;
    public String[] list;

    public DeviceListAdapter(Context context, String[] strings) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        list = strings;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.device_list_view, parent, false);

            listViewHolder.deviceName = (TextView) convertView.findViewById(R.id.device_name);

            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }


        listViewHolder.deviceName.setText(list[position]);

        return convertView;
    }

    static class ViewHolder {
        TextView deviceName;
    }
}
