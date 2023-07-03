package com.example.s_mart.ui.checkout

import androidx.lifecycle.ViewModel
import com.example.s_mart.core.utils.Validation


class CheckoutViewModel : ViewModel() {
    fun isEmptyField(field : String) : Boolean = Validation.isEmptyField(field)

    fun validateCreditCardLength(card: String): Boolean {
        return Validation.validateCreditCardNumbersLength(card)
    }

    fun validateCreditCardFormat(card: String): Boolean {
        return Validation.validateCreditCardFormat(card)
    }

}