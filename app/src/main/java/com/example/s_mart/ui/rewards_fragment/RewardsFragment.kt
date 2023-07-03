package com.example.s_mart.ui.rewards_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.s_mart.R
import com.example.s_mart.core.adapters.RewardsAdapter
import com.example.s_mart.databinding.FragmentRewardsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RewardsFragment : Fragment() {

    private var _binding: FragmentRewardsBinding? = null
    private val binding get() = _binding!!

    private val voucherViewModel: VoucherViewModel by viewModels()
    private val adapter = RewardsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRewardsBinding.inflate(inflater, container, false)

        binding.rvVouchers.adapter = adapter

        onRedeemClicked()

        lifecycleScope.launch {
            voucherViewModel.client.collect {
                it?.let {
                    binding.client = it
                }
            }
        }

        lifecycleScope.launch {
            voucherViewModel.vouchers.collect { vouchers ->
                if (vouchers.isNotEmpty()) {
                    adapter.submitList(vouchers)
                }
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun onRedeemClicked() {
        lifecycleScope.launch {
            voucherViewModel.client.collect {
                it?.let {
                    adapter.onRedeemClicked = { voucher ->
                        if (!voucherViewModel.updateClient(voucher, it)) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.insufficient_balance),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}