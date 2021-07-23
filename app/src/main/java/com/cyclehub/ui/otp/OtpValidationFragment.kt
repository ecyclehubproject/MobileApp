package com.cyclehub.ui.otp

import android.os.Bundle
import android.os.CountDownTimer
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
import com.cyclehub.R
import com.cyclehub.databinding.OtpValidationFragmentBinding
import com.cyclehub.db.DataManager
import com.cyclehub.other.Constants
import com.cyclehub.utils.Resource
import com.cyclehub.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class OtpValidationFragment : Fragment() {


    @Inject
    lateinit var dataManager: DataManager
    private lateinit var binding: OtpValidationFragmentBinding
    private val otpValidationViewModel: OtpValidationViewModel by viewModels()
    private val args: OtpValidationFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OtpValidationFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView3.text = getString(R.string.a_6_digit_pin, args.mobile)
        showProgress(false)
        binding.confirmButton.setOnSingleClickListener {
            args.mobile.let { it1 ->
                otpValidationViewModel.otpValidation(
                    it1,
                    binding.otpView.text.toString()
                )
            }
        }
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.resendOtp.isEnabled = false
                binding.timer.text =
                    "Didn't receive otp retry in: ${millisUntilFinished / 1000} sec"
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                binding.resendOtp.isEnabled = true
                binding.timer.text = ""
            }
        }.start()
        binding.resendOtp.paint.isUnderlineText = true
        binding.resendOtp.setOnSingleClickListener {
            otpValidationViewModel.loginUser(args.mobile)
            object : CountDownTimer(30000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.resendOtp.isEnabled = false
                    binding.timer.text = "Didn't receive otp retry in: ${millisUntilFinished / 1000} sec"
                }

                override fun onFinish() {
                    binding.resendOtp.isEnabled = true
                    binding.timer.text = ""
                }
            }.start()
        }

        otpValidationViewModel.login.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    showProgress(false)
                    it.data?.let { res ->
                        if (res.msg == "success") {
                            Toast.makeText(
                                context,
                                "Otp Request send successfully",
                                Toast.LENGTH_SHORT
                            ).show()
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

        otpValidationViewModel.res.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    showProgress(false)
                    it.data?.let { res ->
                        if (res.msg == "success") {
                            lifecycleScope.launch {
                                dataManager.storeData(
                                    true,
                                    res.model.authToken,
                                    userType = res.model.role
                                )
                                when (res.model.role) {
                                    Constants.CUSTOMER -> findNavController().navigate(
                                        OtpValidationFragmentDirections.actionOtpValidationFragmentToExploreNavigation()
                                    )
                                    Constants.VENDOR -> findNavController().navigate(
                                        OtpValidationFragmentDirections.actionOtpValidationFragmentToVendorNavigation()
                                    )
                                    Constants.ENGINEER -> findNavController().navigate(
                                        OtpValidationFragmentDirections.actionOtpValidationFragmentToEngineerNavigation()
                                    )
                                }
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
        binding.updateMobile.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showProgress(boolean: Boolean) {
        binding.progress.progressBar.isVisible = boolean
    }


}