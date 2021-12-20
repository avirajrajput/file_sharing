package com.manacher.filesharing.activities;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;

import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.manacher.filesharing.R;
import com.manacher.filesharing.adapters.DeviceListAdapter;
import com.manacher.filesharing.adapters.TransferFilesListAdapter;
import com.manacher.filesharing.dialogs.AcceptDialog;
import com.manacher.filesharing.dialogs.AskQusDialog;
import com.manacher.filesharing.dialogs.ScanDialog;
import com.manacher.filesharing.models.FileLength;
import com.manacher.filesharing.services.AnimationService;
import com.manacher.filesharing.utils.FileItem;
import com.manacher.filesharing.utils.Util;
import com.manacher.filesharing.utils.WifiDirectBroadcast;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class DiscoverPeersActivity extends BaseActivity
        implements AcceptDialog.AcceptDialogListener, AskQusDialog.AskDisconnectedDialogListener, ScanDialog.ScanListener {
    private ListView listView;
    private ListView bagListView;

    private DiscoverPeersActivity context = this;
    protected TextView connectionStatus;

    private WifiManager wifiManager;

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel wifiChannel;

    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;
    private WifiP2pDevice[] deviceArray;

    private SendReceive sendReceive;

    private TextView sendButton;

    private AskQusDialog askDisconnectCard;

    private List<WifiP2pDevice> peers = new ArrayList<>();
    private ArrayList<String> fileNamesList = new ArrayList<>();
    private ArrayList<Uri> fileDataList = new ArrayList<>();
    private ArrayList<Long> fileLengthList = new ArrayList<>();

    private ArrayList<FileItem> bagList = new ArrayList<>();

    private Button backButton;
    private ProgressBar progressbar;

    private ProgressBar discoverProgressBar;
    private TextView doneButton;

    private TransferFilesListAdapter adapter;
    private WifiP2pConfig config;

    private static final int BUFFER = 20480;

    private AnimationService animationService;

    private LottieAnimationView wifiRadar;

    private FragmentManager fragmentManager;

    private AcceptDialog acceptDialog;

    private CardView scanButton;
    private ScanDialog scanCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        setCurrentActivity(this);

        startActivity();

    }

    private void startActivity(){
        ArrayList<FileLength> fileLengthListPar = getIntent().getParcelableArrayListExtra("file_length_list");
        fileNamesList = getIntent().getStringArrayListExtra("file_name_list");
        fileDataList = getIntent().getParcelableArrayListExtra("file_data_list");

        bagList = getIntent().getParcelableArrayListExtra("bag_list");
        setListIcon();

        if(fileLengthListPar != null){
            Util.copyList(fileLengthListPar, fileLengthList);

        }

        this.initialized();
        this.checkWifiOnOff();
        this.discoverWifi();
        this.clickListener();
    }

    public WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;

            if (wifiP2pInfo.isGroupOwner && wifiP2pInfo.groupFormed) {//Host
                connectionStatus.setText("Sender");
                ServerClass serverClass = new ServerClass();
                serverClass.start();
            }
            else if (wifiP2pInfo.groupFormed){//Client
                connectionStatus.setText("Sender");
                ClientClass clientClass = new ClientClass(groupOwnerAddress);
                clientClass.start();
            }
        }
    };

    private void connectWithDevice(String deviceAddress, String deviceName){
        config = new WifiP2pConfig();
        config.deviceAddress = deviceAddress;

        Log.d("HHSG88", "connectWithDevice: "+deviceAddress);

        config.groupOwnerIntent = 15;

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        wifiP2pManager.connect(wifiChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                connectionStatus.setText(deviceName);

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(int i) {
                Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_SHORT).show();
                connectionStatus.setText("connection fail");
                disconnect();

            }
        });
    }

    private void clickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final WifiP2pDevice device = deviceArray[i];
                connectWithDevice(device.deviceAddress, device.deviceName);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReceive.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                sendButton.setVisibility(View.GONE);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askDisconnectCard= new AskQusDialog("Do want to disconnect ?");
                askDisconnectCard.setCancelable(false);
                askDisconnectCard.show(fragmentManager, "disconnecting dialog");
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnect();
                Intent intent = new Intent(context, HomeActivity.class);
                startActivity(intent);

            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scanCard = new ScanDialog();
                scanCard.show(fragmentManager, "scan dialog");


            }
        });
    }

    private void initialized() {
        listView = findViewById(R.id.list_view);
        scanButton = findViewById(R.id.scanButton);
        fragmentManager = getSupportFragmentManager();

        wifiRadar = findViewById(R.id.animationView);
        animationService = new AnimationService();

        sendButton = findViewById(R.id.send_item_button);
        doneButton = findViewById(R.id.done_button);

        listView.setVisibility(View.VISIBLE);

        discoverProgressBar = findViewById(R.id.discover_progressBar);

        progressbar = findViewById(R.id.progress_complete);

        backButton = findViewById(R.id.backButton);

        bagListView = findViewById(R.id.list_view_bagList);
        adapter = new TransferFilesListAdapter(context, bagList, context.getString(R.string.offline_tag), context.getString(R.string.sender_tag));
        bagListView.setAdapter(adapter);

        connectionStatus = findViewById(R.id.statusText);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);

        wifiChannel = wifiP2pManager.initialize(context, getMainLooper(), null);

        receiver = new WifiDirectBroadcast(wifiP2pManager, wifiChannel, context);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

    }

    public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peersList) {

            if (!peersList.getDeviceList().equals(peers)) {

                peers.clear();
                peers.addAll(peersList.getDeviceList());

                String[] deviceNameArray = new String[peersList.getDeviceList().size()];

                deviceArray = new WifiP2pDevice[peersList.getDeviceList().size()];

                int index = 0;
                for (WifiP2pDevice device : peersList.getDeviceList()) {
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }

                DeviceListAdapter deviceListAdapter = new DeviceListAdapter(context, deviceNameArray);
                listView.setAdapter(deviceListAdapter);

            }

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void ScanCardListener(String data) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                connectWithDevice(data, "Android");
                scanCard.dismiss();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class SendReceive extends AsyncTask<Void, Void, Void> {

        private Socket socket;
        private ServerSocket serverSocket;

        public SendReceive(Socket socket, ServerSocket serverSocket) {// from ServerClass
            this.socket = socket;
            this.serverSocket = serverSocket;
            setConnectionCard();
            connectStatus();

        }
        public SendReceive(Socket socket) {//from ClientClass
            this.socket = socket;
            this.serverSocket = null;
            setConnectionCard();
            connectStatus();

        }

        @SuppressLint("UseCompatLoadingForDrawables")
        private void sendData() {
            try {
                InputStream inputStream;
                OutputStream outputStream = socket.getOutputStream();// OutPutStream
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);// ObjectOutputStream

                ContentResolver cr = context.getContentResolver();

                // Load data in objectOutputStream
                objectOutputStream.writeInt(fileDataList.size());

                objectOutputStream.writeObject(fileNamesList);
                objectOutputStream.flush();

                objectOutputStream.writeObject(fileLengthList);
                objectOutputStream.flush();

                double totalDataSize = getSum(fileLengthList);
                double completeProgress = 0;

                for (int i = 0; i < bagList.size(); i++) {

                    inputStream = cr.openInputStream(fileDataList.get(i));

                        if (inputStream != null) {

                            byte[] buf = new byte[BUFFER];
                            int read_bytes;

                            try {
                                float total = 0;

                                while ((read_bytes = inputStream.read(buf)) != -1) {
                                    total += read_bytes;
                                    int value = (int) ((total * 100) / bagList.get(i).getFileSize());
                                    bagList.get(i).setProgress(value);

                                    completeProgress += read_bytes;
                                    int valueComplete = (int) ((completeProgress * 100) / totalDataSize);
                                    progressbar.setProgress(valueComplete);

                                    outputStream.write(buf, 0, read_bytes);
                                    onProgressUpdate();
                                }

                                bagList.get(i).setDone(context.getResources().getDrawable(R.drawable.done_icon_green));
                                notifyList();
                                inputStream.close();

                            } catch (IOException ignored) {
                            }
                        }
                }
                outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
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
            sendData();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            notifyList();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {

                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                disconnect();
            }

            fileNamesList.clear();
            fileLengthList.clear();
            fileDataList.clear();

            doneButton.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            disconnect();
        }

    }
    private void notifyList() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // Stuff that updates the UI
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void connectStatus() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                wifiRadar.setVisibility(View.GONE);

                connectionStatus.setText("Connected");

                animationService.viewGoneAnimator(discoverProgressBar, 1000);

                animationService.viewGoneAnimator(listView, 1000);

                animationService.viewVisibleAnimator(bagListView, 1000);

            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setListIcon() {

        for (int i = 0; i < bagList.size(); i++) {
            if (bagList.get(i).getFileName().toLowerCase().contains("pdf")) {

                bagList.get(i).setIcon(context.getResources().getDrawable(R.drawable.pdf));

            } else if (bagList.get(i).getFileName().toLowerCase().contains("text") || bagList.get(i).getFileName().toLowerCase().contains("txt")) {

                bagList.get(i).setIcon(context.getResources().getDrawable(R.drawable.txt));

            } else if (bagList.get(i).getFileName().toLowerCase().contains("mp3")) {

                bagList.get(i).setIcon(context.getResources().getDrawable(R.drawable.mp3));

            } else if (bagList.get(i).getFileName().toLowerCase().contains("jpeg") || bagList.get(i).getFileName().toLowerCase().contains("jpg")) {

                bagList.get(i).setIcon(context.getResources().getDrawable(R.drawable.jpg));

            } else if (bagList.get(i).getFileName().toLowerCase().contains("png")) {

                bagList.get(i).setIcon(context.getResources().getDrawable(R.drawable.png));

            } else if (bagList.get(i).getFileName().toLowerCase().contains("mp4")) {

                bagList.get(i).setIcon(context.getResources().getDrawable(R.drawable.video_file));

            } else if (bagList.get(i).getFileName().toLowerCase().contains("apk")) {

                bagList.get(i).setIcon(context.getResources().getDrawable(R.drawable.apk));

            } else {
                bagList.get(i).setIcon(context.getResources().getDrawable(R.drawable.file_icon2));
            }
        }
    }

    public class ServerClass extends Thread {

        @Override
        public void run() {
            try {

                ServerSocket serverSocket = new ServerSocket(SplashActivity.PORT);
                Socket socket = serverSocket.accept();
                sendReceive = new SendReceive(socket, serverSocket);
                connectStatus();

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

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    private void checkWifiOnOff() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }
    private void discoverWifi() {

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        wifiP2pManager.discoverPeers(wifiChannel, new WifiP2pManager.ActionListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess() {
                connectionStatus.setText("Searching ...");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(int code) {

                Log.d("IO89", "onFailure: " +code);
                connectionStatus.setText("Discovery not Started");

            }
        });
    }

    public void disconnect() {
        if (wifiP2pManager != null && wifiChannel != null) {
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            wifiP2pManager.requestGroupInfo(wifiChannel, new WifiP2pManager.GroupInfoListener() {
                @Override
                public void onGroupInfoAvailable(WifiP2pGroup group) {
                    if (group != null && wifiP2pManager != null && wifiChannel != null) {
                        wifiP2pManager.removeGroup(wifiChannel, new WifiP2pManager.ActionListener() {

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFailure(int reason) {

                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        disconnect();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        askDisconnectCard= new AskQusDialog("Do you want to disconnect ?");
        askDisconnectCard.setCancelable(false);
        askDisconnectCard.show(fragmentManager, "disconnecting dialog");

    }

    private double getSum(ArrayList<Long> list){
        double sum = 0;
        for(int i = 0; i < list.size(); i++)
            sum += list.get(i);
        return sum;
    }


    private void setConnectionCard(){
        try {
            acceptDialog = new AcceptDialog();
            acceptDialog.setCancelable(false);
            acceptDialog.show(getSupportFragmentManager(), "accept_dialog");

        }catch (Exception e){
            e.printStackTrace();
            
        }
    }

    @Override
    public void acceptCardButton(Button doneButton) {
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReceive.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                acceptDialog.dismiss();
            }
        });
    }


    @Override
    public void AskQusDialogButton(Button yesButton, Button noButton) {
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                disconnect();
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