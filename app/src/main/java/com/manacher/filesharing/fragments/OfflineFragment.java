package com.manacher.filesharing.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.manacher.filesharing.R;
import com.manacher.filesharing.activities.HomeActivity;
import com.manacher.filesharing.activities.ReceiverActivity;
import com.manacher.filesharing.activities.SendSelectActivity;
import com.manacher.filesharing.services.AnimationService;


public class OfflineFragment extends Fragment {
    private LinearLayout sendButton;
    private LinearLayout receiveButton;
    private Activity context;
    private AnimationService animationService;

    public OfflineFragment() {
    }

    public OfflineFragment(Activity context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_offline, container, false);

        this.initialized(rootView);

        animationService.viewPosition(sendButton, 0, 0, 50, 0, 2_000);
        animationService.viewPosition(receiveButton, 0, 0, -50, 0, 2_000);

        this.clickListener();

        return rootView;
    }

    private void initialized(View view){
        sendButton = view.findViewById(R.id.send_layout);
        receiveButton = view.findViewById(R.id.receive_layout);
        animationService = new AnimationService();
    }

    private void clickListener(){

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SendSelectActivity.class);
                context.startActivity(intent);
            }
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((HomeActivity)context, ReceiverActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
