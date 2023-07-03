package com.example.data.repo

import com.example.data.bluetooth.BluetoothService
import com.example.domain.repo.BluetoothRepository
import kotlinx.coroutines.flow.SharedFlow

class BluetoothRepoImpl(private val bluetoothService: BluetoothService) : BluetoothRepository {
    override val barcode: SharedFlow<String> get() = bluetoothService.barcode

    override fun startScan() {
        bluetoothService.startScan()
    }

    override fun stopScan() {
        bluetoothService.stopScan()
    }
}