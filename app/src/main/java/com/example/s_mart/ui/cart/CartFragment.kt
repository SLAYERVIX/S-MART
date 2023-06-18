package com.example.s_mart.ui.cart

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
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.data.Constants
import com.example.domain.entity.Client
import com.example.domain.entity.Product
import com.example.domain.entity.Voucher
import com.example.s_mart.R
import com.example.s_mart.core.adapters.CartAdapter
import com.example.s_mart.core.adapters.VoucherAdapter
import com.example.s_mart.core.callbacks.CartCallback
import com.example.s_mart.core.callbacks.VoucherCallback
import com.example.s_mart.core.utils.calcDiscount
import com.example.s_mart.databinding.FragmentCartBinding
import com.example.s_mart.ui.SmartViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("MissingPermission")
class CartFragment : Fragment(), CartCallback, VoucherCallback {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SmartViewModel by activityViewModels()

    @Inject
    lateinit var bluetoothAdapter: BluetoothAdapter

    private lateinit var bluetoothGatt: BluetoothGatt
    private lateinit var bluetoothGattCallback: BluetoothGattCallback

    private lateinit var dialog: AlertDialog

    private val cartAdapter = CartAdapter(this)
    private val voucherAdapter = VoucherAdapter(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)

        dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.clear_cart))
            .setMessage(getString(R.string.clear_cart_message))
            .setNegativeButton(getString(R.string.cancel), null)
            .setPositiveButton(getString(R.string.clear)) { _, _ ->
                clearClientCart()
            }.create()
    }

    private fun clearClientCart() {
        viewModel.clientDocument.get()
            .addOnSuccessListener { snapshot ->
                val client = snapshot.toObject(Client::class.java)

                client?.let {
                    it.cart.products.clear()
                    it.cart.totalPrice = 0.0

                    viewModel.clientDocument.set(client)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        binding.rvProducts.adapter = cartAdapter
        binding.rvVouchers.adapter = voucherAdapter

        viewModel.clientDocument.addSnapshotListener { value, _ ->
            if (value != null) {
                val client = value.toObject(Client::class.java)
                client?.let {
                    cartAdapter.submitList(it.cart.products)
                    voucherAdapter.submitList(it.vouchers)

                    it.cart.totalPrice -= (it.cart.totalPrice * it.cart.appliedVoucher.discount)

                    updateUi(it.cart.products.isEmpty(), it.cart.totalPrice)
                }
            }
        }

        setupBluetooth()

        bluetoothAdapter.bluetoothLeScanner.startScan(scanFilters, scanSettings, scanCallback)

        binding.btnClear.setOnClickListener {
            dialog.show()
        }

        binding.btnCheckout.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun updateUi(empty: Boolean, totalPrice: Double?) {
        if (empty) {
            binding.apply {
                btnCheckout.isEnabled = false
                btnClear.visibility = View.GONE
                btnCheckout.visibility = View.GONE
                tvCart.visibility = View.GONE
                tvPrice.visibility = View.GONE
                tvTotal.visibility = View.GONE
                divider.visibility = View.GONE
                rvVouchers.visibility = View.GONE
                tvMessage.visibility = View.VISIBLE
                ivEmpty.visibility = View.VISIBLE
            }
        }
        else {
            binding.apply {
                btnCheckout.isEnabled = true
                btnClear.visibility = View.VISIBLE
                btnCheckout.visibility = View.VISIBLE
                tvCart.visibility = View.VISIBLE
                tvPrice.visibility = View.VISIBLE
                tvTotal.visibility = View.VISIBLE
                divider.visibility = View.VISIBLE
                rvVouchers.visibility = View.VISIBLE
                tvMessage.visibility = View.GONE
                ivEmpty.visibility = View.GONE

                tvPrice.text = totalPrice.toString()
            }
        }
    }

    private fun setupBluetooth() {
        bluetoothGattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    // Connected to the device, start service discovery
                    gatt.discoverServices()
                }
                else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    // Disconnected from the device, handle accordingly
                    // You can attempt to reconnect or clean up the resources as needed
                    // For example:
                    bluetoothAdapter.bluetoothLeScanner.startScan(
                        scanFilters,
                        scanSettings,
                        scanCallback
                    )
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

                // Ensure the received value is not empty
                if (value.isNotEmpty()) {
                    // Process the received data according to your requirements
                    // For example, convert bytes to a string if the data represents text
                    val receivedData = String(value).trim()

                    viewModel.productCollection.document(receivedData).get()
                        .addOnSuccessListener { documentSnapshot ->
                            val prod = documentSnapshot.toObject(Product::class.java)

                            if (prod != null) {
                                addProductToCart(prod)
                            }

                        }.addOnFailureListener { exception ->
                            Log.e("rabbit", "Error retrieving product data: ${exception.message}")
                        }
                }
            }
        }
    }

    private fun addProductToCart(product: Product) {
        viewModel.firebaseAuth.currentUser?.let { _ ->
            viewModel.clientDocument.get()
                .addOnCompleteListener { task ->
                    task.addOnSuccessListener { snapshot ->
                        val client = snapshot.toObject(Client::class.java)
                        val uuid = UUID.randomUUID().toString()

                        client?.let {
                            it.cart.products.add(product.copy(_id = uuid))
                            it.cart.totalPrice += calcDiscount(
                                product.price,
                                product.discountPercentage
                            )
                        }

                        viewModel.clientDocument.set(client!!)
                    }
                    task.addOnFailureListener {

                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::bluetoothGatt.isInitialized) {
            bluetoothGatt.disconnect()
            bluetoothGatt.close()
        }

        _binding = null
    }

    override fun onDeleteClicked(item: Product) {
        viewModel.clientDocument.get().addOnSuccessListener { snapshot ->
            val client = snapshot.toObject(Client::class.java)

            client?.let {
                client.cart.products.remove(item)
                client.cart.totalPrice -= calcDiscount(item.price, item.discountPercentage)

                viewModel.clientDocument.set(client)
            }
        }
    }

    private val scanFilters = mutableListOf<ScanFilter>()

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device: BluetoothDevice = result.device
            val deviceAddress = device.address

            // Check if the device MAC address matches your desired MAC address
            if (deviceAddress == Constants.DESIRED_DEVICE_ADDRESS) {
                // Device found, stop scanning
                bluetoothAdapter.bluetoothLeScanner.stopScan(this)

                bluetoothGatt = device.connectGatt(requireContext(), false, bluetoothGattCallback)
            }
        }
    }

    override fun onApplyClicked(item: Voucher) {
        viewModel.clientDocument.get().addOnCompleteListener { task ->
            task.addOnSuccessListener { snapshot ->
                val client = snapshot.toObject(Client::class.java)
                client?.let {
                    it.cart.appliedVoucher = item
                    viewModel.clientDocument.set(client)
                }
            }

            task.addOnFailureListener {}
        }
    }
}