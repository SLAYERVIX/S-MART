package com.example.s_mart.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.Client
import com.example.s_mart.R
import com.example.s_mart.core.utils.Constants
import com.example.s_mart.core.utils.Date
import com.example.s_mart.databinding.FragmentCheckoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.roundToInt

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var fireStore: FirebaseFirestore
    private lateinit var clientsReference: DocumentReference
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        clientsReference =
            fireStore.collection(Constants.CLIENTS_REF).document(firebaseAuth.currentUser!!.uid)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)

        binding.btnConfirmPayment.setOnClickListener {
            validatePayment()
        }

        setTotalPrice()


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setTotalPrice() {
        clientsReference.get().addOnCompleteListener { task ->
            task.addOnSuccessListener { snapshot ->
                val client = snapshot.toObject(Client::class.java)
                client?.let {
                    binding.tvPrice.text = "EGP ${it.cart.totalPrice}"
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

        clientsReference.get().addOnCompleteListener { task ->
            task.addOnSuccessListener { snapshot ->
                val client = snapshot.toObject(Client::class.java)
                client?.let {
                    it.points += it.cart.totalPrice.roundToInt()

                    it.orderHistory.products.addAll(it.cart.products)
                    it.orderHistory.totalPrice = it.cart.totalPrice
                    it.orderHistory.date = Date.date

                    it.cart.products.clear()
                    it.cart.totalPrice = 0.0



                    clientsReference.set(it).addOnSuccessListener {
                        findNavController().navigate(R.id.action_checkoutFragment_to_paymentSuccesfulFragment)
                    }
                }
            }
            task.addOnFailureListener {

            }
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