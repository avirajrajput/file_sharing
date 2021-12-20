package com.manacher.filesharing.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.Fragment;

import com.manacher.filesharing.R;
import com.manacher.filesharing.adapters.BagListAdapter;
import com.manacher.filesharing.models.FileLength;
import com.manacher.filesharing.utils.FileItem;
import com.manacher.filesharing.utils.FilesManager;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApkFragment extends Fragment {
    private List<AppList> installedAppsList;
    private AppAdapter installedAppAdapter;
    private GridView listView;

    private Activity context;

    private ArrayList<String> fileNamesList;
    private ArrayList<FileLength> fileLengthList;
    private ArrayList<Uri> fileDataList;

    private ArrayList<FileItem> bagList;

    private BagListAdapter adapter2;
    private RelativeLayout collectorPlace;

    private FilesManager filesManager;

    private String tag;

//    private FireAuthService fireAuthService;

    public ApkFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        context = getActivity();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        this.context = getActivity();
    }

    public ApkFragment(Activity context,
                       ArrayList<String> fileNamesList,
                       ArrayList<FileLength> fileLengthList,
                       ArrayList<Uri> fileData,
                       ArrayList<FileItem> bagList,
                       BagListAdapter adapter2,
                       RelativeLayout collectorPlace,
                       String tag) {

        this.context = context;
        this.fileNamesList = fileNamesList;
        this.fileLengthList = fileLengthList;
        this.fileDataList = fileData;
        this.bagList = bagList;
        this.adapter2 = adapter2;
        this.collectorPlace = collectorPlace;
        this.tag = tag;
//        this.fireAuthService = new FireAuthService();

    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_apk, container, false);
        try{


            listView = (GridView) rootView.findViewById(R.id.installed_app_list);
            filesManager = new FilesManager(context);

            installedAppsList = new ArrayList<>();
            installedAppAdapter = new AppAdapter(context, installedAppsList);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    installedAppsList = getInstalledApps();
                    installedAppAdapter.setListStorage(installedAppsList);

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            installedAppAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();

            listView.setAdapter(installedAppAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                    File file = new File(installedAppsList.get(i).getPackages().applicationInfo.publicSourceDir);
                    String appName = installedAppsList.get(i).getName()+".apk";

                    setUpList(file, appName, installedAppsList.get(i));
                   // writeFile(file, appName);
                }
            });

            //Total Number of Installed-Apps(i.e. List Size)
            String  abc = listView.getCount()+"";
            TextView countApps = (TextView)rootView.findViewById(R.id.countApps);
            countApps.setText("Total Installed Apps: "+abc);
            Toast.makeText(context, abc+" Apps", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            e.printStackTrace();
        }
         return rootView;
    }

    private void setUpList(File file, String fileName, AppList item){

            if (!filesManager.isBagHasItem(bagList, fileName)){

                fileNamesList.add(0, fileName);
                fileLengthList.add(0, new FileLength(file.length()));
                fileDataList.add(0, Uri.fromFile(file));

                bagList.add(0, new FileItem(fileName, item.getIcon(), file.getAbsolutePath(), file.length()));



                if (bagList.isEmpty()){
                    collectorPlace.setVisibility(View.GONE);

                }else {
                    collectorPlace.setVisibility(View.VISIBLE);

                }
                adapter2.notifyDataSetChanged();

            }
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

        public void setListStorage(List<AppList> listStorage){
            this.listStorage = listStorage;
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

            ViewHolder listViewHolder;
            if(convertView == null){
                listViewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.installed_app_list, parent, false);

                listViewHolder.textInListView = (TextView)convertView.findViewById(R.id.list_app_name);
                listViewHolder.imageInListView = (ImageView)convertView.findViewById(R.id.app_icon);

                convertView.setTag(listViewHolder);
            }else{
                listViewHolder = (ViewHolder)convertView.getTag();
            }
                listViewHolder.textInListView.setText(listStorage.get(position).getName());
                listViewHolder.imageInListView.setImageDrawable(listStorage.get(position).getIcon());


            return convertView;
        }

        class ViewHolder{
            TextView textInListView;
            ImageView imageInListView;
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
