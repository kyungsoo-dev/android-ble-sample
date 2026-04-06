package com.example.bleboilerplate

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult

class BleScanCallback(
    private val onResult: (ScanResult) -> Unit,
    private val onFailed: (Int) -> Unit
) : ScanCallback() {

    override fun onScanResult(callbackType: Int, result: ScanResult) {
        super.onScanResult(callbackType, result)
        onResult(result)
    }

    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        onFailed(errorCode)
    }
}
