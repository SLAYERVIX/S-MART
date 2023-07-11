package com.example.s_mart.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.data.Constants
import com.example.s_mart.databinding.FragmentCheckoutBinding
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val checkoutViewModel: CheckoutViewModel by viewModels()


    private lateinit var paymentSheet: PaymentSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkoutViewModel.retrieveCustomer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)

        PaymentConfiguration.init(requireContext(), Constants.PUBLISHABLE_KEY)

        paymentSheet = PaymentSheet(this) { result ->
            onPaymentResult(result)
        }

        binding.btnPayWithCredit.setOnClickListener {
            binding.btnPayWithCredit.isEnabled = false
            paymentFlow()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun paymentFlow() {
        lifecycleScope.launch {
            checkoutViewModel.checkoutState.collect {
                when (it) {
                    PaymentState.Successful -> {
                        val configuration = PaymentSheet.Configuration(
                            merchantDisplayName = Constants.MERCHANT_DISPLAY_NAME,
                            PaymentSheet.CustomerConfiguration(
                                id = checkoutViewModel.customerId ?: "",
                                ephemeralKeySecret = checkoutViewModel.ephemeral ?: ""
                            )
                        )

                        paymentSheet.presentWithPaymentIntent(
                            checkoutViewModel.clientSecret ?: "",
                            configuration
                        )
                    }

                    else -> {

                    }
                }
            }
        }
    }


    private fun onPaymentResult(result: PaymentSheetResult) {
        when (result) {
            is PaymentSheetResult.Completed -> {
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                binding.btnPayWithCredit.isEnabled = true
            }

            is PaymentSheetResult.Canceled -> {binding.btnPayWithCredit.isEnabled = true}
            is PaymentSheetResult.Failed -> {binding.btnPayWithCredit.isEnabled = true}
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}