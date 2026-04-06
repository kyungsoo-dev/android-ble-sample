package com.example.bleboilerplate

import androidx.recyclerview.widget.RecyclerView
import com.example.bleboilerplate.databinding.ItemBleDeviceBinding

class BleDeviceViewHolder(private val binding: ItemBleDeviceBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BleDeviceUiModel) {
        binding.tvDeviceName.text = item.name
        binding.tvDeviceAddress.text = item.address
        binding.tvDeviceRssi.text = binding.root.context.getString(R.string.device_rssi, item.rssi)
    }
}
