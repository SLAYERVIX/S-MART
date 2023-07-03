package com.example.s_mart.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.s_mart.R
import com.example.s_mart.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val adapter = ProfileAdapter()
        binding.rvProfile.adapter = adapter

        adapter.submitList(profileViewModel.profileItems)

        binding.btnLogout.setOnClickListener {
            if (profileViewModel.firebaseSignOut()) {
                // Navigate to LoginFragment
                findNavController().navigate(R.id.action_global_login)
            }
        }

        profileViewModel.currentUser?.let {
            binding.user = it
        }

        return binding.root
    }
}