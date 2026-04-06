package com.example.bleboilerplate

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bleboilerplate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val deviceMap = linkedMapOf<String, BleDeviceUiModel>()
    private val deviceListAdapter = BleDeviceListAdapter()

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val device = result.device
            val address = device.address ?: return
            val deviceName = device.name ?: getString(R.string.unknown_device)

            deviceMap[address] = BleDeviceUiModel(
                address = address,
                name = deviceName,
                rssi = result.rssi
            )
            renderDeviceList()

            binding.tvStatus.text = getString(R.string.scan_found_device_count, deviceMap.size)
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            binding.tvStatus.text = getString(R.string.scan_failed, errorCode)
        }
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grants ->
            if (grants.values.all { it }) {
                startBleScan()
            } else {
                binding.tvStatus.text = getString(R.string.permission_required)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        binding.rvDevices.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = deviceListAdapter
        }

        binding.tvTitle.text = getString(R.string.app_name)
        binding.tvDescription.text = getString(R.string.main_description)
        binding.tvStatus.text = getString(R.string.ready_to_scan)

        binding.btnStartScan.setOnClickListener {
            checkPermissionsAndScan()
        }
    }

    override fun onStop() {
        super.onStop()
        stopBleScan()
    }

    private fun checkPermissionsAndScan() {
        val permissions = requiredPermissions()
        val denied = permissions.filter { permission ->
            ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
        }

        if (denied.isEmpty()) {
            startBleScan()
            return
        }

        permissionLauncher.launch(denied.toTypedArray())
    }

    private fun startBleScan() {
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            binding.tvStatus.text = getString(R.string.ble_not_supported)
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            Toast.makeText(this, R.string.bluetooth_disabled, Toast.LENGTH_SHORT).show()
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

    private fun requiredPermissions(): List<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            listOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            listOf(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}
