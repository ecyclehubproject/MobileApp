package com.cyclehub.ui.payment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cyclehub.databinding.FragmentPaymentBinding
import com.cyclehub.model.PaymentRequest
import com.cyclehub.model.UserData
import com.cyclehub.ui.checkout.CheckOutViewModel
import com.cyclehub.utils.PaymentObserver
import com.cyclehub.utils.Resource
import com.google.android.material.snackbar.Snackbar
import com.razorpay.Checkout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.math.RoundingMode
import java.text.DecimalFormat

@AndroidEntryPoint
class PaymentFragment : Fragment() {
    private val args: PaymentFragmentArgs by navArgs()
    private val checkOutViewModel: CheckOutViewModel by viewModels()
    private lateinit var binding: FragmentPaymentBinding
    private var amount = 0
    private var orderId = ""
    private var user: UserData? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Checkout.preload(activity?.applicationContext)
        val num = args.orderdata.price
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING

        amount = (df.format(num).toDouble() * 100).toInt()
        orderId = args.orderdata.orderId
        checkOutViewModel.getUser()
        checkOutViewModel.user.observe(viewLifecycleOwner, {

            when (it.status) {
                Resource.Status.SUCCESS -> {
                    showProgress(false)
                    it?.data?.let { userInfo ->
                        user = userInfo.model
                    }
                }
                Resource.Status.ERROR ->
                    showProgress(false)
                Resource.Status.LOADING ->
                    showProgress(true)
            }
        })
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000)
            startPayment()
        }
        checkOutViewModel.payment.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        it?.data?.let { res ->
                            if (res.msg == "success") {
                                findNavController().navigate(
                                    PaymentFragmentDirections.actionPaymentFragmentToOrderStatusFragment(
                                        true, res.model.transactionId
                                    )
                                )
                                Toast.makeText(binding.root.context, res.msg, Toast.LENGTH_SHORT).show()
                            } else {
                                findNavController().navigate(
                                    PaymentFragmentDirections.actionPaymentFragmentToOrderStatusFragment(
                                        false, res.model.transactionId
                                    )
                                )

                                Toast.makeText(binding.root.context, res.msg, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                    Resource.Status.LOADING -> {

                    }
                    Resource.Status.ERROR -> {
                        findNavController().navigate(
                            PaymentFragmentDirections.actionPaymentFragmentToOrderStatusFragment(
                                false, args.orderdata.orderId
                            )
                        )
                        Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            })
        PaymentObserver.paymentStatusLiveData.observe(viewLifecycleOwner, {
            val paymentData = it?.paymentData
            val rzpPaymentId = it?.rzpPaymentId
            if (paymentData != null) {
                val paymentRequest =
                    if (!rzpPaymentId.isNullOrEmpty()) {
                        args.orderdata.id.let { orderId ->
                            PaymentRequest(
                                orderId,
                                "online",
                                paymentData.orderId,
                                rzpPaymentId,
                                paymentData.signature,
                                "success",
                                args.orderExtraInfo.mediaId,
                                args.orderExtraInfo.brandsInfo,
                                args.orderExtraInfo.partsInfo,
                                args.orderExtraInfo.additionalInfo,
                            )
                        }
                    } else {
                        args.orderdata.id.let { orderId ->
                            PaymentRequest(
                                orderId,
                                "online",
                                paymentData.orderId,
                                paymentData.paymentId,
                                paymentData.signature,
                                "failed",
                                args.orderExtraInfo.mediaId,
                                args.orderExtraInfo.brandsInfo,
                                args.orderExtraInfo.partsInfo,
                                args.orderExtraInfo.additionalInfo

                            )
                        }
                    }
                checkOutViewModel.confirmPayment(paymentRequest)
            }
        })
    }

    private fun startPayment() {

        val co = Checkout()
        val activity: Activity = this.requireActivity()

        try {
            val options = JSONObject()
            options.put("name", "e-CycleHub")
            options.put("description", "")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#00B76A")
            options.put("currency", "INR")
            options.put("order_id", orderId)
            options.put("amount", amount.toString())//pass amount in currency subunits

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email", user?.emailId)
            prefill.put("contact", user?.phoneNumber)
            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }


    }

    private fun showProgress(boolean: Boolean) {
        binding.progress.progressBar.isVisible = boolean
    }


}