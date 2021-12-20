package com.manacher.filesharing.services;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.TranslateAnimation;

import static android.view.View.VISIBLE;

public class AnimationService {

    public void viewGoneAnimator(final View view, int timeDuration) {

        view.animate()
                .alpha(0f)
                .setDuration(timeDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });

    }

    public void viewVisibleAnimator(final View view, int timeDuration) {

        view.animate()
                .alpha(1f)
                .setDuration(timeDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        view.setVisibility(VISIBLE);
                    }
                });

    }

    public void viewInvisibleAnimator(final View view, int timeDuration) {

        view.animate()
                .alpha(0f)
                .setDuration(timeDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.INVISIBLE);
                    }
                });

    }

    public void viewPosition(View view, int fromX, int toX, int fromY, int toY, int timeDuration){
        TranslateAnimation animation = new TranslateAnimation(fromX, toX, fromY, toY);
        animation.setDuration(timeDuration);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }
}
