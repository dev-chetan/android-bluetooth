package com.easymed.bluetoothdeveloperdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.easymed.bluetoothdeveloperdemo.databinding.ActivityBluetoothNewBinding;

public class BluetoothNewActivity extends AppCompatActivity {
    private ActivityBluetoothNewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bluetooth_new);
    }
}