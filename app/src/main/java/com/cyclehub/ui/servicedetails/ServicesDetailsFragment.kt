package com.cyclehub.ui.servicedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyclehub.R
import com.cyclehub.databinding.FragmentServicesDetailsBinding
import com.cyclehub.model.PurchaseData
import com.cyclehub.utils.Resource
import com.cyclehub.utils.loadAnyUrl
import com.cyclehub.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ServicesDetailsFragment : Fragment() {
    lateinit var binding: FragmentServicesDetailsBinding
    private val servicesDetailsViewModel: ServicesDetailsViewModel by viewModels()
    var price: Int = 0
    var productId: String = ""
    var vehicleId: String = ""
    val args: ServicesDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServicesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productId = args.serviceId
        servicesDetailsViewModel.getServicesDetails(args.serviceId)
        servicesDetailsViewModel.res.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        it?.data?.let { res ->
                            if (res.msg == "success") {
                                binding.serviceTitle.text = res.model.name
                                binding.servicesDescription.layoutManager =
                                    LinearLayoutManager(context)
                                binding.servicesDescription.adapter = ServicesDescriptionAdapter(
                                    res.model.description.split("|||").map { it -> it.trim() }
                                )
                                price = res.model.amount
                                binding.price.text =
                                    getString(R.string.amount, res.model.amount.toDouble())
                                binding.promotionViewpager.loadAnyUrl(res.model.image)
                            } else {
                                Toast.makeText(binding.root.context, res.msg, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        showProgress(false)
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

        binding.buyButton.setOnSingleClickListener {
            val purchaseData =
                PurchaseData(
                    binding.serviceTitle.text.toString(),
                    price.toDouble(),
                    productId
                )
            findNavController().navigate(
                ServicesDetailsFragmentDirections.actionServicesDetailsFragmentToCheckOutFragment(
                    purchaseData
                )
            )
        }
    }

    private fun showProgress(boolean: Boolean) {
        binding.progress.progressBar.isVisible = boolean
    }


}