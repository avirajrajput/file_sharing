package com.manacher.filesharing.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.manacher.filesharing.activities.ReceiverActivity;

public class ReceiverWifiDirectBroadcast extends BroadcastReceiver {
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel wifiChannel;
    private ReceiverActivity rContext;

    public ReceiverWifiDirectBroadcast(WifiP2pManager wifiP2pManager,
                                       WifiP2pManager.Channel wifiChannel,
                                       ReceiverActivity context) {

        this.wifiP2pManager = wifiP2pManager;
        this.wifiChannel = wifiChannel;
        this.rContext = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onReceive(Context context, Intent intent) {


        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(context, "WIFI IS ON", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "WIFI IS OFF", Toast.LENGTH_SHORT).show();
            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            if (wifiP2pManager != null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                wifiP2pManager.requestPeers(wifiChannel, rContext.peerListListener);
            }
        }else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){

            if (wifiP2pManager == null){
                return;
            }

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            assert networkInfo != null;
            if (networkInfo.isConnected()){
                wifiP2pManager.requestConnectionInfo(wifiChannel, rContext.connectionInfoListener);

            }else{
//                rContext.connectionStatus.setText("Disconnected");
            }

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
                WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                String thisDeviceName = device.deviceAddress;

                Log.d("PP99", "onReceive: "+thisDeviceName);
              }
    }
}
