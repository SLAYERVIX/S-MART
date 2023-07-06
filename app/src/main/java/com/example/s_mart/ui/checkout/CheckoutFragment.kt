package com.example.s_mart.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.s_mart.databinding.FragmentCheckoutBinding

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val checkoutViewModel : CheckoutViewModel by viewModels()

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
    }

    private fun setTotalPrice() {
//        viewModel.clientDocument.get().addOnCompleteListener { task ->
//            task.addOnSuccessListener { snapshot ->
//                val client = snapshot.toObject(Client::class.java)
//                client?.let {
//                    if (it.cart.appliedVoucher.discount != 0.0) {
//                        it.cart.totalPrice -= (it.cart.totalPrice * it.cart.appliedVoucher.discount)
//                        binding.tvPrice.text = "EGP ${it.cart.totalPrice}"
//                    }
//                    else {
//                        binding.tvPrice.text = "EGP ${it.cart.totalPrice}"
//                    }
//                }
//            }
//            task.addOnFailureListener { }
//        }
    }

    private fun validatePayment() {

//        viewModel.clientDocument.get().addOnCompleteListener { task ->
//            task.addOnSuccessListener { snapshot ->
//                val client = snapshot.toObject(Client::class.java)
//                client?.let {
//                    it.points += it.cart.totalPrice.roundToInt()
//
//                    it.orderHistory.products.addAll(it.cart.products)
//                    it.orderHistory.totalPrice = it.cart.totalPrice
//                    it.orderHistory.date = Date.date
//
//                    it.cart.products.clear()
//                    it.cart.totalPrice = 0.0
//                    it.vouchers.remove(it.cart.appliedVoucher)
//
//                    it.cart.appliedVoucher = Voucher()
//
//                    viewModel.clientDocument.set(it).addOnSuccessListener {
//
//                        Toast.makeText(requireContext(), "Successful transaction", Toast.LENGTH_SHORT).show()
//                        findNavController().popBackStack()
//                        // findNavController().navigate(R.id.action_checkoutFragment_to_paymentSuccesfulFragment)
//                    }
//                }
//            }
//            task.addOnFailureListener {}
//        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}