package com.app.healthmonitor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;
import java.util.UUID;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeviceFragment extends Fragment {
    View fragmentView;
    private ViewItemModel viewItemModel;
    String DeviceInfo;
    public static Handler handler;
    BluetoothDevice BTModule = null;
    private final static int ERROR_READ = 0; // used in bluetooth handler to identify message update
    UUID serviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothService Service;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Toast.makeText(getContext(), "Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission not Granted!", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_device, container, false);
        viewItemModel = new ViewModelProvider(requireActivity()).get(ViewItemModel.class);
        BluetoothManager bluetoothManager = null;
        bluetoothManager = requireActivity().getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        TextView BT_Devices = fragmentView.findViewById(R.id.btDevices);
        TextView ConnectState = fragmentView.findViewById(R.id.connectState);

        CardView connectToDevice = fragmentView.findViewById(R.id.connectToDevice);
        CardView searchDevices = fragmentView.findViewById(R.id.searchDevices);


        //Using a handler to update the interface in case of an error connecting to the BT device
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case ERROR_READ:
                        String Message = msg.obj.toString();
                        Toast.makeText(getActivity(), Message,
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        @SuppressLint("MissingPermission")
        final Observable<Boolean> connectToBTObservable = Observable.create(emitter -> {
            if (Service == null||!Service.connected||Service.getServiceUUID() != serviceUUID){
                Service = new BluetoothService(BTModule,serviceUUID,handler);
                Service.SetupSocketConnection();
            }
            emitter.onNext(Service.getMmsocket().isConnected());
            emitter.onComplete();
        });
        searchDevices.setOnClickListener(new View.OnClickListener() {
            //Display all the linked BT Devices
            @Override
            public void onClick(View view) {
                //Check if the phone supports BT
                if (bluetoothAdapter != null) {
                    //Check BT enabled. If disabled, we ask the user to enable BT
                    if (!bluetoothAdapter.isEnabled()) {
                        Intent EnableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        if (ActivityCompat.checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            activityResultLauncher.launch(EnableBlueTooth);
                        }
                    }
                    String BT_Device = "";

                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0 ) {
                        // There are paired devices. Get the name and address of each paired device.
                        for (BluetoothDevice device : pairedDevices) {
                            String deviceName = device.getName();
                            String deviceHardwareAddress = device.getAddress(); // MAC address
                            //We append all devices to a String that we will display in the UI
                            BT_Device = BT_Device + deviceName + "\n";
                            if (deviceName.equals("HC-05")) {
                                serviceUUID = device.getUuids()[0].getUuid();
                                BTModule = device;
                                connectToDevice.setEnabled(true);
                                DeviceInfo = deviceName + " || " + deviceHardwareAddress + "\n";
                            }
                            BT_Devices.setText(BT_Device);
                        }
                    }
                }
            }
        });
        connectToDevice.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View view) {
                    connectToBTObservable.
                            observeOn(AndroidSchedulers.mainThread()).
                            subscribeOn(Schedulers.io()).
                            subscribe(connectedToBTDevice -> {
                                if(connectedToBTDevice){
                                    MainApplication.getApplication().setBluetoothService(Service);
                                    Service.write(MainApplication.getApplication().getCurrentUser().getUid());
                                    Toast.makeText(getActivity(), "Device is connected.",Toast.LENGTH_SHORT).show();
                                    String Info = DeviceInfo +"Connection State: "+ Service.getMmsocket().isConnected();
                                    ConnectState.setText(Info);
                                }
                            });
            }
        });
        return fragmentView;
    }
}
