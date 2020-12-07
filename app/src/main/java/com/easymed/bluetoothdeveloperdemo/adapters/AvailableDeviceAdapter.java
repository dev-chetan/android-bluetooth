package com.easymed.bluetoothdeveloperdemo.adapters;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easymed.bluetoothdeveloperdemo.R;
import com.easymed.bluetoothdeveloperdemo.databinding.ItemAvailableDevicesBinding;

import java.util.ArrayList;

public class AvailableDeviceAdapter extends RecyclerView.Adapter<AvailableDeviceAdapter.ViewHolder> {

    private ArrayList<BluetoothDevice> arrayList = new ArrayList<>();

    @NonNull
    @Override
    public AvailableDeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_available_devices, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableDeviceAdapter.ViewHolder holder, int position) {
        BluetoothDevice device = arrayList.get(position);
        holder.binding.tvTitle.setText(device.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClick(device);
                    }
                });
            }
        });
    }

    public void setOnItemClickListener(PairedDeviceAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(BluetoothDevice device);
    }

    PairedDeviceAdapter.OnItemClickListener listener = new PairedDeviceAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BluetoothDevice device) {

        }
    };

    public void add(BluetoothDevice device) {
        arrayList.add(device);
        notifyDataSetChanged();
    }

    public ArrayList<BluetoothDevice> getArrayList() {
        return arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemAvailableDevicesBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemAvailableDevicesBinding.bind(itemView);
        }
    }
}
