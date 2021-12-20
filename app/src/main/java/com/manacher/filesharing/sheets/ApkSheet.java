package com.manacher.filesharing.sheets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.manacher.filesharing.R;
import com.manacher.filesharing.activities.SplashActivity;
import com.manacher.filesharing.utils.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApkSheet extends BottomSheetDialogFragment {
    private ApkSheet fContext;

//    private FireAuthService fireAuthService;
    private List<AppList> installedAppsList;
    private AppAdapter installedAppAdapter;
    private GridView listView;
    private Activity context;
    private FileItem fileItem;
    private RelativeLayout fileView;
    private ImageView fileIcon;
    private TextView fileSize;
    private TextView fileName;

    public ApkSheet(Activity context, FileItem fileItem, RelativeLayout fileView, ImageView fileIcon, TextView fileSize, TextView fileName) {
        this.context = context;
        this.fileItem = fileItem;
        this.fileView = fileView;

        this.fileIcon = fileIcon;
        this.fileSize = fileSize;
        this.fileName = fileName;

//        this.fireAuthService = new FireAuthService();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.apk_sheet, container, false);
        SplashActivity.setApkSheet(this);

        fContext = this;
        listView = (GridView) rootView.findViewById(R.id.installed_app_list);

        installedAppsList = getInstalledApps();

        installedAppAdapter = new AppAdapter(context, installedAppsList);
        listView.setAdapter(installedAppAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                File file = new File(installedAppsList.get(i).getPackages().applicationInfo.publicSourceDir);
                String appName = installedAppsList.get(i).getName()+".apk";
                Drawable icon = installedAppsList.get(i).getIcon();

                fileItem.setIcon(icon);
                fileItem.setFileName(appName);
                fileItem.setFilePath(String.valueOf(Uri.fromFile(file)));
                fileItem.setFileSize(file.length());
                fileItem.setProgress(0);
                fileItem.setAbsolutePath(file.getAbsolutePath());

//                fileItem.setSenderId(fireAuthService.getUserId());
//                Log.d("GHGA", "setSenderId: "+fireAuthService.getUserId());

                fileIcon.setImageDrawable(icon);
                fileName.setText(appName);
                fileSize.setText(String.format("%.2f", ( file.length()) * (0.00_00_00_95) ));

                fileView.setVisibility(View.VISIBLE);
                fContext.dismiss();
            }
        });

        //Total Number of Installed-Apps(i.e. List Size)
        String  abc = listView.getCount()+"";
        TextView countApps = (TextView)rootView.findViewById(R.id.countApps);
        countApps.setText("Total Installed Apps: "+abc);
        Toast.makeText(context, abc+" Apps", Toast.LENGTH_SHORT).show();
        return rootView;
    }

    private List<AppList> getInstalledApps() {
        PackageManager pm = context.getPackageManager();

        List<AppList> apps = new ArrayList<>();

        List<PackageInfo> packs = pm.getInstalledPackages(0);

        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!isSystemPackage(p))) {
                String appName = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(context.getPackageManager());
                apps.add(new AppList(appName, icon, p));
            }
        }
        return apps;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    public class AppAdapter extends BaseAdapter {

        public LayoutInflater layoutInflater;
        public List<AppList> listStorage;

        public AppAdapter(Context context, List<AppList> customizedListView) {
            layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listStorage = customizedListView;
        }

        @Override
        public int getCount() {
            return listStorage.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            AppAdapter.ViewHolder listViewHolder;
            if(convertView == null){
                listViewHolder = new AppAdapter.ViewHolder();
                convertView = layoutInflater.inflate(R.layout.installed_app_list, parent, false);

                listViewHolder.textInListView = (TextView)convertView.findViewById(R.id.list_app_name);
                listViewHolder.imageInListView = (ImageView)convertView.findViewById(R.id.app_icon);
//                listViewHolder.packageInListView = (TextView)convertView.findViewById(R.id.app_package);
                listViewHolder.doneIcon = (ImageView)convertView.findViewById(R.id.selected);

                convertView.setTag(listViewHolder);
            }else{
                listViewHolder = (ViewHolder)convertView.getTag();
            }
            listViewHolder.textInListView.setText(listStorage.get(position).getName());
            listViewHolder.imageInListView.setImageDrawable(listStorage.get(position).getIcon());
//                listViewHolder.packageInListView.setText(listStorage.get(position).getPackages().packageName);


            return convertView;
        }

        class ViewHolder{
            TextView textInListView;
            ImageView imageInListView;
            //            TextView packageInListView;
            ImageView doneIcon;
        }
    }

    public class AppList {
        private String name;
        private Drawable icon;
        private PackageInfo packages;

        public AppList(String name, Drawable icon, PackageInfo packages) {
            this.name = name;
            this.icon = icon;
            this.packages = packages;
        }

        public String getName() {
            return name;
        }

        public Drawable getIcon() {
            return icon;
        }

        public PackageInfo getPackages() {
            return packages;
        }

    }
}
