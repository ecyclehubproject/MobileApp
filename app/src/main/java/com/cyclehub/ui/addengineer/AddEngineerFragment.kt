package com.cyclehub.ui.addengineer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cyclehub.databinding.AddEngineerFragmentBinding
import com.cyclehub.model.AddEngineerRequest
import com.cyclehub.other.Validator.isEmpty
import com.cyclehub.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEngineerFragment : Fragment() {

    companion object {
        fun newInstance() = AddEngineerFragment()
    }

    private lateinit var binding: AddEngineerFragmentBinding
    private val viewModel: AddEngineerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddEngineerFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress(false)
        binding.signupButton.setOnClickListener {
            if (isEmpty(binding.name.text.toString()) && isEmpty(binding.email.text.toString()) && isEmpty(
                    binding.phone.text.toString()
                )
            ) {
                val engineerRequest = AddEngineerRequest(
                    binding.name.text.toString(),
                    binding.email.text.toString(),
                    binding.phone.text.toString()
                )
                viewModel.addEngineer(engineerRequest)
            }
        }

        viewModel.res.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let {
                            findNavController().popBackStack()
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