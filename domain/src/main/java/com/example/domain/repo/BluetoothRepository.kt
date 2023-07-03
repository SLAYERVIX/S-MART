package com.example.domain.repo

import kotlinx.coroutines.flow.SharedFlow

interface BluetoothRepository {
    val barcode : SharedFlow<String>

    fun startScan()
    fun stopScan()
}