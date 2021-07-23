package com.cyclehub.ui.myorder

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
import com.cyclehub.databinding.MyOrdersFragmentBinding
import com.cyclehub.other.Constants
import com.cyclehub.utils.Resource
import com.cyclehub.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyOrdersFragment : Fragment() {

    lateinit var binding: MyOrdersFragmentBinding
    private val myOrdersViewModel: MyOrdersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyOrdersFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myOrdersViewModel.getOrders()
        myOrdersViewModel.orders.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let { res ->
                            if (!res.model.orders.isNullOrEmpty() && !res.model.services.isNullOrEmpty() && !res.model.transactions.isNullOrEmpty() && !res.model.users.isNullOrEmpty()) {
                                binding.myOrders.layoutManager = LinearLayoutManager(context)
                                binding.myOrders.setHasFixedSize(true)
                                binding.myOrders.adapter = MyOrderAdapter(
                                    res.model.orders.toTypedArray(),
                                    res.model.services.toTypedArray(),
                                    res.model.transactions.toTypedArray(),
                                    res.model.users.toTypedArray()
                                ) { order ->
                                    findNavController().navigate(
                                        MyOrdersFragmentDirections.actionMyOrdersFragmentToMyOrderDetailsFragment(
                                            order.id.toString(), Constants.CUSTOMER
                                        )
                                    )
                                }

                            } else {
                                Utils.showEmptyView(
                                    binding.myOrders,
                                    binding.emptyView,
                                    "No Orders Available"
                                )
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