package com.example.data.repo

import com.example.data.network.PaymentService
import com.example.domain.entity.stripe.EphemeralRequest
import com.example.domain.entity.stripe.PaymentRequest
import com.example.domain.entity.stripe.customer.CustomerRequest
import com.example.domain.repo.StripeRepository
import retrofit2.Response

class StripeRepoImpl(private val paymentService: PaymentService) : StripeRepository {
    override suspend fun getCustomerData(): Response<CustomerRequest> {
        return paymentService.getCustomerData()
    }

    override suspend fun getEphemeralKey(customerId: String): Response<EphemeralRequest> {
        return paymentService.getEphemeralKey(customerId)
    }

    override suspend fun getClientSecret(
        customerId: String,
        amount: Int,
        currency: String,
        automaticPaymentMethodsEnabled: Boolean
    ): Response<PaymentRequest> {
        return paymentService.getClientSecret(
            customerId,
            amount,
            currency,
            automaticPaymentMethodsEnabled
        )
    }

}