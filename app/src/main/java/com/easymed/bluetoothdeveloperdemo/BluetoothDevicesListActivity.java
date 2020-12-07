package com.easymed.bluetoothdeveloperdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.easymed.bluetoothdeveloperdemo.adapters.AvailableDeviceAdapter;
import com.easymed.bluetoothdeveloperdemo.adapters.PairedDeviceAdapter;
import com.easymed.bluetoothdeveloperdemo.commons.AcceptThread;
import com.easymed.bluetoothdeveloperdemo.commons.ConnectThread;
import com.easymed.bluetoothdeveloperdemo.databinding.ActivityBluetoothDevicesListBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.easymed.bluetoothdeveloperdemo.commons.MyBluetoothService.MessageConstants.MESSAGE_READ;
import static com.easymed.bluetoothdeveloperdemo.commons.MyBluetoothService.MessageConstants.MESSAGE_TOAST;
import static com.easymed.bluetoothdeveloperdemo.commons.MyBluetoothService.MessageConstants.MESSAGE_WRITE;

public class BluetoothDevicesListActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    private PairedDeviceAdapter adapter = new PairedDeviceAdapter();
    private AvailableDeviceAdapter availableDeviceAdapter = new AvailableDeviceAdapter();
    private static ActivityBluetoothDevicesListBinding binding;

    // public static final String UUID_TEMP = "123e4567-e89b-12d3-a456-426614174000";HIGH Security
    public static final String UUID_TEMP = "00001101-0000-1000-8000-00805f9b34fb"; //LOW Security

    @Override
    protected void onStart() {
        super.onStart();
        setStatusBarColor();
    }

    private void setStatusBarColor() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bluetooth_devices_list);
        setListSetup();
        binding.textView1.append("\nAdapter: " + btAdapter);
        checkBluetoothState();
        binding.toolbar.tvScan.setOnClickListener(this);
        new AcceptThread(btAdapter, "BluetoothDeveloperDemo").start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void setListSetup() {
        binding.rvPairedDevices.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPairedDevices.setAdapter(adapter);

        adapter.setOnItemClickListener(new PairedDeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BluetoothDevice device) {
                new ConnectThread(device, btAdapter, handler).start();
            }
        });

        binding.rvAvailableDevices.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAvailableDevices.setAdapter(availableDeviceAdapter);


        availableDeviceAdapter.setOnItemClickListener(new PairedDeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BluetoothDevice device) {
                new ConnectThread(device, btAdapter, handler).start();
            }
        });
    }

    private static Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MESSAGE_READ:
                    binding.tvAvailableDevice.append(getRedText(message));
                    break;
                case MESSAGE_WRITE:
                    byte[] bytes1 = (byte[]) message.obj;
                    String s1 = new String(bytes1);
                    binding.tvAvailableDevice.append(s1);
                    break;
                case MESSAGE_TOAST:
                    break;
            }
        }
    };

    private static CharSequence getRedText(Message message) {
        return new String((byte[]) message.obj);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                if (deviceName != null) {
                    availableDeviceAdapter.add(device);
                    removeDummyData(availableDeviceAdapter.getArrayList());
                }
            }
        }
    };

    private void removeDummyData(ArrayList<BluetoothDevice> arrayList) {
        HashSet<BluetoothDevice> hs = new HashSet<>(arrayList); // demoArrayList= name of arrayList from which u want to remove duplicates
        arrayList.clear();
        arrayList.addAll(hs);
    }


    private void checkBluetoothState() {
        if (btAdapter == null) {
            //   binding.textView1.append("\nBluetooth NOT supported. Aborting.");
            return;
        } else {
            if (btAdapter.isEnabled()) {
                Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
                for (BluetoothDevice device : devices) {
                    adapter.add(device);
                }
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvScan:
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(receiver, filter);
                btAdapter.startDiscovery();
                break;
        }
    }
}