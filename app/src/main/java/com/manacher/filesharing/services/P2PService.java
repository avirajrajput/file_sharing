package com.manacher.filesharing.services;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

import com.manacher.filesharing.activities.DiscoverPeersActivity;

public class P2PService {

    private WifiManager wifiManager;

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel wifiChannel;

    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;
    private String[] deviceNameArray;
    private WifiP2pDevice[] deviceArray;

    private DiscoverPeersActivity.SendReceive sendReceive;

    public P2PService(Activity activity){

        wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiP2pManager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
        wifiChannel = wifiP2pManager.initialize(activity, activity.getMainLooper(), null);

//        receiver = new WifiDirectBroadcast(wifiP2pManager, wifiChannel, activity);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }
}
