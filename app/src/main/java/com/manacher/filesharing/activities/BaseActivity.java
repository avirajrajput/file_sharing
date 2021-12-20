package com.manacher.filesharing.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;

import android.os.Build;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.manacher.filesharing.R;
import com.manacher.filesharing.utils.Routing;



public class BaseActivity extends AppCompatActivity{
    protected View view;
    protected Routing routing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initialization();
    }
    protected void setCurrentActivity(Activity activity){
        routing = new Routing(activity);

    }

    private void initialization() {
        try{

            view = getWindow().getDecorView();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
            setStatusBarColor(R.color.google_blue);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void setStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, color));
        }
    }

    protected void setNavigationBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, color));
        }

    }

    protected void setStatusBarLight(boolean on){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (on) {
                view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


            } else {
                view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            }
        }
    }

    protected void setNavigationBarLight(boolean on){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (on) {
                view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

            } else {
                view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

            }
        }
    }

    protected boolean checkInternetConnection(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    protected void showNoConnection(){
        Snackbar.make(view, "No Connection", Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.google_red))
                .show();
    }

    protected void showSnack(String message){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.google_yellow))
                .show();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                boolean noConnectivity = intent.getBooleanExtra(
                        ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
                );

                if (noConnectivity) {
                    hideNavigationBar();

                        SplashActivity.setNetworkStatus(true);

                } else {
                    if (SplashActivity.isNetworkStatus()){

                        SplashActivity.setNetworkStatus(false);
                    }
                }
            }
        }
    };

    protected void hideNavigationBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {

           int width = image.getWidth();
           int height = image.getHeight();

           float bitmapRatio = (float)width / (float) height;
           if (bitmapRatio > 1) {
               width = maxSize;
               height = (int) (width / bitmapRatio);
           } else {
               height = maxSize;
               width = (int) (height * bitmapRatio);
           }
           return Bitmap.createScaledBitmap(image, width, height, true);

    }

    public void setFullScreen(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            View view = getCurrentFocus();
            if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
                int[] scrcoords = new int[2];
                view.getLocationOnScreen(scrcoords);
                float x = ev.getRawX() + view.getLeft() - scrcoords[0];
                float y = ev.getRawY() + view.getTop() - scrcoords[1];
                if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                    ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
            }
            return super.dispatchTouchEvent(ev);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}