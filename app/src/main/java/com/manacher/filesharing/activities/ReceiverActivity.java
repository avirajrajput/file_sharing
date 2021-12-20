package com.manacher.filesharing.activities;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.airbnb.lottie.LottieAnimationView;
import com.manacher.filesharing.R;
import com.manacher.filesharing.adapters.TransferFilesListAdapter;
import com.manacher.filesharing.dialogs.AskQusDialog;
import com.manacher.filesharing.dialogs.QRDialog;
import com.manacher.filesharing.dialogs.ScanDialog;
import com.manacher.filesharing.services.AdsServices;
import com.manacher.filesharing.services.GpsService;
import com.manacher.filesharing.utils.FileItem;
import com.manacher.filesharing.utils.ReceiverWifiDirectBroadcast;
import com.manacher.filesharing.utils.Util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ReceiverActivity extends BaseActivity
        implements AskQusDialog.AskDisconnectedDialogListener {

    private ReceiverActivity context = this;
    protected TextView connectionStatus;

    private WifiManager wifiManager;

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel wifiChannel;

    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;
    private List<WifiP2pDevice> peers = new ArrayList<>();
    private String[] deviceNameArray;
    private WifiP2pDevice[] deviceArray;

    private ServerClass serverClass;
    private ClientClass clientClass;
    private SendReceive sendReceive;
    private static final int BUFFER = 20480;

    private ArrayList<String> fileNamesList = new ArrayList<>();
    private ArrayList<Long> fileLengthList = new ArrayList<>();
    private ArrayList<FileItem> bagList = new ArrayList<>();

    private ListView receiveListView;
    private Button backButton;
    private LottieAnimationView wifiRadar;
    private RelativeLayout itemPlace;
    private TextView doneButton;
    private TransferFilesListAdapter adapter;
    private ProgressBar progressbar;
    private AskQusDialog askDisconnectCard;
    private GpsService gpsService;
    private CardView qrButton;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
        setCurrentActivity(this);
        startActivity();

        if(!wifiManager.isWifiEnabled()){
            gpsService.displayWifiSettings();
        }
//        new AdsServices().showFullScreen(this);
    }

    private void startActivity(){
        this.initialized();

        this.receiveListView.setAdapter(adapter);

        this.checkWifiOnOff();

        this.discoverWifi();

        this.listener();
    }

    private void listener(){
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("fromReceiver", true);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askDisconnectCard= new AskQusDialog("Do want to disconnect ?");
                askDisconnectCard.setCancelable(false);
                askDisconnectCard.show(getSupportFragmentManager(), "disconnecting dialog");
            }
        });

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SamSung: d6:8a:39:98:8b:c0
                String temp = "3a:e6:0a:df:e4:54";
                QRDialog qrCard = new QRDialog(temp);
                qrCard.show(fragmentManager, "scan dialog");

            }
        });
    }

    @SuppressLint("HardwareIds")
    private void initialized(){
        gpsService = new GpsService(this);
        doneButton = findViewById(R.id.done_button);
        progressbar = findViewById(R.id.progress_complete);
        qrButton = findViewById(R.id.qrButton);

        receiveListView = findViewById(R.id.receiveListView);

        fragmentManager = getSupportFragmentManager();

        adapter = new TransferFilesListAdapter(context, bagList, context.getString(R.string.offline_tag), context.getString(R.string.receiver_tag));

        wifiRadar = findViewById(R.id.animationView);
        itemPlace = findViewById(R.id.itemPlace_r);

        backButton = findViewById(R.id.backButton);

        connectionStatus = findViewById(R.id.rStatus);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);

        wifiChannel =  wifiP2pManager.initialize(context, getMainLooper(), null);

        receiver = new ReceiverWifiDirectBroadcast(wifiP2pManager, wifiChannel, context);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        Log.d("PP99", "initialized: "+Util.getMAC());

    }

    public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peersList) {
            if (!peersList.getDeviceList().equals(peers)){
                peers.clear();
                peers.addAll(peersList.getDeviceList());

                deviceNameArray = new String[peersList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peersList.getDeviceList().size()];

                int index = 0;
                for (WifiP2pDevice device : peersList.getDeviceList()){
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }
            }

        }
    };

    public WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;

            if (wifiP2pInfo.isGroupOwner && wifiP2pInfo.groupFormed){

                connectionStatus.setText("Receiver");
                serverClass = new ServerClass();
                serverClass.start();
            }
            else {

                connectionStatus.setText("Receiver");
                clientClass = new ClientClass(groupOwnerAddress);
                clientClass.start();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(receiver, intentFilter);

        this.discoverWifi();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @SuppressLint("StaticFieldLeak")
    public class SendReceive extends AsyncTask<Void, Void, Void> {

        private Socket socket;
        private ServerSocket serverSocket;
        public SendReceive(Socket socket, ServerSocket serverSocket) {// from ServerClass
            this.socket = socket;
            this.serverSocket = serverSocket;
            connectStatus();
        }

        public SendReceive(Socket socket) {// from ClientClass
            this.socket = socket;
            this.serverSocket = null;
            connectStatus();
        }


        @SuppressLint("UseCompatLoadingForDrawables")
        private void read(){
            try {


                InputStream inputStream = socket.getInputStream();// InputStream
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);// ObjectInputStream

                //  We get size of items to be receive
                int sizeOfItems = objectInputStream.readInt();

                //  Get filenames and size for know to read file
                fileNamesList = (ArrayList<String>) objectInputStream.readObject();// File Name List
                fileLengthList = (ArrayList<Long>) objectInputStream.readObject();// File Length List
                setBagList();

                double totalDataSize = Util.getSum(fileLengthList);
                double CompleteProgress = 0;

                for (int i = 0; i < bagList.size(); i++) {
                    String fileName = fileNamesList.get(i);
                    Long fileSize = fileLengthList.get(i);


                        File file = new File(Environment.getExternalStorageDirectory()
                                 + "/FileSharing/Offline/" + fileName);

                        File dir = file.getParentFile();


                                if (!dir.exists())
                                    dir.mkdirs();

                                if (file.exists())
                                    file.delete();

                                        if (file.createNewFile()) {
                                            Log.d("Receiver", "File Created");

                                        } else Log.d("Receiver", "File Not Created");

                                        OutputStream outputStream = new FileOutputStream(file);
                                        byte[] buf = new byte[BUFFER];
                                        int read_bytes;
                                        try {
                                            float total = 0;
                                                while (fileSize > 0 && (read_bytes = inputStream.read(buf, 0, (int) Math.min(buf.length, fileSize))) != -1) {

                                                    total += read_bytes;
                                                    int value = (int)((total * 100) / bagList.get(i).getFileSize());
                                                    bagList.get(i).setProgress(value);

                                                    CompleteProgress += read_bytes;
                                                    int valueComplete = (int) ((CompleteProgress * 100) / totalDataSize);
                                                    progressbar.setProgress(valueComplete);

                                                    outputStream.write(buf, 0, read_bytes);
                                                    onProgressUpdate();
                                                    fileSize -= read_bytes;
                                                }

                                            bagList.get(i).setDone(context.getResources().getDrawable(R.drawable.done_icon_blue));
                                            notifyList();

                                            outputStream.flush();
                                            outputStream.close();

                                            } catch (IOException e) {}
                }

                objectInputStream.close();
                socket.close();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            finally {
                try {

                    if (socket != null){
                        socket.close();

                    }

                    if (serverSocket != null){
                        serverSocket.close();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        @Override
        protected Void doInBackground(Void... voids) {
            if (socket !=  null){
                read();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            notifyList();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Sender", "Finished!");
            doneStatus();

        }

    }
    private void notifyList(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                adapter.notifyDataSetChanged();

            }
        });
    }
    private void doneStatus(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // Stuff that updates the UI
                fileNamesList.clear();
                fileLengthList.clear();

                doneButton.setVisibility(View.VISIBLE);

            }
        });
    }

    private void connectStatus() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                connectionStatus.setText("Connected");

                // Stuff that updates the UI
                wifiRadar.setVisibility(View.GONE);
                itemPlace.setVisibility(View.VISIBLE);

            }
        });
    }

    private void setBagList(){
        for (int i = 0; i < fileNamesList.size(); i++){
            bagList.add(new FileItem(fileNamesList.get(i), getListIcon(fileNamesList.get(i)), "",fileLengthList.get(i)));
        }
        notifyList();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getListIcon(String name){

            if (name.toLowerCase().contains("pdf")){

                return  context.getResources().getDrawable(R.drawable.pdf);


            }else if (name.toLowerCase().contains("text") || name.toLowerCase().contains("txt")){


                return context.getResources().getDrawable(R.drawable.txt);


            }else if (name.toLowerCase().contains("mp3")){


                return context.getResources().getDrawable(R.drawable.mp3);


            }else if (name.toLowerCase().contains("jpeg") || name.toLowerCase().contains("jpg")){

                return context.getResources().getDrawable(R.drawable.jpg);

            }else if (name.toLowerCase().contains("png")){

                return context.getResources().getDrawable(R.drawable.png);

            }else if (name.toLowerCase().contains("mp4")){

                return context.getResources().getDrawable(R.drawable.video_file);

            }else if (name.toLowerCase().contains("apk")){

                return context.getResources().getDrawable(R.drawable.apk);

            }else{
                return context.getResources().getDrawable(R.drawable.file_icon2);
            }
    }

    public class ServerClass extends Thread {

        public ServerClass() {
        }
        @Override
        public void run() {

            try {

                ServerSocket serverSocket = new ServerSocket(SplashActivity.PORT);
                Socket socket = serverSocket.accept();

                sendReceive = new SendReceive(socket, serverSocket);
                sendReceive.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public class ClientClass extends Thread {

        private Socket socket;
        private String hostAdd;
        public ClientClass(InetAddress inetAddress) {
            hostAdd = inetAddress.getHostAddress();
            this.socket = new Socket();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        @Override
        public void run() {
            try {


                socket.bind(null);
                socket.connect(new InetSocketAddress(hostAdd, SplashActivity.PORT), 5_000);
                sendReceive = new SendReceive(socket);
                sendReceive.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private void checkWifiOnOff(){
        if (!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
    }

    private void discoverWifi() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        wifiP2pManager.discoverPeers(wifiChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                connectionStatus.setText("Searching...");
            }

            @Override
            public void onFailure(int i) {
                connectionStatus.setText("please try again");

            }
        });
    }

    @Override
    public void onBackPressed() {
        askDisconnectCard= new AskQusDialog("Do you want to disconnect ?");
        askDisconnectCard.setCancelable(false);
        askDisconnectCard.show(getSupportFragmentManager(), "disconnecting dialog");

    }

    @Override
    public void AskQusDialogButton(Button yesButton, Button noButton) {
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askDisconnectCard.dismiss();
                context.finish();

            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askDisconnectCard.dismiss();
            }
        });
    }
}