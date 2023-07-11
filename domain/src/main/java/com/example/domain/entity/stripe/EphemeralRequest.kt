package com.example.domain.entity.stripe

import com.example.domain.entity.stripe.customer.AssociatedObject
import com.squareup.moshi.Json

data class EphemeralRequest(
    @Json(name = "associated_objects")
    val associated_objects: List<AssociatedObject>,
)