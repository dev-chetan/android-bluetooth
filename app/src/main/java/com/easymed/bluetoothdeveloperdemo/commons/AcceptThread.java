package com.easymed.bluetoothdeveloperdemo.commons;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import static com.easymed.bluetoothdeveloperdemo.BluetoothDevicesListActivity.UUID_TEMP;

public class AcceptThread extends Thread {
    private static final String TAG = "AcceptThread";
    private final BluetoothServerSocket mmServerSocket;

    public AcceptThread(BluetoothAdapter defaultAdapter, String name) {
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            Log.e(TAG, "AcceptThreadName: " + name);
            tmp = defaultAdapter.listenUsingRfcommWithServiceRecord(name, UUID.fromString(UUID_TEMP));
        } catch (IOException e) {
            Log.e(TAG, "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                manageMyConnectedSocket(socket);
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void manageMyConnectedSocket(BluetoothSocket socket) {
        Log.e(TAG, "manageMyConnectedSocket: ");
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}