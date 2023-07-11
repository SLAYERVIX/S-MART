package com.example.data.network

import com.example.data.Constants
import com.example.domain.entity.stripe.EphemeralRequest
import com.example.domain.entity.stripe.PaymentRequest
import com.example.domain.entity.stripe.customer.CustomerRequest
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

// https://api.stripe.com/v1/customers
interface PaymentService {
    @POST("v1/customers")
    @Headers("Authorization: Bearer ${Constants.SK_TOKEN}")
    suspend fun getCustomerData(): Response<CustomerRequest>

    @Headers("Stripe-Version: 2022-11-15", "Authorization: Bearer ${Constants.SK_TOKEN}")
    @POST("v1/ephemeral_keys")
    suspend fun getEphemeralKey(
        @Query("customer") customerId: String
    ): Response<EphemeralRequest>

    @Headers("Authorization: Bearer ${Constants.SK_TOKEN}")
    @POST("v1/payment_intents")
    suspend fun getClientSecret(
        @Query("customer") customerId: String,
        @Query("amount") amount: Int,
        @Query("currency") currency: String,
        @Query("automatic_payment_methods[enabled]") automaticPaymentMethodsEnabled: Boolean
    ): Response<PaymentRequest>
}