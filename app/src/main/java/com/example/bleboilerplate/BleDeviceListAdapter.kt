package com.example.bleboilerplate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bleboilerplate.databinding.ItemBleDeviceBinding

data class BleDeviceUiModel(
    val address: String,
    val name: String,
    val rssi: Int
)

class BleDeviceListAdapter : ListAdapter<BleDeviceUiModel, BleDeviceListAdapter.DeviceViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBleDeviceBinding.inflate(inflater, parent, false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DeviceViewHolder(private val binding: ItemBleDeviceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BleDeviceUiModel) {
            binding.tvDeviceName.text = item.name
            binding.tvDeviceAddress.text = item.address
            binding.tvDeviceRssi.text = binding.root.context.getString(R.string.device_rssi, item.rssi)
        }
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
