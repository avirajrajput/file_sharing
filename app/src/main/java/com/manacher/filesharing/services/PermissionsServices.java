package com.manacher.filesharing.services;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsServices {

    private Activity context;

    public static final int READ_PERMISSION_CODE = 101;
    public static final int READ_CONTACTS_PERMISSION_CODE = 201;
    public static final int ACCESS_COARSE_PERMISSION_CODE = 301;
    public static final int ACCESS_FINE_PERMISSION_CODE = 401;
    public static final int WRITE_PERMISSION_CODE = 501;
    public static final int RECORD_AUDIO_CODE = 701;

    public static final int READ_PHONE_STATE = 601;
    public static final int MANAGE_EXTERNAL_CODE = 801;
    public PermissionsServices(Activity context) {
        this.context = context;
    }

    public void askForReadPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String permission_manifest = Manifest.permission.READ_EXTERNAL_STORAGE;

            if(this.isAlreadyGranted(permission_manifest)){
                return;
            }
            context.requestPermissions(new String[]{permission_manifest}, READ_PERMISSION_CODE);
        }
    }
    public void askForWritePermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String permission_manifest = Manifest.permission.WRITE_EXTERNAL_STORAGE;

            if(this.isAlreadyGranted(permission_manifest)){
                return;
            }
            context.requestPermissions(new String[]{permission_manifest}, WRITE_PERMISSION_CODE);
        }
    }

    public void askForAccessFineLocationPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String permission_manifest = Manifest.permission.ACCESS_FINE_LOCATION;

            if(this.isAlreadyGranted(permission_manifest)){
                return;
            }
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_PERMISSION_CODE);
        }
    }

    public void askForAccessCoarseLocationPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String permission_manifest = Manifest.permission.ACCESS_COARSE_LOCATION;

            if(this.isAlreadyGranted(permission_manifest)){
                return;
            }
            context.requestPermissions(new String[]{permission_manifest}, ACCESS_COARSE_PERMISSION_CODE);
        }
    }
    public void askForReadPhoneStatePermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String permission_manifest = Manifest.permission.READ_PHONE_STATE;

            if(this.isAlreadyGranted(permission_manifest)){
                return;
            }
            context.requestPermissions(new String[]{permission_manifest}, ACCESS_COARSE_PERMISSION_CODE);
        }
    }
    public void askForReadContactPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String permission_manifest = Manifest.permission.READ_CONTACTS;

            if(this.isAlreadyGranted(permission_manifest)){
                return;
            }
            context.requestPermissions(new String[]{permission_manifest}, READ_CONTACTS_PERMISSION_CODE);
        }
    }

    public boolean isAlreadyGranted(String permission_manifest){

        int permission = ContextCompat.checkSelfPermission(context, permission_manifest);
        return (permission == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean isAlreadyGranted(int code){
        return code == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isPermissionGrated(String permission_manifest, Activity context){
        return (ActivityCompat.checkSelfPermission(context, permission_manifest) == PackageManager.PERMISSION_GRANTED);
    }

    public void askForMicPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String permission_manifest = Manifest.permission.RECORD_AUDIO;
            if(this.isAlreadyGranted(permission_manifest)){
                return;
            }
            context.requestPermissions(new String[]{permission_manifest}, RECORD_AUDIO_CODE);
        }
    }

    public void askForAllStorage(){
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);

    }

}
