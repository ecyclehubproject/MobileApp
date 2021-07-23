package com.cyclehub.ui.checkout

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.cyclehub.BuildConfig
import com.cyclehub.R
import com.cyclehub.databinding.CheckOutFragmentBinding
import com.cyclehub.model.*
import com.cyclehub.utils.PermissionUtils.isLocationPermissionGranted
import com.cyclehub.utils.PermissionUtils.requestLocationPermission
import com.cyclehub.utils.Resource
import com.cyclehub.utils.Utils
import com.cyclehub.utils.Utils.getBitmap
import com.cyclehub.utils.bitMapToString
import com.cyclehub.utils.setOnSingleClickListener
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.schibstedspain.leku.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CheckOutFragment : Fragment() {
    private val checkOutViewModel: CheckOutViewModel by viewModels()
    private lateinit var binding: CheckOutFragmentBinding
    private val args: CheckOutFragmentArgs by navArgs()
    private lateinit var selectedRadioButton: AppCompatRadioButton
    private var mediaId: String = ""
    private var totalCharge: Double = 0.0
    private var city = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CheckOutFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val basePrice = args.purchasedata.price
        val commuteCharges = 0.0
        val sGstCharges = (basePrice * 9.toDouble()) / 100
        val cGstCharges = (basePrice * 9.toDouble()) / 100
        totalCharge = basePrice + cGstCharges + sGstCharges + commuteCharges
        binding.orderDetails.text = args.purchasedata.serviceName
        binding.baseCharges.text = getString(R.string.amount, basePrice)
        binding.commuteCharges.text = getString(R.string.amount, commuteCharges)
        binding.sgst.text = getString(R.string.amount, sGstCharges)
        binding.cgst.text = getString(R.string.amount, cGstCharges)
        binding.totalCharges.text = getString(R.string.amount, totalCharge)
        checkOutViewModel.getLastAddress()
        checkOutViewModel.res.observe(viewLifecycleOwner,
            {
                when (it.status) {

                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let { res ->
                            if (res.msg == "success") {
                                if (!res.model.isNullOrEmpty()) {
                                    binding.orderButton.isEnabled = true
                                    binding.addressText.text = getString(
                                        R.string.address_format,
                                        res.model[res.model.size - 1].address1,
                                        res.model[res.model.size - 1].address2,
                                        res.model[res.model.size - 1].address3
                                    )
                                    val pin = res.model[res.model.size - 1].pincode.toIntOrNull()
                                    if (pin != null) {
                                        city = res.model[res.model.size - 1].city
                                        checkOutViewModel.servesPinCode(
                                            pin
                                        )
                                    }
                                }
                            } else {
                                binding.orderButton.isEnabled = false
                                binding.addUpdateAddress.text = getString(R.string.add_address)
                            }
                        }
                    }
                    Resource.Status.LOADING -> {
                        showProgress(true)
                    }
                    Resource.Status.ERROR -> {
                        showProgress(false)
                        Snackbar.make(
                            binding.root,
                            it.message.toString(),
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            })
        checkOutViewModel.servespincode.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    showProgress(false)
                    it?.data?.model?.let { serves ->
                        if (!serves.servesPinCode) {
                            binding.emptyView.isVisible = true
                            binding.orderButton.isVisible = false
                            binding.emptyView.text =
                                "Sorry, Our services aren't available in $city yet. Coming soon to your location."
                        } else {
                            binding.orderButton.isVisible = true
                            binding.emptyView.isVisible = false
                        }
                    }


                }
                Resource.Status.ERROR -> {
                    showProgress(false)
                }


                Resource.Status.LOADING ->
                    showProgress(true)
            }
        })
        binding.orderButton.setOnSingleClickListener {
            if (binding.brand.text.isNullOrEmpty()) {
                binding.brand.error = "Brand Cannot be Empty"
            } else if (binding.addressText.text.isNullOrEmpty()) {
                Toast.makeText(context, "Please add address.", Toast.LENGTH_SHORT).show()
            } else {
                val selectedRadioButtonId: Int = binding.paymentOption.checkedRadioButtonId
                if (selectedRadioButtonId != -1) {
                    selectedRadioButton = binding.paymentOption.findViewById(selectedRadioButtonId)
                    val string: String = selectedRadioButton.text.toString()
                    val transactionType =
                        if (string.equals(getString(R.string.cash_on_service), false))
                            "offline"
                        else
                            "online"
                    checkOutViewModel.startPayment(
                        TransactionRequest(
                            totalCharge.times(100),
                            transactionType,
                            args.purchasedata.productId.toInt()
                        )
                    )

                }
            }
        }

        checkOutViewModel.purchase.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        val orderExtraInfo = OrderExtraInfo(
                            mediaId,
                            binding.brand.text.toString(),
                            binding.spareParts.text.toString(),
                            binding.additionalInfo.text.toString()
                        )
                        it?.data?.let { res ->
                            showProgress(false)
                            if (res.msg == "success") {
                                if (res.model.mode.equals("online", false)) {
                                    val orderDetails = OrderDetails(
                                        res.model.id,
                                        res.model.transactionId,
                                        res.model.amount,
                                    )
                                    findNavController().navigate(
                                        CheckOutFragmentDirections.actionCheckOutFragmentToPaymentFragment(
                                            args.purchasedata,
                                            orderDetails,
                                            orderExtraInfo
                                        )
                                    )
                                } else {

                                    checkOutViewModel.confirmPayment(
                                        PaymentRequest(
                                            res.model.id,
                                            "offline",
                                            status = "success",
                                            mediaId = orderExtraInfo.mediaId,
                                            brandInfo = orderExtraInfo.brandsInfo,
                                            partsInfo = orderExtraInfo.partsInfo,
                                            deception = orderExtraInfo.additionalInfo
                                        )
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
                        Snackbar.make(
                            binding.root,
                            it.message.toString(),
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            })
        checkOutViewModel.payment.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let { res ->
                            if (res.msg == "success") {
                                showProgress(false)
                                findNavController().navigate(
                                    CheckOutFragmentDirections.actionCheckOutFragmentToOrderStatusFragment1(
                                        true,
                                        res.model.transactionId
                                    )
                                )
                            } else {
                                findNavController().navigate(
                                    CheckOutFragmentDirections.actionCheckOutFragmentToOrderStatusFragment1(
                                        false,
                                        res.model.transactionId
                                    )
                                )
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
                        Snackbar.make(
                            binding.root,
                            it.message.toString(),
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            })
        checkOutViewModel.imageUpload.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let { res ->
                            if (res.msg == "success") {
                                mediaId = res.model.mediaId
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
        binding.addUpdateAddress.setOnSingleClickListener {
            if (isLocationPermissionGranted(binding.addUpdateAddress.context)) {
                addAddress()
            } else {
                requestLocationPermission(requireActivity())
            }
        }
        binding.uploadImage.setOnSingleClickListener {
            ImagePicker.with(this)
                .compress(512)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    showProgress(true)
                    startForProfileImageResult.launch(intent)
                }
        }
        binding.uploadImageText.setOnSingleClickListener {
            ImagePicker.with(this)
                .compress(512)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    showProgress(true)
                    startForProfileImageResult.launch(intent)
                }
        }

    }

    private val startForAddressResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val latitude = data?.getDoubleExtra(LATITUDE, 0.0)
                    val longitude = data?.getDoubleExtra(LONGITUDE, 0.0)
                    val address = data?.getStringExtra(LOCATION_ADDRESS)
                    val locationData = Utils.geoAddress(
                        binding.root.context,
                        latitude!!,
                        longitude!!
                    )
                    city = locationData?.city.toString()
                    locationData?.pincode?.toInt()?.let {
                        checkOutViewModel.servesPinCode(
                            it
                        )
                    }
                    if (locationData != null) {
                        checkOutViewModel.addAddress(
                            AddressRequest(
                                address.toString(),
                                locationData.city,
                                getString(
                                    R.string.state_pincode,
                                    locationData.state,
                                    locationData.city
                                ),
                                locationData.city,
                                locationData.latitude, locationData.longitude,
                                locationData.pincode,
                                locationData.state
                            )
                        )
                    }
                    binding.addressText.text = address.toString()

                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(activity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            showProgress(false)
            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data as Uri
                    lifecycleScope.launch {
                        val bitmap = getBitmap(requireContext(), fileUri)
                        binding.uploadImage.load(fileUri)
                        checkOutViewModel.uploadImage(
                            ImageUploadRequest(
                                bitmap.bitMapToString(),
                                "${System.currentTimeMillis()}.jpeg"
                            )
                        )
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(activity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun showProgress(boolean: Boolean) {
        binding.progress.progressBar.isVisible = boolean
    }

    private fun addAddress() {
        val locationPickerIntent = LocationPickerActivity.Builder()
            .withGooglePlacesApiKey(BuildConfig.GMP_KEY)
            .withGeolocApiKey(BuildConfig.GMP_KEY)
            .withDefaultLocaleSearchZone()
            .withSatelliteViewHidden()
            .withGoogleTimeZoneEnabled()
            .withUnnamedRoadHidden().withMapStyle(R.raw.map_style)
            .build(binding.addUpdateAddress.context)
        locationPickerIntent.putExtra(ENABLE_LOCATION_PERMISSION_REQUEST, true)
        locationPickerIntent.putExtra(SEARCH_ZONE,"en_IN")
        startForAddressResult.launch(locationPickerIntent)
    }
}
