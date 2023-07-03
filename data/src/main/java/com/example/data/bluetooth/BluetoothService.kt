package com.example.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import com.example.data.Constants
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@SuppressLint("MissingPermission")
class BluetoothService(
    private val bluetoothAdapter: BluetoothAdapter,
    private val scanSettings: ScanSettings,
    private val context: Context
) {
    private lateinit var bluetoothGatt: BluetoothGatt

    private val scanFilters = mutableListOf<ScanFilter>()

    private var _barcode: MutableSharedFlow<String> = MutableSharedFlow(replay = 1)
    val barcode: SharedFlow<String> get() = _barcode

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device: BluetoothDevice = result.device
            val deviceAddress = device.address

            // Check if the device MAC address matches your desired MAC address
            if (deviceAddress == Constants.DESIRED_DEVICE_ADDRESS) {
                // Device found, stop scanning
                stopScan()
                bluetoothGatt = device.connectGatt(context, false, bluetoothGattCallback)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // Connected to the device, start service discovery
                gatt.discoverServices()
            }
            else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // Disconnected from the device, handle accordingly
                // You can attempt to reconnect or clean up the resources as needed
                // For example:
                startScan()
                bluetoothGatt.close()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val service = gatt.getService(Constants.SERVICE_UUID)
                val characteristic = service?.getCharacteristic(Constants.CHARACTERISTICS_UUID)

                characteristic?.let {
                    // Enable notifications or indications on the characteristic
                    gatt.setCharacteristicNotification(it, true)

                    val descriptor =
                        it.getDescriptor(Constants.CLIENT_CHARACTERISTIC_CONFIG_UUID)


                    descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    descriptor?.let { gattDescriptor ->
                        if (Build.VERSION.SDK_INT == 33) {
                            gatt.writeDescriptor(gattDescriptor, ByteArray(2))
                        }
                        else {
                            @Suppress("DEPRECATION")
                            gatt.writeDescriptor(descriptor)
                        }
                    }
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            // Handle received data here
            val value = characteristic.value

            // Process the received data according to your requirements
            // For example, convert bytes to a string if the data represents text
            val barcode = String(value).trim()
            _barcode.tryEmit(barcode)
        }
    }


    fun startScan() {
        bluetoothAdapter.bluetoothLeScanner.startScan(scanFilters, scanSettings, scanCallback)
    }

    fun stopScan() {
        bluetoothAdapter.bluetoothLeScanner.stopScan(scanCallback)
    }
}