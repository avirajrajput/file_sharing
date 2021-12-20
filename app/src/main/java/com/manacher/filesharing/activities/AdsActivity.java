package com.manacher.filesharing.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdService;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.manacher.filesharing.R;
import com.manacher.filesharing.services.AdsServices;
import com.manacher.filesharing.utils.Routing;

public class AdsActivity extends BaseActivity {
    private Button start;
    private Routing routing;
    private AdView adView;

    private AdsServices adService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        start = findViewById(R.id.start);
        routing = new Routing(this);
        adService = new AdsServices();
        adView = findViewById(R.id.adView);
        TemplateView template = findViewById(R.id.my_template);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routing.navigate(HomeActivity.class, true);
            }
        });

        adService.loadBanner(adView);
        adService.loadNative(template, this);
    }
}