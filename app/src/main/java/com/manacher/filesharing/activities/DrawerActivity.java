package com.manacher.filesharing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.manacher.filesharing.R;
import com.manacher.filesharing.utils.Routing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class DrawerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    protected DrawerLayout drawer;
    protected ActionBarDrawerToggle toggle;
    protected NavigationView navigationView;
    protected View headerView;

    private Routing routing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        routing = new Routing(this);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        permission_fn();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        headerView = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

       if (id == R.id.nav_share) {
           sendInvitation();
       }

       else if(id == R.id.nav_about){

       }

       else if(id == R.id.nav_privacy){
           SplashActivity.WEB_LINK = "https://absmedia798.blogspot.com/2020/08/priacy-policy.html?m=1";
           routing.navigate(WebActivity.class, false);
        }

        return true;
    }

    public void sendInvitation() {

        try {

            String invitationLink = "https://play.google.com/store/apps/details?id=com.manacher.filesharing";
            Intent shareIntent =
                    new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey I am using this amazing app: "+getString(R.string.app_name));
            String shareMessage =
                    "Hey I am using this amazing app: \n\nInstall "+getString(R.string.app_name)+" app from store.\n\nThis application provides fastest data sharing.\n\n"
                            + invitationLink;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));


        } catch(Exception e) {
            //e.toString();

        }
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


//    private void navigateToPrimeActivity(){
//        Intent intent = new Intent(MainActivity.this, PrimeActivity.class);
//        intent.putExtra("fromHome",true);
//        startActivity(intent);
//        finish();
//    }

//    private void navigateToSoftPuranaDashBoard(String type){
//        Intent intent = new Intent(MainActivity.this, SoftPuranaDashboardActivity.class);
//        intent.putExtra("type", type);
//        startActivity(intent);
//        //finish();
//    }
////    private void navigateToHardCopyDashBoard(){
////        Intent intent = new Intent(MainActivity.this, HardCopyBookDashboardActivity.class);
////        intent.putExtra("fromHome", false);
////        startActivity(intent);
////       //finish();
////    }
//
//    private void navDonationActivity(){
//        Intent intent = new Intent(MainActivity.this, DonationActivity.class);
//        startActivity(intent);
//    }
//
//    private void permission_fn(){
//
//        if((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
//
//            boolean_permission = false;
//
//            if((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))){
//
//            }else{
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION );
//            }
//
//        }else{
//
//            boolean_permission = true;
//        }
//        if((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
//
//            boolean_permission = false;
//
//            if((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))){
//
//            }else{
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION );
//            }
//
//        }else{
//
//            boolean_permission = true;
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if(requestCode == REQUEST_PERMISSION){
//
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                boolean_permission = true;
//            }
//            else{
//
//                Toast.makeText(MainActivity.this, "Please grant permission", Toast.LENGTH_LONG).show();
//            }
//        }
//    }


//    public void sendInvitation() {
//
//        try {
//
//            String invitationLink = SplashActivity.APP_INFO.getApplicationUrl();
//            Intent shareIntent =
//                    new Intent(Intent.ACTION_SEND);
//            shareIntent.setType("text/plain");
//
//            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey I am using this amazing app: "+getString(R.string.app_name));
//            String shareMessage =
//                    "Hey I am using this amazing app: \n\nInstall "+getString(R.string.app_name)+" app from store and login.\n\nThis app provides a huge library of Islamic books at one place.\n\nThis is very portable to use, you can read any book anytime at one place.\n\n\n"
//                    + invitationLink;
//            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
//            startActivity(Intent.createChooser(shareIntent, "choose one"));
//
//
//        } catch(Exception e) {
//            //e.toString();
//            new FileUtils(MainActivity.this).showShortToast("Something went wrong try again later!");
//        }
//    }
}