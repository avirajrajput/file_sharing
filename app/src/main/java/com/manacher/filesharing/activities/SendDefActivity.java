package com.manacher.filesharing.activities;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;


import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdView;
import com.manacher.filesharing.R;
import com.manacher.filesharing.models.FileLength;
import com.manacher.filesharing.services.AdsServices;
import com.manacher.filesharing.services.GpsService;
import com.manacher.filesharing.services.PermissionsServices;
import com.manacher.filesharing.utils.FileItem;
import com.manacher.filesharing.utils.FilesManager;

import java.io.File;
import java.util.ArrayList;

public class SendDefActivity extends BaseActivity {
    private Activity context = this;
    private ArrayList<FileItem> bagList = new ArrayList<>();
    private ArrayList<String> fileNamesList = new ArrayList<>();
    private ArrayList<FileLength> fileLengthList = new ArrayList<>();
    private ArrayList<Uri> fileDataList = new ArrayList<>();
    private GpsService gpsService;
    private PermissionsServices permissionsServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_def);
        LinearLayout sendOffline = findViewById(R.id.sendOffline);
        gpsService = new GpsService(context);
        Intent intent = this.getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        permissionsServices = new PermissionsServices(this);

        if (Intent.ACTION_SEND.equals(action) && type != null) {

            if (type.startsWith("image/")) {
                handleSendImage(intent);// Handle multiple images being sent

            }else{

                this.handleSendFile(intent);
            }

        }
        else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {


            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent

            }else{
                this.handleSendMultipleFiles(intent);

            }
        }

        sendOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        AdView adView = findViewById(R.id.adView);
        TemplateView template = findViewById(R.id.my_template);
        AdsServices adsServices = new AdsServices();

        adsServices.loadNative(template, context);
        adsServices.loadBanner(adView);
    }

    private void sendButton(){
        Intent intent = new Intent(context, DiscoverPeersActivity.class);
        intent.putStringArrayListExtra("file_name_list", fileNamesList);
        intent.putParcelableArrayListExtra("file_length_list", fileLengthList);
        intent.putParcelableArrayListExtra("file_data_list", fileDataList);
        intent.putParcelableArrayListExtra("bag_list", bagList);

        if (gpsService.isGpsEnable()){
            startActivity(intent);
        }else{
            gpsService.displayPromptForEnablingGPS();
        }
    }

    private void setSendOffline(){

        if (permissionsServices.isAlreadyGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (permissionsServices.isAlreadyGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (permissionsServices.isAlreadyGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    sendButton();

                } else {
//                    ((HomeActivity) context).transferActivity = getString(R.string.sender_tag);
                    permissionsServices.askForAccessFineLocationPermission();
                }

            } else {
//                ((HomeActivity) context).transferActivity = getString(R.string.sender_tag);
                permissionsServices.askForWritePermission();
            }

        } else {
//            ((HomeActivity) context).transferActivity = getString(R.string.sender_tag);
            permissionsServices.askForReadPermission();

        }
    }

    private void handleSendFile(Intent intent) {
        Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (uri != null) {
            setFilesOnLists(uri);

        }
    }

    private void handleSendMultipleFiles(Intent intent) {
        ArrayList<Uri> uris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (uris != null) {
            for (Uri uri : uris) {
                setFilesOnLists(uri);

            }
        }
    }

    private void setFilesOnLists(Uri uri) {

        File file = new File(uri.getPath());
        fileDataList.add(uri);
        fileNamesList.add(file.getName()+".png");
        Log.d("fdfuyf87", "setFilesOnLists: "+getMimeType(file));

        fileLengthList.add(new FileLength(FilesManager.getRealSizeFromUri(context, uri)));
        bagList.add(new FileItem(file.getName()+".png", null, file.getPath(), FilesManager.getRealSizeFromUri(context, uri), "12345"));

    }

    private void handleSendImage(Intent intent) {

        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            setFilesOnLists(imageUri);

        }
    }

    private void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);

            // Update UI to reflect multiple images being shared
            if (imageUris != null) {
                for (Uri uri : imageUris) {
                    setFilesOnLists(uri);

                }
            }
    }

    public String getMimeType(File file) {

        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);

        if (type == null)
            type = "*/*";

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        Uri data = Uri.fromFile(file);
//
//        intent.setDataAndType(data, type);
//
//        startActivity(intent);

        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("Home Activity", "onRequestPermissionsResult: requestCode >>"+requestCode);


        switch (requestCode) {
            case PermissionsServices.READ_PERMISSION_CODE: {
                Log.d("Home Activity", "onRequestPermissionsResult: grantResults[0] >>"+grantResults[0]);
                if (PermissionsServices.isAlreadyGranted(grantResults[0])) {
                    if (permissionsServices.isAlreadyGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        if(permissionsServices.isAlreadyGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
                            sendButton();

                        }else{
                            permissionsServices.askForAccessFineLocationPermission();

                        }

                    } else {

                        permissionsServices.askForWritePermission();
                    }

                    break;
                }else{
                    Intent intent = new Intent(context, PermissionActivity.class);
                    intent.putExtra("title", getString(R.string.storage_tile));
                    intent.putExtra("dis", getString(R.string.storage_dis));
                    startActivity(intent);
                }
            }

            case PermissionsServices.WRITE_PERMISSION_CODE: {

                if (grantResults.length > 0 && PermissionsServices.isAlreadyGranted(grantResults[0])) {


                    if(permissionsServices.isAlreadyGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
                        sendButton();

                    }else{
                        permissionsServices.askForAccessFineLocationPermission();
                    }

                } else {

                    Intent intent = new Intent(context, PermissionActivity.class);
                    intent.putExtra("title", getString(R.string.storage_tile));
                    intent.putExtra("dis", getString(R.string.storage_dis));
                    startActivity(intent);

                }

                break;
            }


            case PermissionsServices.ACCESS_FINE_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                Log.d("Home Activity", "onRequestPermissionsResult: grantResults[0] >>"+grantResults[0]);
                if (PermissionsServices.isAlreadyGranted(grantResults[0])) {
                    sendButton();

                } else {
                    Intent intent = new Intent(context, PermissionActivity.class);
                    intent.putExtra("title", getString(R.string.location_tile));
                    intent.putExtra("dis", getString(R.string.location_dis));

                    startActivity(intent);
                }

                break;
            }

            case -1:
                break;
        }
    }
}