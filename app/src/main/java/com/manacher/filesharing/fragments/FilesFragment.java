package com.manacher.filesharing.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.manacher.filesharing.R;
import com.manacher.filesharing.activities.HomeActivity;
import com.manacher.filesharing.services.AdsServices;
import com.manacher.filesharing.utils.FilesManager;


import java.io.File;


public class FilesFragment extends Fragment {
    private HomeActivity context;
    private Fragment currentFragment;

    @SuppressLint("SdCardPath")
    private String folder_main = Environment.getExternalStorageDirectory() +"/SendKite";

    private FilesManager filesManager;

    private View rootView;


    public FilesFragment(HomeActivity context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.files_fragment, container, false);
        filesManager = new FilesManager();

        currentFragment = new OfflineFilesFragment(context);

        AdView adView = rootView.findViewById(R.id.adView);
        new AdsServices().loadBanner(adView);


        if (savedInstanceState == null) {
            context.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place_files,
                    currentFragment).commit();
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomNavigationView bottomNav = rootView.findViewById(R.id.bottom_navigation_files);
        bottomNav.getMenu().clear();

        bottomNav.inflateMenu(R.menu.offline_navigation_bar);

        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_online_files:{
                            if(!(currentFragment instanceof OnlineFilesFragment)) {
                                fragment = new OnlineFilesFragment(context);
                                addFragment(fragment);
                                currentFragment = fragment;
                            }
                            break;
                        }
                        case R.id.nav_offline_files:
                            if(!(currentFragment instanceof OfflineFilesFragment)) {
                                fragment = new OfflineFilesFragment(context);
                                addFragment(fragment);
                                currentFragment = fragment;
                            }
                            break;
                    }
                    return true;
                }
            };

    public void addFragment(Fragment fragment){
        context.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place_files,
                fragment).commit();
    }

    private void function(File file){
        if (filesManager.isFolder(file)){

            if (filesManager.isFileExists(file))

            Log.d("kum kum", "function: "+filesManager.getFileList(file));
            File[] files = filesManager.getFileList(file);
            for (int i = 0; i < files.length; ++i){
                Log.d("kaka kaka", "function: "+files[i].getName());
            }
        }
    }
}