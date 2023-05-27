package com.example.s_mart.ui.cart

import android.annotation.SuppressLint
import android.app.Activity
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
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.entity.Product
import com.example.domain.entity.ProductResponse
import com.example.s_mart.R
import com.example.s_mart.core.utils.Constants
import com.example.s_mart.core.utils.Mappers
import com.example.s_mart.core.adapters.CartAdapter
import com.example.s_mart.core.callbacks.CartCallback
import com.example.s_mart.core.utils.calcDiscount
import com.example.s_mart.databinding.FragmentCartBinding
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("MissingPermission")
class CartFragment : Fragment(),CartCallback {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var bluetoothAdapter: BluetoothAdapter

    private lateinit var bluetoothGatt: BluetoothGatt

    private lateinit var firestore: FirebaseFirestore

    private val viewModel: CartViewModel by viewModels()

    private lateinit var dialog: AlertDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)

        dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.clear_cart))
            .setMessage(getString(R.string.clear_cart_message))
            .setNegativeButton(getString(R.string.cancel), null)
            .setPositiveButton("Clear") { _, _ ->
                viewModel.clearCart()
            }.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        val adapter = CartAdapter(this)
        binding.rvProducts.adapter = adapter

        lifecycleScope.launchWhenCreated {
            viewModel.getAllCartProducts().collect {
                if (it.isEmpty()) {
                    binding.apply {
                        btnCheckout.isEnabled = false
                        btnClear.visibility = View.GONE
                        btnCheckout.visibility = View.GONE
                        tvCart.visibility = View.GONE
                        tvPrice.visibility = View.GONE
                        tvTotal.visibility = View.GONE
                        divider.visibility = View.GONE
                        tvMessage.visibility = View.VISIBLE
                        ivEmpty.visibility = View.VISIBLE
                    }
                }
                else {
                    var total = 0.0
                    binding.apply {
                        btnCheckout.isEnabled = true
                        btnClear.visibility = View.VISIBLE
                        btnCheckout.visibility = View.VISIBLE
                        tvCart.visibility = View.VISIBLE
                        tvPrice.visibility = View.VISIBLE
                        tvTotal.visibility = View.VISIBLE
                        divider.visibility = View.VISIBLE
                        tvMessage.visibility = View.GONE
                        ivEmpty.visibility = View.GONE

                        for (product in it) {
                            if (product.discountPercentage == 0.0) {
                                total += product.price
                            }
                            else {
                                total += calcDiscount(product.price, product.discountPercentage)
                            }
                        }

                        binding.tvPrice.text = "EGP $total"
                    }
                }

                adapter.submitList(it)
            }
        }

        val gattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    // Connected to the device, start service discovery
                    gatt.discoverServices()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    // Disconnected from the device, handle accordingly
                    // You can attempt to reconnect or clean up the resources as needed
                    // For example:
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
                // Handle received data here
                val value = characteristic.value

                // Ensure the received value is not empty
                if (value.isNotEmpty()) {
                    // Process the received data according to your requirements
                    // For example, convert bytes to a string if the data represents text
                    val receivedData = String(value).trim()

                    val productsReference =
                        firestore.collection(Constants.PRODUCTS_REF).document(receivedData)

                    productsReference.get().addOnSuccessListener { documentSnapshot ->
                        val prod = documentSnapshot.toObject(ProductResponse::class.java)
                        if (prod != null) {
                            viewModel.insertProduct(Mappers.mapProductResponseToProduct(prod))
                        }
                    }.addOnFailureListener { exception ->
                        Log.e("rabbit", "Error retrieving product data: ${exception.message}")
                    }
                }
            }
        }

        val scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val device: BluetoothDevice = result.device
                val deviceAddress = device.address

                // Check if the device MAC address matches your desired MAC address
                if (deviceAddress == Constants.DESIRED_DEVICE_ADDRESS) {
                    // Device found, stop scanning
                    bluetoothAdapter.bluetoothLeScanner.stopScan(this)

                    bluetoothGatt = device.connectGatt(requireContext(), false, gattCallback)
                }
            }
        }

        val scanFilters = mutableListOf<ScanFilter>()
        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        bluetoothAdapter.bluetoothLeScanner.startScan(scanFilters, scanSettings, scanCallback)

        binding.btnClear.setOnClickListener {
            dialog.show()
        }

       // enableBluetooth()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::bluetoothGatt.isInitialized) {
            bluetoothGatt.disconnect()
            bluetoothGatt.close()
        }

        _binding = null
    }

    private fun enableBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            // Bluetooth is not enabled, request the user to enable it
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            resultLauncher.launch(enableBtIntent)
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Handle the result here
            }
        }

    override fun onDeleteClicked(item: Product) {
        viewModel.deleteProduct(item)
    }
}


