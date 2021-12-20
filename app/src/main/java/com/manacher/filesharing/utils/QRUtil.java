package com.manacher.filesharing.utils;

import static android.content.Context.WINDOW_SERVICE;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.manacher.filesharing.R;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRUtil {
    private Activity activity;

    public QRUtil(Activity activity){
        this.activity = activity;
    }

    public Bitmap createQR(String data){
        QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, getSmallerDimension());
        qrgEncoder.setColorBlack(R.color.google_blue);
        qrgEncoder.setColorWhite(Color.WHITE);
        return qrgEncoder.getBitmap();
    }

    private int getSmallerDimension(){
        WindowManager manager = (WindowManager) activity.getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        return smallerDimension;
    }
}
