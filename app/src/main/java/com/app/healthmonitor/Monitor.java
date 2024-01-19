package com.app.healthmonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Monitor extends AppCompatActivity {
    private int currentApiVersion;
    BluetoothService bluetoothService;
    Handler handler;
    String message;
    CardView BB,HR;
    TextView HR_text, BO_text, BT_text;
    private final static int ERROR_READ = 0,VALUE_READ = 1;


    @SuppressLint("CheckResult")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        currentApiVersion = Build.VERSION.SDK_INT;

        final int flags =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
        bluetoothService = MainApplication.getApplication().getBluetoothService();
        HR = findViewById(R.id.HR_Card);
        HR_text = findViewById(R.id.HR_text);
        BO_text = findViewById(R.id.BO_text);
        BT_text = findViewById(R.id.BT_text);
        BB = findViewById(R.id.Back_B);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String Message = msg.obj.toString();
                switch (msg.what) {
                    case ERROR_READ:
                        Toast.makeText(Monitor.this, Message,
                                Toast.LENGTH_LONG).show();
                        break;
                    case VALUE_READ:
                        String[] Data = Message.split(" ");
                        HR_text.setText(Data[0]);
                        BO_text.setText(Data[1]);
                        BT_text.setText(Data[2]);
                        break;
                    default:
                        break;
                }
            }
        };
        final Observable<Boolean> readData = Observable.create(emitter -> {
            bluetoothService.handler = handler;
            bluetoothService.readData();
            emitter.onComplete();
        });
        if (bluetoothService == null)
        {
            Toast.makeText(Monitor.this, "Device is not connected.",
                    Toast.LENGTH_LONG).show();
        }
        else {
            bluetoothService.setRunning(true);
            readData.observeOn(AndroidSchedulers.mainThread()).
                    subscribeOn(Schedulers.io()).
                    subscribe(ReceivedData -> {
                    });
        }

        BB.setOnClickListener(view -> {
            if (bluetoothService != null) bluetoothService.setRunning(false);
            this.finish();
        });
    }
}