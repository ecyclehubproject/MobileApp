package com.cyclehub.ui.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclehub.model.ImageUploadRequest
import com.cyclehub.model.ImageUploadResponse
import com.cyclehub.model.ProfileUpdateRequest
import com.cyclehub.model.UserResponse
import com.cyclehub.other.SingleLiveEvent
import com.cyclehub.repository.CycleHubRepository
import com.cyclehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerEditProfileViewModel @Inject constructor(
    private val cycleHubRepository: CycleHubRepository
) : ViewModel() {
    private val _imageUpload = SingleLiveEvent<Resource<ImageUploadResponse>>()

    val imageUpload: LiveData<Resource<ImageUploadResponse>>
        get() = _imageUpload

    private val _updateProfile = SingleLiveEvent<Resource<UserResponse>>()

    val updateProfile: LiveData<Resource<UserResponse>>
        get() = _updateProfile

    fun uploadImage(imageUploadRequest: ImageUploadRequest) = viewModelScope.launch {
        _imageUpload.postValue(cycleHubRepository.uploadImage(imageUploadRequest))
    }

    fun profileUpdate(profileUpdateRequest: ProfileUpdateRequest) = viewModelScope.launch {
        _updateProfile.postValue(cycleHubRepository.updateProfile(profileUpdateRequest))
    }
}