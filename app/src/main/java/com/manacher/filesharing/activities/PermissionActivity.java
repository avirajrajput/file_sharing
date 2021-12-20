package com.manacher.filesharing.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.manacher.filesharing.R;

public class PermissionActivity extends BaseActivity {
    private Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView title = findViewById(R.id.title);
        TextView dis = findViewById(R.id.dis);
        TextView turnOn = findViewById(R.id.turnOn);

        turnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startInstalledAppDetailsActivity();
            }
        });

        String sTitle = getIntent().getStringExtra("title");
        String sDis = getIntent().getStringExtra("dis");

        if (sTitle != null && sDis != null){
            title.setText(sTitle);
            dis.setText(sDis);
        }

    }

    public void startInstalledAppDetailsActivity() {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }
}