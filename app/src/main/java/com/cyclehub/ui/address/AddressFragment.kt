package com.cyclehub.ui.address

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cyclehub.R
import com.cyclehub.databinding.AddressFragmentBinding
import com.cyclehub.model.AddressRequest
import com.cyclehub.other.Validator
import com.cyclehub.utils.Resource
import com.cyclehub.utils.setOnSingleClickListener
import com.cyclehub.utils.toEditable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressFragment : Fragment() {

    private val addressViewModel: AddressViewModel by viewModels()
    private lateinit var binding: AddressFragmentBinding
    private val args: AddressFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = AddressFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val locationData = args.locationData
        binding.addressLine1.setText(locationData.address)
        binding.city.setText(locationData.city)
        binding.pincode.setText(locationData.pincode)
        val stateCountry = String.format(
            getString(R.string.state_country),
            locationData.state,
            locationData.country
        )
        binding.state.setText(stateCountry)

        binding.addAddressButton.setOnSingleClickListener {
            if (Validator.isEmpty(binding.name.text.toString().trim()) &&
                Validator.isEmpty(binding.addressLine1.text.toString().trim()) &&
                Validator.isEmpty(binding.city.text.toString().trim()) &&
                Validator.isEmpty(binding.pincode.text.toString().trim()) &&
                Validator.isEmpty(binding.state.text.toString().trim())
            ) {
                addressViewModel.addAddress(
                    AddressRequest(
                        binding.name.text.toString()
                            .trim() + " " + binding.addressLine1.text.toString().trim(),
                        binding.city.text.toString().trim(),
                        getString(
                            R.string.state_pincode,
                            binding.state.text.toString().trim(),
                            binding.pincode.text.toString().trim()
                        ),
                        binding.city.text.toString().trim(),
                        locationData.latitude.toString(), locationData.longitude.toString(),
                        binding.pincode.text.toString().trim(),
                        binding.state.text.toString().trim()
                    )
                )
            }
        }
        addressViewModel.user.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if (it.data != null) {
                        binding.name.text = it.data.name.toEditable()

                    } else {
                        Toast.makeText(
                            binding.root.context,
                            it.message.toString(),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }
                Resource.Status.ERROR -> {
                    Toast.makeText(binding.root.context, it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                Resource.Status.LOADING -> {
                }
            }
        })

        addressViewModel.res.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        it?.data?.let { res ->
                            if (res.msg == "success") {
                                findNavController().navigate(
                                    AddressFragmentDirections.actionAddressFragmentToCheckOutFragment(args.purchasedata)
                                )
                            } else {
                                Toast.makeText(binding.root.context, res.msg, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                    Resource.Status.LOADING -> {

                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(
                            binding.root.context,
                            it.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            })
    }
}
