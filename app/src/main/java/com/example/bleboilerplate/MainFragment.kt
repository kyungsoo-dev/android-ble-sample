package com.example.bleboilerplate

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bleboilerplate.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val deviceMap = linkedMapOf<String, BleDeviceUiModel>()
    private val deviceListAdapter = BleDeviceListAdapter()

    private val scanCallback = BleScanCallback(
        onResult = { result ->
            val device = result.device
            val address = device.address ?: return@BleScanCallback
            val deviceName = device.name ?: getString(R.string.unknown_device)

            deviceMap[address] = BleDeviceUiModel(
                address = address,
                name = deviceName,
                rssi = result.rssi
            )
            renderDeviceList()

            binding.tvStatus.text = getString(R.string.scan_found_device_count, deviceMap.size)
        },
        onFailed = { errorCode ->
            binding.tvStatus.text = getString(R.string.scan_failed, errorCode)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bluetoothManager = requireContext().getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        binding.rvDevices.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = deviceListAdapter
        }

        binding.tvTitle.text = getString(R.string.app_name)
        binding.tvDescription.text = getString(R.string.main_description)
        binding.tvStatus.text = getString(R.string.ready_to_scan)

        binding.btnStartScan.setOnClickListener {
            val permissionManager = activity as? PermissionManager
            if (permissionManager == null) {
                binding.tvStatus.text = getString(R.string.permission_required)
                return@setOnClickListener
            }

            permissionManager.runWithBlePermissions(
                onGranted = { startBleScan() },
                onDenied = { binding.tvStatus.text = getString(R.string.permission_required) }
            )
        }
    }

    override fun onStop() {
        super.onStop()
        stopBleScan()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startBleScan() {
        if (!requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            binding.tvStatus.text = getString(R.string.ble_not_supported)
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            Toast.makeText(requireContext(), R.string.bluetooth_disabled, Toast.LENGTH_SHORT).show()
            binding.tvStatus.text = getString(R.string.bluetooth_disabled)
            return
        }

        deviceMap.clear()
        renderDeviceList()

        bluetoothAdapter.bluetoothLeScanner?.startScan(scanCallback)
        binding.tvStatus.text = getString(R.string.scan_started)
    }

    private fun stopBleScan() {
        bluetoothAdapter.bluetoothLeScanner?.stopScan(scanCallback)
    }

    private fun renderDeviceList() {
        val sortedList = deviceMap.values
            .sortedByDescending { it.rssi }
        deviceListAdapter.submitList(sortedList)
    }
}
