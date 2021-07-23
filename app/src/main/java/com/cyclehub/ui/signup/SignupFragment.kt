package com.cyclehub.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.cyclehub.R
import com.cyclehub.databinding.FragmentSignupBinding
import com.cyclehub.db.DataManager
import com.cyclehub.model.SignupRequest
import com.cyclehub.other.Constants.CUSTOMER
import com.cyclehub.other.Constants.ENGINEER
import com.cyclehub.other.Constants.VENDOR
import com.cyclehub.other.Validator.isValidEmail
import com.cyclehub.other.Validator.isValidPhone
import com.cyclehub.utils.Resource
import com.cyclehub.utils.loadSvg
import com.cyclehub.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private val signUpViewModel: SignUpViewModel by viewModels()
    private lateinit var binding: FragmentSignupBinding
    private val userType = arrayOf(CUSTOMER, VENDOR, ENGINEER)
    private var userTypeString: String = CUSTOMER

    @Inject
    lateinit var dataManager: DataManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayAdapter =
            ArrayAdapter(binding.userType.context, R.layout.spinner_item_container, userType)
        binding.userType.adapter = arrayAdapter

        binding.userType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                userTypeString = userType[position]
                Toast.makeText(activity, userType[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
        binding.phone.addTextChangedListener {
            binding.errorMsg.text = ""
        }
        binding.email.addTextChangedListener {
            binding.errorMsg.text = ""
        }
        binding.name.addTextChangedListener {
            binding.errorMsg.text = ""
        }

        binding.appLogo.load(R.drawable.logo_ecyclehub)
        binding.signupButton.setOnSingleClickListener {
            if (isValidEmail(binding.email) && isValidPhone(binding.phone)) {
                val signupRequest =
                    SignupRequest(
                        binding.name.text.toString(),
                        binding.email.text.toString(),
                        binding.phone.text.toString(),
                        userTypeString
                    )
                signUpViewModel.signUp(signupRequest)
            }
        }

        signUpViewModel.res.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it.data?.let { res ->
                        if (res.msg == "success") {
                            findNavController().navigate(
                                SignupFragmentDirections.actionSignupFragmentToOtpValidationFragment(
                                    binding.phone.text.toString().trim()
                                )
                            )
                        } else {
                            binding.errorMsg.text = res.msg
                            Toast.makeText(binding.root.context, res.msg, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                Resource.Status.LOADING -> {

                }
                Resource.Status.ERROR -> {
                    binding.errorMsg.text = it.message
                    Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }


}