package com.app.healthmonitor;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainApplication extends Application {
    private static MainApplication sInstance;
    private BluetoothService bluetoothService;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    public static MainApplication getApplication() {
        return sInstance;
    }

    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public void setBluetoothService(BluetoothService BTService)
    {
        this.bluetoothService = BTService;
    }

    public BluetoothService getBluetoothService()
    {
        return bluetoothService;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(FirebaseUser currentUser) {
        this.currentUser = currentUser;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }
}
