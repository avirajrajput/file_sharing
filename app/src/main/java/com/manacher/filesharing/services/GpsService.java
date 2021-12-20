package com.manacher.filesharing.services;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

public class GpsService {
    Activity context;

    public GpsService(Activity context) {
        this.context = context;
    }


    public boolean isGpsEnable(){
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        return  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void displayPromptForEnablingGPS()
    {
//startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        final AlertDialog.Builder builder =  new AlertDialog.Builder(context);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "For send data you need to enable GPS.";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                context.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }

    public void displayWifiSettings() {
//startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        final AlertDialog.Builder builder =  new AlertDialog.Builder(context);
        final String action = Settings.ACTION_WIFI_SETTINGS;
        final String message = "For sharing data you need turn on Wifi.";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                context.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }
}
