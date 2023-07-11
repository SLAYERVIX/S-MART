package com.example.domain.repo

import com.example.domain.entity.stripe.EphemeralRequest
import com.example.domain.entity.stripe.PaymentRequest
import com.example.domain.entity.stripe.customer.CustomerRequest
import retrofit2.Response

interface StripeRepository {
    suspend fun getCustomerData(): Response<CustomerRequest>
    suspend fun getEphemeralKey(customerId: String): Response<EphemeralRequest>
    suspend fun getClientSecret(
        customerId: String,
        amount: Int,
        currency: String,
        automaticPaymentMethodsEnabled: Boolean
    ): Response<PaymentRequest>

}