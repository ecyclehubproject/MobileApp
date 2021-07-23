package com.cyclehub.ui.vendorlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.birjuvachhani.locus.Locus
import com.cyclehub.R
import com.cyclehub.databinding.VendorLoginFragmentBinding
import com.cyclehub.model.LocationData
import com.cyclehub.model.VendorSignupRequest
import com.cyclehub.other.Constants
import com.cyclehub.other.Validator
import com.cyclehub.utils.Resource
import com.cyclehub.utils.Utils
import com.cyclehub.utils.toEditable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VendorLoginFragment : Fragment() {
    private lateinit var binding: VendorLoginFragmentBinding
    private val viewModel: VendorLoginViewModel by viewModels()
    private lateinit var locationData: LocationData
    private var userTypeString: String = Constants.VENDOR

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = VendorLoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress(false)
        Locus.getCurrentLocation(binding.pincode.context) { result ->
            result.location?.let {
                locationData = Utils.geoAddress(
                    binding.pincode.context,
                    it.latitude,
                    it.longitude
                )!!
                binding.pincode.text = locationData.pincode.toEditable()
                binding.city.text = locationData.city.toEditable()
                binding.state.text = locationData.state.toEditable()
            }
            result.error?.let { binding.pincode.isVisible = false }
        }
        binding.signupButton.setOnClickListener {
            if (Validator.isEmpty(binding.name.text.toString().trim()) &&
                Validator.isEmpty(binding.phone.text.toString().trim()) &&
                Validator.isEmpty(binding.email.text.toString().trim()) &&
                Validator.isEmpty(binding.addressLine1.text.toString().trim()) &&
                Validator.isEmpty(binding.city.text.toString().trim()) &&
                Validator.isEmpty(binding.pincode.text.toString().trim()) &&
                Validator.isEmpty(binding.state.text.toString().trim())
            ) {
                viewModel.vendorSignUp(
                    VendorSignupRequest(
                        binding.name.text.toString()
                            .trim() + " " + binding.addressLine1.text.toString()
                            .trim(),
                        binding.city.text.toString().trim(),
                        getString(
                            R.string.state_pincode,
                            binding.state.text.toString().trim(),
                            binding.pincode.text.toString().trim()
                        ),
                        binding.city.text.toString().trim(),
                        binding.email.text.toString().trim(),
                        locationData.latitude.toString(),
                        locationData.longitude.toString(),
                        binding.name.text.toString().trim(),
                        binding.phone.text.toString().trim(),
                        binding.pincode.text.toString().trim(),
                        userTypeString,
                        binding.state.text.toString().trim()
                    )
                )
            }
        }

        viewModel.res.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    showProgress(false)
                    it.data?.let { res ->
                        if (res.msg == "success") {
                            findNavController().navigate(
                                VendorLoginFragmentDirections.actionVendorLoginFragmentToOtpValidationFragment(
                                    binding.phone.text.toString().trim()
                                )
                            )

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

    private fun showProgress(boolean: Boolean) {
        binding.progress.progressBar.isVisible = boolean
    }
}