package com.example.s_mart.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.s_mart.R
import com.example.s_mart.core.utils.Constants
import com.example.s_mart.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()

            if (firebaseAuth.currentUser == null) {
                // Navigate to LoginFragment
                findNavController().navigate(R.id.action_global_login)
            }
        }

        firebaseAuth.currentUser?.let {
            binding.tvDisplayName.text = it.displayName
            binding.tvEmail.text = it.email
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}