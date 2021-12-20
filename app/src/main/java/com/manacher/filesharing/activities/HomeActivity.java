package com.manacher.filesharing.activities;

import   android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.manacher.filesharing.R;
import com.manacher.filesharing.dialogs.AskQusDialog;
import com.manacher.filesharing.fragments.FilesFragment;
import com.manacher.filesharing.fragments.NoPermissionFragment;
import com.manacher.filesharing.fragments.OnlineFragment;
import com.manacher.filesharing.services.AdsServices;
import com.manacher.filesharing.services.AnimationService;
import com.manacher.filesharing.services.PermissionsServices;
import com.manacher.filesharing.utils.FilesManager;
import com.manacher.filesharing.utils.Routing;

public class HomeActivity extends DrawerActivity implements AskQusDialog.AskDisconnectedDialogListener{
    private HomeActivity context = this;
    private Fragment currentFragment;
    private PermissionsServices permissionsServices;
    private FilesManager filesManager;
    private LinearLayout logo;
    private AnimationService animationService;
    private View toolBarUnderLine;
    private BottomNavigationView bottomNav;
    private Routing routing;
    public String transferActivity;

    private Toolbar toolbar;
    private boolean fromReceive;
    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_home);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_home, null, false);
        drawer.addView(contentView, 0);

        SplashActivity.setHomeActivity(this);

        fromReceive = getIntent().getBooleanExtra("fromReceiver", false);

        this.initialization();

        if (fromReceive){
            currentFragment = new FilesFragment(context);

        }else{
            currentFragment = new OnlineFragment();

        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place,
                    currentFragment).commit();
        }
    }

    @SuppressLint("WrongViewCast")
    private void initialization(){

        bottomNav = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.toolbar);
        permissionsServices = new PermissionsServices(this);
        filesManager = new FilesManager();
        animationService = new AnimationService();
        logo = findViewById(R.id.logo);
        toolBarUnderLine = findViewById(R.id.toolbar_underLine);
        routing = new Routing(context);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here

            }


            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here

            }
        };

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));

        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }


    private void logoAnimate(){
        animationService.viewPosition(logo, 0, 0, -120, 0, 1000);
        animationService.viewVisibleAnimator(toolBarUnderLine, 1000);

    }




    @Override
    protected void onStart() {
        super.onStart();
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (context.fromReceive){
            bottomNav.getMenu().getItem(1).setChecked(true);

        }else{
            bottomNav.getMenu().getItem(0).setChecked(true);

        }



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.nav_home:{
                            openHomeFragment();
                            break;
                        }

                        case R.id.nav_files:
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                if (Environment.isExternalStorageManager()) {
                                    setFilesFragment();
                                } else {
                                    permissionsServices.askForAllStorage();
                                }
                            }else{
                                setFilesFragment();
                            }


                            break;

                    }
                    return true;
                }
            };

    private void setFilesFragment(){
        if(permissionsServices.isAlreadyGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
            if (permissionsServices.isAlreadyGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                openFilesFragment();

            }else{
                transferActivity = "fragment";
                permissionsServices.askForWritePermission();
            }

        }else {
            transferActivity = "fragment";
            permissionsServices.askForReadPermission();

        }
    }

    private void openHomeFragment(){
        Fragment fragment = null;
        if(!(currentFragment instanceof OnlineFragment)) {
            fragment = new OnlineFragment();
            addFragment(fragment);
            currentFragment = fragment;
        }
    }


    private void openFilesFragment(){
        Fragment fragment = null;
        if(!(currentFragment instanceof FilesFragment)) {
            fragment = new FilesFragment(context);
            addFragment(fragment);

            currentFragment = fragment;
        }
    }

    private void openNoPermissionFragment(){
        Fragment fragment = null;

            fragment = new NoPermissionFragment(context);
            addFragment(fragment);
            currentFragment = fragment;

    }

    public void addFragment(Fragment fragment){
        context.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place,
                fragment).commit();
    }
    public void addFragment(Fragment fragment, String tag){
        context.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place,
                fragment, tag).commit();
    }
    public Fragment getFragmentByTag(String tag){
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    private void createDirectory(){
        String folder_main = "FileSharing";
        filesManager.createDirectory(Environment.getExternalStorageDirectory(), folder_main);

        String folder_parent = Environment.getExternalStorageDirectory() +"/" + folder_main;

        String folder_online = "/Online";
        filesManager.createDirectory(folder_parent, folder_online);

        String folder_offline = "/Offline";
        filesManager.createDirectory(folder_parent, folder_offline);
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
                    if (transferActivity.equals(getString(R.string.online))){
                        if (permissionsServices.isAlreadyGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//                            routing.navigate(SelectOnlineActivity.class, false);

                        }else{
                            permissionsServices.askForWritePermission();
                        }

                    }else if (transferActivity.equals(getString(R.string.sender_tag))){
                            if (permissionsServices.isAlreadyGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                                if(permissionsServices.isAlreadyGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
                                    routing.navigate(SendSelectActivity.class, false);

                                }else{
                                    permissionsServices.askForAccessFineLocationPermission();

                            }

                        }else{
                            permissionsServices.askForWritePermission();
                        }


                    }else if (transferActivity.equals("fragment")){
                        openFilesFragment();

                    }else if (transferActivity.equals(getString(R.string.receiver_tag))){
                        if (permissionsServices.isAlreadyGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            if(permissionsServices.isAlreadyGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
                                routing.navigate(ReceiverActivity.class, false);

                            }else{
                                permissionsServices.askForAccessFineLocationPermission();

                            }

                        }else{
                            permissionsServices.askForWritePermission();
                        }


                    }

                } else {
//                    openNoPermissionFragment("NO Read Permission", "read_permission");
                    Intent intent = new Intent(context, PermissionActivity.class);
                    intent.putExtra("title", getString(R.string.storage_tile));
                    intent.putExtra("dis", getString(R.string.storage_dis));
                    startActivity(intent);
                }

                break;
            }

            case PermissionsServices.WRITE_PERMISSION_CODE: {

                if (grantResults.length > 0 && PermissionsServices.isAlreadyGranted(grantResults[0])) {
                    createDirectory();

                    if (transferActivity.equals(getString(R.string.online))){
//                        routing.navigate(SelectOnlineActivity.class, false);

                    }else if (transferActivity.equals(getString(R.string.sender_tag))){
                        if(permissionsServices.isAlreadyGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
                            routing.navigate(SendSelectActivity.class, false);

                        }else{
                            permissionsServices.askForAccessFineLocationPermission();
                        }


                    }else if (transferActivity.equals("fragment")){
                        openFilesFragment();

                    }else if (transferActivity.equals(getString(R.string.receiver_tag))){
                            if(permissionsServices.isAlreadyGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
                                routing.navigate(ReceiverActivity.class, false);

                            }else{
                                permissionsServices.askForAccessFineLocationPermission();
                            }
                    }

                    } else {
//                        openNoPermissionFragment();
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
                    if (transferActivity.equals(getString(R.string.sender_tag))){
                        routing.navigate(SendSelectActivity.class, false);

                    }else if (transferActivity.equals(getString(R.string.receiver_tag))){
                        routing.navigate(ReceiverActivity.class, false);

                    }

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


    private AskQusDialog askDisconnectCard;

    @Override
    public void onBackPressed() {
//        if (currentFragment instanceof OnlineFragment){
////            if(((OnlineFragment)currentFragment).getSooLayout().isShown()){
//////                ((OnlineFragment)currentFragment).getSooLayout().setVisibility(View.GONE);
//////                ((OnlineFragment)currentFragment).getCloseButton().setVisibility(View.GONE);
//////                return;
//////            }
//        }
//
//
//        if (backPressedTime + 2_000 > System.currentTimeMillis()) {
//            backToast.cancel();
////            context.finish();
//
//            finishAffinity();
//            return;
//        } else {
//            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
//            backToast.show();
//        }
//        backPressedTime = System.currentTimeMillis();
        askDisconnectCard= new AskQusDialog("Are you sure to exit ?");
        askDisconnectCard.setCancelable(false);
        askDisconnectCard.show(getSupportFragmentManager(), "disconnecting dialog");

        new AdsServices().showFullScreen(this);
    }

    @Override
    public void AskQusDialogButton(Button yesButton, Button noButton) {
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askDisconnectCard.dismiss();
            }
        });
    }

}