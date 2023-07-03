package com.example.domain.repo

import kotlinx.coroutines.flow.SharedFlow

interface BluetoothRepository {
    fun retrieveBarcodeFlow () : SharedFlow<String>
    fun startScan()
    fun stopScan()
}