package com.easymed.bluetoothdeveloperdemo.adapters;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easymed.bluetoothdeveloperdemo.R;
import com.easymed.bluetoothdeveloperdemo.databinding.ItemPairedDevicesBinding;

import java.util.ArrayList;

public class PairedDeviceAdapter extends RecyclerView.Adapter<PairedDeviceAdapter.ViewHolder> {

    private ArrayList<BluetoothDevice> arrayList = new ArrayList<>();

    @NonNull
    @Override
    public PairedDeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paired_devices, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PairedDeviceAdapter.ViewHolder holder, int position) {
        BluetoothDevice device = arrayList.get(position);
        holder.binding.tvTitle.setText(device.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(device);
            }
        });
    }

    OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(BluetoothDevice device) {

        }
    };

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(BluetoothDevice device);
    }

    public void add(BluetoothDevice device) {
        arrayList.add(device);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemPairedDevicesBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemPairedDevicesBinding.bind(itemView);
        }
    }
}
