package com.example.bleboilerplate

interface PermissionManager {
    fun runWithBlePermissions(onGranted: () -> Unit, onDenied: () -> Unit)
}
