package com.example.s_mart.ui.cart

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.Product
import com.example.s_mart.R
import com.example.s_mart.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("MissingPermission")
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel: CartViewModel by viewModels()

    private lateinit var dialog: AlertDialog

    private val cartAdapter = CartAdapter()
    private val voucherAdapter = VoucherAdapter()

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)


        binding.apply {
            rvProducts.adapter = cartAdapter
            rvVouchers.adapter = voucherAdapter

            btnClear.setOnClickListener {
                dialog.show()
            }

            btnCheckout.setOnClickListener {
                findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
            }
        }

        onItemDeleteClicked()
        onVoucherApplyClicked()

        getClientData()
        getProductBarcode()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getProductBarcode() {
        lifecycleScope.launch {
            cartViewModel.barcode.collect {
                if (it.isNotEmpty()) {
                    retrieveProductByBarcode(it)
                }
            }
        }
    }

    private fun getClientData() {
        lifecycleScope.launch {
            cartViewModel.retrieveClient().collect { client ->
                client?.let {
                    cartAdapter.submitList(it.cart.products)
                    voucherAdapter.submitList(it.vouchers)

                    binding.client = it
                }
            }
        }
    }

    private fun onVoucherApplyClicked() {
        voucherAdapter.onApplyClicked = { item ->
            cartViewModel.updateAppliedVoucher(item)
        }
    }

    private fun onItemDeleteClicked() {
        cartAdapter.onItemDeleteClicked = { item ->
            cartViewModel.removeProductFromCart(item)
        }
    }

    private fun clearClientCart() {
        cartViewModel.clearCart()
    }

    private fun retrieveProductByBarcode(barcode: String) {
        lifecycleScope.launch {
            cartViewModel.retrieveProductByBarcode(barcode).collect {
                addProductToCart(it)
            }
        }
    }

    private fun addProductToCart(product: Product) {
        cartViewModel.addProductToCart(product)
    }

    override fun onDestroy() {
        super.onDestroy()
//        if (::bluetoothGatt.isInitialized) {
//            bluetoothGatt.disconnect()
//            bluetoothGatt.close()
//        }
        cartViewModel.stopScan()
        _binding = null
    }
}