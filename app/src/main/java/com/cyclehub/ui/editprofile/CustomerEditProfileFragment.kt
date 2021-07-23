package com.cyclehub.ui.editprofile

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cyclehub.databinding.CustomerEditProfileFragmentBinding
import com.cyclehub.model.ImageUploadRequest
import com.cyclehub.model.ProfileUpdateRequest
import com.cyclehub.other.Constants.ISPROFILEUPDATED
import com.cyclehub.other.Validator
import com.cyclehub.utils.*
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomerEditProfileFragment : Fragment() {

    private val viewModel: CustomerEditProfileViewModel by viewModels()
    private lateinit var binding: CustomerEditProfileFragmentBinding
    private val args: CustomerEditProfileFragmentArgs by navArgs()
    private var mediaId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CustomerEditProfileFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress(false)
        val user = args.userInfo
        user?.let {
            binding.editProfile.loadSvgFromUrl(user.picture)
            binding.name.text = user.name.toEditable()
            binding.email.text = user.emailId.toEditable()
        }
        viewModel.imageUpload.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let { res ->
                            if (res.msg == "success") {
                                mediaId = res.model.mediaId
                                binding.editProfile.loadSvgFromUrl(res.model.mediaPath)
                            }
                            showProgress(false)
                        }
                    }
                    Resource.Status.LOADING -> {
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT)
                            .show()
                        showProgress(true)
                    }
                    Resource.Status.ERROR -> {
                        showProgress(false)
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
        viewModel.updateProfile.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let { res ->
                            if (res.msg == "success") {
                                binding.editProfile.loadSvgFromUrl(res.model.picture)
                                binding.name.text = res.model.name.toEditable()
                                binding.email.text = res.model.emailId.toEditable()
                                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                                    ISPROFILEUPDATED,
                                    true
                                )
                                findNavController().popBackStack()
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
        binding.editProfile.setOnSingleClickListener {
            showProgress(true)
            ImagePicker.with(this)
                .crop()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
        binding.saveButton.setOnSingleClickListener {
            val email = binding.email.text.toString().trim()
            val name = binding.name.text.toString().trim()
            val id = user?.id
            if (Validator.isEmpty(email) && Validator.isEmpty(name) && id != null) {
                val profileUpdateRequest = ProfileUpdateRequest(email, id, mediaId, name)
                viewModel.profileUpdate(profileUpdateRequest)
            }
        }
    }

    private fun showProgress(boolean: Boolean) {
        binding.progress.progressBar.isVisible = boolean
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data as Uri
                    lifecycleScope.launch {
                        val bitmap = Utils.getBitmap(requireContext(), fileUri)
                        val bit = rotateImageIfRequired(requireContext(), bitmap, fileUri)
                        if (bit!=null)
                        viewModel.uploadImage(
                            ImageUploadRequest(
                                bit.bitMapToString(),
                                "${System.currentTimeMillis()}.jpeg"
                            )
                        )
                    }


                }
                ImagePicker.RESULT_ERROR -> {
                    showProgress(false)
                    Toast.makeText(activity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    showProgress(false)
                    Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

}