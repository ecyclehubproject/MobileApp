package com.cyclehub.ui.mybike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cyclehub.databinding.MyBikeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyBikeFragment : Fragment() {

    companion object {
        fun newInstance() = MyBikeFragment()
    }

    private lateinit var binding: MyBikeFragmentBinding
    private val viewModel: MyBikeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyBikeFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}