package com.example.s_mart.ui.coins_market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.s_mart.databinding.FragmentCoinsMarketBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoinsMarketFragment : Fragment() {

    private var _binding: FragmentCoinsMarketBinding? = null
    private val binding get() = _binding!!
    private val coinsMarketViewModel: CoinsMarketViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoinsMarketBinding.inflate(inflater, container, false)

        val adapter = CoinsMarketAdapter()
        binding.rvCoinsProducts.adapter = adapter

        lifecycleScope.launch {
            coinsMarketViewModel.retrieveCoinsProducts().collect {
                adapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            coinsMarketViewModel.retrieveClient().collect {
                it?.let { client ->
                    binding.client = client
                }
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}