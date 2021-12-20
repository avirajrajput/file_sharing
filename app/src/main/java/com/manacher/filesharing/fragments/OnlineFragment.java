package com.manacher.filesharing.fragments;

import android.Manifest;
import android.app.Activity;


import android.os.Bundle;
import android.os.Build;
import android.os.Environment;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;

import android.widget.LinearLayout;


import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdView;
import com.manacher.filesharing.R;
import com.manacher.filesharing.activities.HomeActivity;
import com.manacher.filesharing.activities.ReceiverActivity;
import com.manacher.filesharing.activities.SendSelectActivity;
import com.manacher.filesharing.services.AdsServices;
import com.manacher.filesharing.services.GpsService;
import com.manacher.filesharing.services.PermissionsServices;
import com.manacher.filesharing.utils.Routing;



public class OnlineFragment extends Fragment {
    private Activity context;
    private Routing routing;

    private GpsService gpsService;
    private LinearLayout receiveButton;

    private LinearLayout sendOffline;
    private PermissionsServices permissionsServices;

    public OnlineFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = getActivity();
        routing = new Routing(context);

        View rootView = inflater.inflate(R.layout.fragment_online, container, false);

        this.initialization(rootView);

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                receiveButtonPressed();

            }
        });

        sendOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendButtonPressed();
            }
        });

//        AdView adView = rootView.findViewById(R.id.adView);
//        TemplateView template = rootView.findViewById(R.id.my_template);
//        AdsServices adsServices = new AdsServices();
//
//        adsServices.loadNative(template, context);
//        adsServices.loadBanner(adView);

        return rootView;
    }

    private void receiveButtonPressed(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            if (Environment.isExternalStorageManager()) {
                setReceiveButton();
            } else {
                permissionsServices.askForAllStorage();
            }


        }else {
            setReceiveButton();

        }

    }

    private void setReceiveButton(){
        if(!gpsService.isGpsEnable()){
            gpsService.displayPromptForEnablingGPS();
            return;

        }

        if (permissionsServices.isAlreadyGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
            if (permissionsServices.isAlreadyGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                if(permissionsServices.isAlreadyGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
                    routing.navigate(ReceiverActivity.class, false);

                }else{
                    ((HomeActivity)context).transferActivity = getString(R.string.receiver_tag);
                    permissionsServices.askForAccessFineLocationPermission();
                }

            }else {
                ((HomeActivity)context).transferActivity = getString(R.string.receiver_tag);
                permissionsServices.askForWritePermission();
            }

        }else{
            ((HomeActivity)context).transferActivity = getString(R.string.receiver_tag);
            permissionsServices.askForReadPermission();

        }
    }

    private void sendButtonPressed(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            if (Environment.isExternalStorageManager()) {
                setSendOffline();
            } else {
                permissionsServices.askForAllStorage();
            }


        }else {
            setSendOffline();
        }
    }

    private void setSendOffline(){

        if (permissionsServices.isAlreadyGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (permissionsServices.isAlreadyGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (permissionsServices.isAlreadyGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    routing.navigate(SendSelectActivity.class, false);

                } else {
                    ((HomeActivity) context).transferActivity = getString(R.string.sender_tag);
                    permissionsServices.askForAccessFineLocationPermission();

                }

            } else {
                ((HomeActivity) context).transferActivity = getString(R.string.sender_tag);
                permissionsServices.askForWritePermission();

            }

        } else {
            ((HomeActivity) context).transferActivity = getString(R.string.sender_tag);
            permissionsServices.askForReadPermission();

        }
    }

    private void initialization(View rootView){
        receiveButton = rootView.findViewById(R.id.receive);
        sendOffline = rootView.findViewById(R.id.send);
        permissionsServices = new PermissionsServices(context);
        gpsService = new GpsService(getActivity());

    }

}