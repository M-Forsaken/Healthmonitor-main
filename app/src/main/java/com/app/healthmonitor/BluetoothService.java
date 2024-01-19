package com.app.healthmonitor;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService extends Thread{
    private static final String TAG = "Logs";
    String valueRead;
    Boolean connected = false;
    Boolean running = true;
    private BluetoothSocket mmsocket;
    private final BluetoothDevice BTDevice;
    private final UUID serviceUUID;
    public Handler handler;
    InputStream I_Stream;
    OutputStream O_Stream;
    private final static int ERROR_READ = 0,VALUE_READ = 1;

    @SuppressLint("MissingPermission")
    public BluetoothService(BluetoothDevice device, UUID MY_UUID, Handler handler){
        this.BTDevice = device;
        this.serviceUUID = MY_UUID;
        this.handler = handler;
    }
    public BluetoothSocket getMmsocket() {
        return this.mmsocket;
    }

    public UUID getServiceUUID() {
        return serviceUUID;
    }
    public void setRunning(Boolean running) {
        this.running = running;
    }

    @SuppressLint("MissingPermission")
    public void SetupSocketConnection(){
        try {
            mmsocket = BTDevice.createRfcommSocketToServiceRecord(serviceUUID);
            setupDataStream();
        } catch (IOException e) {
            handler.obtainMessage(ERROR_READ, "Failed to create Socket.").sendToTarget();
        }
        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmsocket.connect();
            connected = true;
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            handler.obtainMessage(ERROR_READ, "Unable to connect to the BT device.").sendToTarget();
            Log.e(TAG, "connectException: " + connectException);
            try {
                mmsocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
        }
    }
    public void setupDataStream(){
        try {
            I_Stream = mmsocket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            O_Stream = mmsocket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }
    }
    public void readData(){
        byte[] buffer = new byte[1024];
        int bytes = 0; // bytes returned from read()
        valueRead = "";

        while (running) {
            try {
                buffer[bytes] = (byte) I_Stream.read();
                String readMessage;
                // If I detect a "\n" means I already read a full measurement
                if (buffer[bytes] == '\n' && running) {
                    readMessage = new String(buffer, 0, bytes);
                    handler.obtainMessage(VALUE_READ, readMessage).sendToTarget();
                    bytes = 0;
                } else {
                    bytes++;
                }

            } catch (IOException e) {
                handler.obtainMessage(ERROR_READ, "Device disconnected").sendToTarget();
                break;
            }
        }
    }
    public void write(String input) {
        byte[] bytes = input.getBytes(); //converts entered String into bytes
        try {
            O_Stream.write(bytes);
        } catch (IOException e) {
            Log.e("Send Error","Unable to send message",e);
        }
    }
    public void cancel() {
        try {
            mmsocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }


}
