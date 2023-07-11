package com.example.s_mart.ui.checkout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repo.StripeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val stripeRepository: StripeRepository
) : ViewModel() {

    var customerId: String? = null
    var ephemeral: String? = null
    var clientSecret: String? = null

    private val _checkoutState: MutableStateFlow<PaymentState> = MutableStateFlow(PaymentState.Idle)
    val checkoutState: StateFlow<PaymentState> get() = _checkoutState

    fun retrieveCustomer() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = stripeRepository.getCustomerData()
            if (response.isSuccessful) {
                response.body()?.let {
                    customerId = it.id
                    retrieveEphemeral(it.id)
                }
            }
        } catch (e: Exception) {
            Log.d("rabbit", "retrieveCustomer: $e")
        }
    }

    private suspend fun retrieveEphemeral(customer: String) = coroutineScope {
        try {
            val response = stripeRepository.getEphemeralKey(customer)
            if (response.isSuccessful) {
                response.body()?.let {
                    ephemeral = it.associated_objects[0].id
                    retrieveClientSecret(it.associated_objects[0].id, 2000, "USD", true)
                }
            }
            else {
                Log.d("rabbit", "retrieveEphemeral2: ${response.errorBody()}")
            }
        } catch (e: Exception) {
            Log.d("rabbit", "retrieveEphemeral2: $e")
        }
    }

    private suspend fun retrieveClientSecret(
        customer: String,
        amount: Int,
        currency: String,
        autoPayment: Boolean
    ) = coroutineScope {
        try {
            val response =
                stripeRepository.getClientSecret(customer, amount, currency, autoPayment)
            if (response.isSuccessful) {

                response.body()?.let {
                    clientSecret = it.client_secret
                    _checkoutState.value = PaymentState.Successful
                }
            }
            else {
                Log.d("rabbit", "retrieveEphemeral2: ${response.errorBody()}")
            }

        } catch (e: Exception) {
            Log.d("rabbit", "retrieveEphemeral2: $e")
        }
    }
}