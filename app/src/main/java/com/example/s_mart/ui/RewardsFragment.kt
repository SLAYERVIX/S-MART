package com.example.s_mart.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.domain.entity.Client
import com.example.domain.entity.Voucher
import com.example.s_mart.R
import com.example.s_mart.core.adapters.RewardsAdapter
import com.example.s_mart.core.callbacks.RedeemCallBack
import com.example.s_mart.core.utils.Constants
import com.example.s_mart.databinding.FragmentRewardsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class RewardsFragment : Fragment(), RedeemCallBack {

    private var _binding: FragmentRewardsBinding? = null
    private val binding get() = _binding!!

    private lateinit var fireStore: FirebaseFirestore
    private lateinit var vouchersReference: CollectionReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var clientsReference: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        vouchersReference =
            fireStore.collection(Constants.VOUCHERS_REF)

        clientsReference =
            fireStore.collection(Constants.CLIENTS_REF).document(firebaseAuth.currentUser!!.uid)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRewardsBinding.inflate(inflater, container, false)


        val adapter = RewardsAdapter(this)
        binding.rvVouchers.adapter = adapter

        clientsReference.get().addOnCompleteListener { task ->
            task.addOnSuccessListener {
                val client = it.toObject(Client::class.java)

                client?.let {
                    binding.tvPoints.text = client.points.toString()
                }
            }
            task.addOnFailureListener {

            }
        }


        vouchersReference.get().addOnCompleteListener { task ->
            task.addOnSuccessListener { snapshot ->
                val vouchers = ArrayList<Voucher>()
                for (snap in snapshot) {
                    val voucher = snap.toObject(Voucher::class.java)
                    vouchers.add(voucher)
                }
                adapter.submitList(vouchers)
            }
            task.addOnFailureListener {

            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onRedeemClicked(item: Voucher) {
        clientsReference.get().addOnCompleteListener { task ->
            task.addOnSuccessListener {
                val client = it.toObject(Client::class.java)
                client?.let {
                    if (client.points >= item.cost) {
                        client.points -= item.cost
                        client.voucher = item

                        clientsReference.set(client)
                    }
                    else {
                        Toast.makeText(requireContext(), getString(R.string.insufficient_balance), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            task.addOnFailureListener {

            }
        }
    }
}