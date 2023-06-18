package com.example.s_mart.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.Client
import com.example.domain.entity.Voucher
import com.example.s_mart.core.utils.Date
import com.example.s_mart.databinding.FragmentCheckoutBinding
import com.example.s_mart.ui.SmartViewModel
import kotlin.math.roundToInt

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SmartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTotalPrice()

        binding.btnConfirmPayment.setOnClickListener {
            validatePayment()
        }
    }

    private fun setTotalPrice() {
        viewModel.clientDocument.get().addOnCompleteListener { task ->
            task.addOnSuccessListener { snapshot ->
                val client = snapshot.toObject(Client::class.java)
                client?.let {
                    if (it.cart.appliedVoucher.discount != 0.0) {
                        it.cart.totalPrice -= (it.cart.totalPrice * it.cart.appliedVoucher.discount)
                        binding.tvPrice.text = "EGP ${it.cart.totalPrice}"
                    }
                    else {
                        binding.tvPrice.text = "EGP ${it.cart.totalPrice}"
                    }
                }
            }
            task.addOnFailureListener { }
        }
    }

    private fun validatePayment() {
        val creditCard = binding.etCreditCard.text.toString()
        val expire = binding.etExpireDate.text.toString()
        val cvv = binding.etCvv.text.toString()
        val holderName = binding.etName.text.toString()

        if (!validateCreditCard(creditCard)) return
        if (!validateExpire(expire)) return
        if (!validateCvv(cvv)) return
        if (!validateHolderName(holderName)) return

        viewModel.clientDocument.get().addOnCompleteListener { task ->
            task.addOnSuccessListener { snapshot ->
                val client = snapshot.toObject(Client::class.java)
                client?.let {
                    it.points += it.cart.totalPrice.roundToInt()

                    it.orderHistory.products.addAll(it.cart.products)
                    it.orderHistory.totalPrice = it.cart.totalPrice
                    it.orderHistory.date = Date.date

                    it.cart.products.clear()
                    it.cart.totalPrice = 0.0
                    it.vouchers.remove(it.cart.appliedVoucher)

                    it.cart.appliedVoucher = Voucher()

                    viewModel.clientDocument.set(it).addOnSuccessListener {

                        Toast.makeText(requireContext(), "Successful transaction", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                        // findNavController().navigate(R.id.action_checkoutFragment_to_paymentSuccesfulFragment)
                    }
                }
            }
            task.addOnFailureListener {}
        }

    }

    private fun validateHolderName(holderName: String): Boolean {
        if (holderName.isEmpty()) {
            return false
        }
        return true
    }

    private fun validateCvv(cvv: String): Boolean {
        if (cvv.isEmpty()) {
            return false
        }
        if (cvv.length < 3) {
            return false
        }
        return true
    }

    private fun validateExpire(expire: String): Boolean {
        if (expire.isEmpty()) {
            return false
        }
        if (expire.length < 5) {
            return false
        }
        return true
    }

    private fun validateCreditCard(creditCard: String): Boolean {
        if (creditCard.isEmpty()) {
            return false
        }
        if (creditCard.length < 16) {
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}