package com.cyclehub.ui.mytransactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyclehub.databinding.MyTransactionsFragmentBinding
import com.cyclehub.utils.Resource
import com.cyclehub.utils.Utils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTransactionsFragment : Fragment() {
    private val viewModel: MyTransactionsViewModel by viewModels()
    private lateinit var binding: MyTransactionsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyTransactionsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTransactions()
        viewModel.orders.observe(viewLifecycleOwner,
            {
                when (it.status) {
                   Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let { res ->
                            if (res.msg == "success") {

                                if (!res.model.isNullOrEmpty()) {
                                    binding.myTransaction.layoutManager = LinearLayoutManager(context)
                                    binding.myTransaction.adapter = MyTransactionsAdapter(
                                        res.model.toTypedArray(),
                                        findNavController()
                                    )
                                } else {
                                    Utils.showEmptyView(
                                        binding.myTransaction,
                                        binding.emptyView,
                                        "No Transaction Available"
                                    )
                                }

                            } else {
                                Toast.makeText(binding.root.context, res.msg, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                   Resource.Status.LOADING -> {
                        showProgress(true)
                    }
                   Resource.Status.ERROR -> {
                        showProgress(false)
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }

    private fun showProgress(boolean: Boolean = false) {
        binding.progress.progressBar.isVisible = boolean
    }


}