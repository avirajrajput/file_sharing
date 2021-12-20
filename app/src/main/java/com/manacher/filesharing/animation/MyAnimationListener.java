package com.rajput.aviraj.animation;

import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;

public class MyAnimationListener implements Animation.AnimationListener {
    ImageView imageView;

    public MyAnimationListener(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        imageView.clearAnimation();
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(imageView.getWidth(), imageView.getHeight());
        Log.d("ANI89", "onAnimationEnd: 1");

        lp.setMargins(50, 100, 0, 0);
        Log.d("ANI89", "onAnimationEnd: 2");

        imageView.setLayoutParams(lp);
        Log.d("ANI89", "onAnimationEnd: 3");

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }
}