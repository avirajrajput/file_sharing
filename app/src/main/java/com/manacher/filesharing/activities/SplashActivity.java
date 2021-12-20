package com.manacher.filesharing.activities;

import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.manacher.filesharing.R;

import com.manacher.filesharing.sheets.ApkSheet;
import com.manacher.filesharing.sheets.AudioSheet;
import com.manacher.filesharing.sheets.FileSheet;
import com.manacher.filesharing.sheets.PhotosSheet;

public class SplashActivity extends BaseActivity {
    private final static int SPLASH_SCREEN_TIMER = 3_000;

    public static ApkSheet apkSheet;
    public static FileSheet fileSheet;
    public static PhotosSheet photosSheet;
    public static AudioSheet audioSheet;

    public static String WEB_LINK;

    public static int PORT = 8888;


    public static HomeActivity homeActivity;

    public static boolean networkStatus;
    public static NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        setFullScreen();
        hideNavigationBar();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_TIMER);
    }


    public static void setApkSheet(ApkSheet apkSheet) {
        SplashActivity.apkSheet = apkSheet;
    }

    public static void setFileSheet(FileSheet fileSheet) {
        SplashActivity.fileSheet = fileSheet;
    }

    public static void setPhotosSheet(PhotosSheet photosSheet) {
        SplashActivity.photosSheet = photosSheet;
    }

    public static void setAudioSheet(AudioSheet audioSheet) {
        SplashActivity.audioSheet = audioSheet;
    }


    public static void setHomeActivity(HomeActivity homeActivity) {
        SplashActivity.homeActivity = homeActivity;
    }

    public static ApkSheet getApkSheet() {
        return apkSheet;
    }

    public static FileSheet getFileSheet() {
        return fileSheet;
    }

    public static PhotosSheet getPhotosSheet() {
        return photosSheet;
    }

    public static AudioSheet getAudioSheet() {
        return audioSheet;
    }

    public static HomeActivity getHomeActivity() {
        return homeActivity;
    }

    public static boolean isNetworkStatus() {
        return networkStatus;
    }

    public static void setNetworkStatus(boolean networkStatus) {
        SplashActivity.networkStatus = networkStatus;
    }
}
