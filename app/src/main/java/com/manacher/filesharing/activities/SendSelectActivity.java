package com.manacher.filesharing.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.manacher.filesharing.R;
import com.manacher.filesharing.adapters.BagListAdapter;
import com.manacher.filesharing.fragments.ApkFragment;
import com.manacher.filesharing.fragments.AudioFragment;
import com.manacher.filesharing.fragments.FileExplorerFragment;
import com.manacher.filesharing.fragments.PhotosFragment;
import com.manacher.filesharing.models.FileLength;
import com.manacher.filesharing.services.AdsServices;
import com.manacher.filesharing.services.GpsService;
import com.manacher.filesharing.utils.FileItem;

import java.util.ArrayList;

import static com.airbnb.lottie.L.TAG;


public class SendSelectActivity extends BaseActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")

    private Button continueButton;
    private WifiManager wifiManager;
    private Fragment currentFragment;
    private SendSelectActivity context = this;


    private ArrayList<String> fileNamesList = new ArrayList<>();
    private ArrayList<FileLength> fileLengthList = new ArrayList<>();
    private ArrayList<Uri> fileDataList = new ArrayList<>();

    private ArrayList<FileItem> bagList = new ArrayList<>();

    private BagListAdapter adapter2;

    private RecyclerView bagListView;

    private RelativeLayout collectorPlace;

    private GpsService gpsService;

    private Button backButton;

    private  static String TAG;

    private long backPressedTime;

    private ImageView wifiIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        TAG = getString(R.string.offline_tag);

        fileNamesList.clear();
        fileDataList.clear();
        fileLengthList.clear();
        bagList.clear();

        bagListView = findViewById(R.id.bag_listView);
        backButton = findViewById(R.id.backButton);
        wifiIcon = findViewById(R.id.wifiIcon);
        continueButton = findViewById(R.id.select_continue_button);
        collectorPlace = findViewById(R.id.container);
        gpsService = new GpsService(context);

//        new AdsServices().showFullScreen(this);

        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        checkWifi();

        wifiIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkWifi();
            }
        });

        adapter2 = new BagListAdapter(bagList,
                fileNamesList,
                fileLengthList,
                fileDataList,
                context,
                collectorPlace,
                TAG);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        bagListView.setLayoutManager(layoutManager);

        bagListView.setAdapter(adapter2);

        continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View view) {

                    if (!bagList.isEmpty()){
                        Intent intent = new Intent(SendSelectActivity.this, DiscoverPeersActivity.class);
                        intent.putStringArrayListExtra("file_name_list", fileNamesList);
                        intent.putParcelableArrayListExtra("file_length_list", fileLengthList);
                        intent.putParcelableArrayListExtra("file_data_list", fileDataList);
                        intent.putParcelableArrayListExtra("bag_list", bagList);

                        if (gpsService.isGpsEnable()){
                            startActivity(intent);

                        }else{
                            gpsService.displayPromptForEnablingGPS();

                        }
                    }else{
                        Toast.makeText(getBaseContext(), "Please select data to send", Toast.LENGTH_LONG).show();
                    }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        currentFragment = new ApkFragment(context, fileNamesList, fileLengthList, fileDataList, bagList, adapter2, collectorPlace, TAG);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place_select,
                    currentFragment).commit();
        }
    }

    private void checkWifi(){
        if(!wifiManager.isWifiEnabled()){
            gpsService.displayWifiSettings();

        }

        wifiToggle();
    }

    private void wifiToggle(){
        if(!wifiManager.isWifiEnabled()){//Wifi Off
            wifiIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_wifi_off));
            continueButton.setVisibility(View.GONE);
        }else{
            wifiIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_wifi_on));
            continueButton.setVisibility(View.VISIBLE);
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        wifiToggle();
    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomNavigationView bottomNav = findViewById(R.id.select_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_apk:{
                            if(!(currentFragment instanceof ApkFragment)) {
                                fragment = new ApkFragment(context, fileNamesList, fileLengthList, fileDataList, bagList, adapter2, collectorPlace, TAG);
                                addFragment(fragment);
                                currentFragment = fragment;
                            }
                            break;
                        }
                        case R.id.nav_file:
                            if(!(currentFragment instanceof FileExplorerFragment)) {
                                fragment = new FileExplorerFragment(context, bagList, adapter2, fileNamesList, fileLengthList, fileDataList, collectorPlace, TAG);
                                addFragment(fragment);
                                currentFragment = fragment;
                            }
                            break;
                        case R.id.nav_imageVideo:
                            if(!(currentFragment instanceof PhotosFragment)) {
                                fragment = new PhotosFragment(context, bagList, adapter2, fileNamesList, fileLengthList, fileDataList, collectorPlace, TAG);
                                addFragment(fragment);
                                currentFragment = fragment;
                            }
                            break;
                        case R.id.nav_audioFile:
                            if(!(currentFragment instanceof AudioFragment)) {
                                fragment = new AudioFragment(context, bagList, adapter2, fileNamesList, fileLengthList, fileDataList, collectorPlace, TAG);
                                addFragment(fragment);
                                currentFragment = fragment;
                            }
                            break;
                    }
                    return true;
                }
            };

    private void addFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place_select,
                fragment).commit();
    }


    @Override
    public void onBackPressed() {
        if (currentFragment instanceof FileExplorerFragment){
            if (backPressedTime + 500 > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                ((FileExplorerFragment)currentFragment).setBackButton();
            }
            backPressedTime = System.currentTimeMillis();
        }else{
            super.onBackPressed();
        }
    }

}