package com.example.bleboilerplate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.bleboilerplate.databinding.ItemBleDeviceBinding

class BleDeviceListAdapter : ListAdapter<BleDeviceUiModel, BleDeviceViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BleDeviceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBleDeviceBinding.inflate(inflater, parent, false)
        return BleDeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BleDeviceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<BleDeviceUiModel>() {
        override fun areItemsTheSame(oldItem: BleDeviceUiModel, newItem: BleDeviceUiModel): Boolean {
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: BleDeviceUiModel, newItem: BleDeviceUiModel): Boolean {
            return oldItem == newItem
        }
    }
}
