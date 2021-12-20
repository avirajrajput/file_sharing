package com.manacher.filesharing.fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.manacher.filesharing.R;

public class NoPermissionFragment extends Fragment {

    private Activity activity;
    private Button turnOn;

    public NoPermissionFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_no_permission, container, false);

        turnOn = view.findViewById(R.id.turnOn);

       turnOn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startInstalledAppDetailsActivity();
           }
       });

        return view;
    }

    public void startInstalledAppDetailsActivity() {
        if (activity == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + activity.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        activity.startActivity(i);
    }
}