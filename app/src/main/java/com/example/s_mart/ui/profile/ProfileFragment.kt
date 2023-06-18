package com.example.s_mart.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.ProfileItem
import com.example.s_mart.R
import com.example.s_mart.core.adapters.ProfileAdapter
import com.example.s_mart.databinding.FragmentProfileBinding
import com.example.s_mart.ui.SmartViewModel
import java.util.UUID

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SmartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val adapter = ProfileAdapter()
        binding.rvProfile.adapter = adapter

        val profileItems = listOf(
            ProfileItem(
                UUID.randomUUID().toString(),
                "Orders",
                R.drawable.order
            ),
            ProfileItem(
                UUID.randomUUID().toString(),
                "Vouchers",
                R.drawable.baseline_local_offer_24
            )
        )

        adapter.submitList(profileItems)

        binding.btnLogout.setOnClickListener {
            viewModel.firebaseAuth.signOut()

            if (viewModel.firebaseAuth.currentUser == null) {

                // Navigate to LoginFragment
                findNavController().navigate(R.id.action_global_login)
            }
        }

        viewModel.firebaseAuth.currentUser?.let {
            binding.tvDisplayName.text = it.displayName
            binding.tvEmail.text = it.email
        }

        return binding.root
    }
}