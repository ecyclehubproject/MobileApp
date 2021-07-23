package com.cyclehub.ui.orderdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cyclehub.R
import com.cyclehub.databinding.MyOrderDetailsFragmentBinding
import com.cyclehub.db.DataManager
import com.cyclehub.other.Constants
import com.cyclehub.other.Constants.CANCEL_ORDER_STATUS
import com.cyclehub.other.Constants.COMPLETED_ORDER_STATUS
import com.cyclehub.other.Constants.CONFIRM_ORDER_STATUS
import com.cyclehub.other.Constants.CREATED_ORDER_STATUS
import com.cyclehub.other.Constants.PENDING_ORDER_STATUS
import com.cyclehub.utils.Resource
import com.cyclehub.utils.Utils.boldTitleString
import com.cyclehub.utils.Utils.showImage
import com.cyclehub.utils.colorSpannableStringWithGrayOneStart
import com.cyclehub.utils.loadSvgFromUrl
import com.cyclehub.utils.setOnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.toInstant
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class MyOrderDetailsFragment : Fragment(R.layout.my_order_details_fragment) {
    private val args: MyOrderDetailsFragmentArgs by navArgs()
    private lateinit var binding: MyOrderDetailsFragmentBinding
    private val myOrderDetailsViewModel: MyOrderDetailsViewModel by viewModels()
    private var vendorNumber = ""
    private var engineerNumber = ""
    private var orderId = ""
    private var userNumber = ""
    private var userLatitude = 0.0
    private var userLongitude = 0.0

    @Inject
    lateinit var dataManager: DataManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyOrderDetailsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress(true)
        when (args.from) {
            Constants.VENDOR -> {
                binding.serviceProvider.isVisible = false
                binding.serviceProviderEngineer.isVisible = true
                binding.serviceProviderUser.isVisible = true
            }
            Constants.ENGINEER -> {
                binding.serviceProviderEngineer.isVisible = false
                binding.serviceProviderUser.isVisible = true
                binding.serviceProvider.isVisible = true

            }
            else -> {
                binding.serviceProviderUser.isVisible = false
            }

        }
        myOrderDetailsViewModel.getOrderDetails(args.orderid)
        myOrderDetailsViewModel.orderDetails.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        it?.data?.let { res ->
                            if (res.msg == "success") {
                                val data = res.model
                                val order = data.order
                                val user =
                                    data.users.firstOrNull { user -> user.id.toInt() == order.customerId }
                                val vendor =
                                    data.users.firstOrNull { vendor -> vendor.id.toInt() == order.vendorId }
                                val engineer =
                                    data.users.firstOrNull { engineer -> engineer.id.toInt() == order.engineerId }
                                orderId = order.orderId
                                userLatitude = user?.latitude?.toDoubleOrNull() ?: 0.0
                                userLongitude = user?.longitude?.toDoubleOrNull() ?: 0.0
                                userNumber = user?.phoneNumber ?: ""
                                myOrderDetailsViewModel.getServiceByID(data.order.serviceId)
                                binding.userName.text = "User Name ${user?.name}"
                                binding.serviceName.text = data.service.name
                                data.service.icon.let { serviceImg ->
                                    binding.serviceImage.loadSvgFromUrl(serviceImg)
                                }
                                binding.serviceAmountTitle.text =
                                    data.service.name.plus(" Charges")
                                binding.nameUser.text = user?.name
                                if (order.partsInfo.isNotEmpty()) binding.partsInfo.text =
                                    boldTitleString("Parts Info : ", order.partsInfo)
                                else binding.partsInfo.isVisible = false
                                if (order.brandInfo.isNotEmpty())
                                    binding.brandInfo.text =
                                        boldTitleString("Brand Info : ", order.brandInfo)
                                else {
                                    binding.brandInfo.isVisible = false
                                    binding.orderExtraDetailsTitle.isVisible = false
                                    binding.orderExtraDetails.isVisible = false
                                    binding.userRequestImage.isVisible = false
                                }
                                if (!res.model.media.isNullOrEmpty()) {
                                    binding.userRequestImage.loadSvgFromUrl(res.model.media[0].mediaPath)
                                    binding.userRequestImage.setOnSingleClickListener {
                                        showImage(
                                            binding.userRequestImage.context,
                                            res.model.media[0].mediaPath
                                        )
                                    }
                                } else
                                    binding.userRequestImage.isVisible = false

                                if (!res.model.comments.isNullOrEmpty())
                                    binding.additionalInfoComment.text = boldTitleString(
                                        "User Comment : ",
                                        res.model.comments[0].deception
                                    )
                                else binding.additionalInfoComment.isVisible = false

                                binding.userAddress.text =
                                    user?.address1.plus(user?.address2).plus(user?.address3)
                                        .replace("null", "")
                                val orderDate =
                                    data.order.createdOn.toInstant().toEpochMilliseconds()
                                val now: Long = Clock.System.now().toEpochMilliseconds()
                                if (data.order.status == CREATED_ORDER_STATUS) binding.changeOrderStatus.isVisible =
                                    true
                                if (args.from == Constants.VENDOR || args.from == Constants.ENGINEER) binding.changeOrderStatus.isVisible =
                                    false
                                val defenceInDay =
                                    TimeUnit.DAYS.convert(
                                        now.minus(orderDate),
                                        TimeUnit.MILLISECONDS
                                    ).toInt()

                                val orderOnText = when {
                                    defenceInDay < 1 -> getString(R.string.order_on_less_day)
                                    defenceInDay == 1 -> getString(
                                        R.string.order_on_one_day,
                                        defenceInDay
                                    )
                                    else -> getString(
                                        R.string.order_on_more_day,
                                        defenceInDay
                                    )
                                }
                                binding.orderOn.text = orderOnText

//Set service provider info
                                when (order.status.uppercase()) {
                                    CREATED_ORDER_STATUS, CANCEL_ORDER_STATUS, PENDING_ORDER_STATUS -> {
                                    }
                                    CONFIRM_ORDER_STATUS -> {
                                        binding.serviceProviderTitle.isVisible = true
                                        if (args.from != Constants.VENDOR && args.from != Constants.ENGINEER) binding.serviceProvider.isVisible =
                                            true

                                    }
                                    COMPLETED_ORDER_STATUS -> {
                                        when (args.from) {
                                            Constants.VENDOR, Constants.ENGINEER -> {
                                                binding.downloadInvoice.isVisible = false
                                            }
                                            else -> {
                                                binding.serviceProviderTitle.isVisible = true
                                                binding.serviceProvider.isVisible = true
                                                binding.serviceProviderEngineer.isVisible = true
                                                binding.downloadInvoice.isVisible = true
                                            }
                                        }

                                    }
                                    else -> {
                                        binding.serviceProviderTitle.isVisible = true
                                        if (args.from != Constants.VENDOR) binding.serviceProvider.isVisible =
                                            true
                                        if (args.from != Constants.ENGINEER) binding.serviceProviderEngineer.isVisible =
                                            true
                                    }
                                }
                                if (data.order.vendorId != 0) {
                                    vendorNumber = vendor?.phoneNumber ?: ""
                                    val vendorName = vendor?.name ?: ""

                                    val vendorAddress =
                                        vendor?.address1 ?: "".plus(vendor?.address2 ?: "")
                                            .plus(vendor?.address3 ?: "")
                                    binding.vendorName.colorSpannableStringWithGrayOneStart(
                                        getString(R.string.vendor_name),
                                        vendorName
                                    ) {}
                                    if (vendorAddress.isEmpty())
                                        binding.vendorAddress.text = ""
                                    else
                                        binding.vendorAddress.colorSpannableStringWithGrayOneStart(
                                            getString(R.string.address),
                                            vendorAddress
                                        ) {}

                                }
                                if (data.order.engineerId != 0) {
                                    engineerNumber = engineer?.phoneNumber ?: ""
                                    val engineerName = engineer?.name ?: ""
                                    binding.enginnerName.colorSpannableStringWithGrayOneStart(
                                        getString(R.string.engineer_name),
                                        engineerName
                                    ) {}
                                }
                                //Set order info

                                binding.serviceAmount.text =
                                    getString(R.string.amount, res.model.transactions.amount)
                                binding.totalCharges.text =
                                    getString(R.string.amount, res.model.transactions.amount)
                                val paymentMode =
                                    if (res.model.transactions.mode == "offline") getString(R.string.cash_on_service) else getString(
                                        R.string.online_payment
                                    )
                                binding.paymentStatus.text = paymentMode
                            } else {
                                Toast.makeText(binding.root.context, res.msg, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        lifecycleScope.launchWhenCreated {
                            delay(100)
                            showProgress(false)
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
        binding.changeOrderStatus.setOnClickListener {
            cancelOrderDialog()
        }
        myOrderDetailsViewModel.cancelOrder.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        it?.data?.let { res ->
                            if (res.msg == "success") {
                                Toast.makeText(
                                    activity,
                                    "Order has been canceled",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.changeOrderStatus.isVisible = false
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

        binding.whatsapp.setOnSingleClickListener {
            startSupportChat(vendorNumber)
        }
        binding.phone.setOnSingleClickListener {
            startCall(vendorNumber)
        }
        binding.whatsappEng.setOnSingleClickListener {
            startSupportChat(engineerNumber)
        }
        binding.phoneEng.setOnSingleClickListener {
            startCall(engineerNumber)
        }
        binding.whatsappUser.setOnSingleClickListener {
            startSupportChat(userNumber)
        }
        binding.phoneUser.setOnSingleClickListener {
            startCall(userNumber)
        }
        binding.userVendor.setOnSingleClickListener {
            Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
        }
        binding.emailEng.setOnSingleClickListener {
            Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
        }
        binding.emailUser.setOnSingleClickListener {
            openMap(userLatitude, userLongitude)

        }
        binding.downloadInvoice.setOnClickListener {
            findNavController().navigate(
                MyOrderDetailsFragmentDirections.actionMyOrderDetailsFragmentToInvoiceFragment(
                    args.orderid
                )
            )
        }
    }

    private fun showProgress(boolean: Boolean) {
        binding.progress.progressBar.isVisible = boolean
    }

    private fun startSupportChat(number: String) {
        if (number.isNotEmpty())
            try {
                val headerReceiver =
                    "Hi I need help with order id $orderId" // Replace with your message.
                val trimToNumber = "+91$number" //10 digit number
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://wa.me/$trimToNumber/?text=$headerReceiver")
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        else {
            Toast.makeText(context, "Number is not valid", Toast.LENGTH_SHORT).show()
        }
    }


    private fun startCall(number: String) {
        if (number.isNotEmpty())
            startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null)))
        else
            Toast.makeText(context, "Number is not valid", Toast.LENGTH_SHORT).show()
    }

    private fun openMap(latitude: Double, longitude: Double) {
        val gmmIntentUri = Uri.parse("google.navigation:q=${latitude},${longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }


    private fun cancelOrderDialog() {
        val dialog = BottomSheetDialog(binding.changeOrderStatus.context)
        val bottomSheet = layoutInflater.inflate(R.layout.cancel_order_bottom_sheet, null)
        dialog.setContentView(bottomSheet)
        val dismissDialog = bottomSheet.findViewById<AppCompatButton>(R.id.dismiss)
        val cancelOrder = bottomSheet.findViewById<AppCompatButton>(R.id.cancel_order)
        val subTitle = bottomSheet.findViewById<AppCompatEditText>(R.id.sub_title)
        dialog.dismissWithAnimation = true
        dismissDialog.setOnSingleClickListener {
            dialog.dismiss()
        }
        cancelOrder.setOnSingleClickListener {
            if (subTitle.text.isNullOrBlank())
                subTitle.error = "This field is required"
            else {
                dialog.dismiss()
                myOrderDetailsViewModel.cancelOrder(args.orderid, subTitle.text.toString())
            }
        }
        dialog.show()
    }
}



