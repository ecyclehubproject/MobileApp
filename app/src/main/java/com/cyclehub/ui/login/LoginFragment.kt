package com.cyclehub.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cyclehub.databinding.FragmentLoginBinding
import com.cyclehub.other.Validator
import com.cyclehub.utils.Resource
import com.cyclehub.utils.setOnSingleClickListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private var mobileNo = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnSingleClickListener {
            if (Validator.isValidPhone(binding.mobile.text.toString().trim())) {
                mobileNo = binding.mobile.text.toString().trim()
                loginViewModel.loginUser(mobileNo)
            }
        }
        loginViewModel.res.observe(this.viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it.data?.let {
                        findNavController().navigate(
                            LoginFragmentDirections.actionLoginFragmentToOtpValidationFragment(
                                binding.mobile.text.toString().trim()
                            )
                        )
                    }
                }

                Resource.Status.LOADING -> {

                }
                Resource.Status.ERROR -> {
                    Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}