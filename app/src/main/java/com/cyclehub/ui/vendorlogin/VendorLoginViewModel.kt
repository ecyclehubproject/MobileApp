package com.cyclehub.ui.vendorlogin

import androidx.lifecycle.LiveData
import com.cyclehub.other.SingleLiveEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclehub.model.SignupResponse
import com.cyclehub.model.VendorSignupRequest
import com.cyclehub.repository.CycleHubRepository
import com.cyclehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VendorLoginViewModel @Inject constructor(
    private val cycleHubRepository: CycleHubRepository
) : ViewModel() {

    private val _res = SingleLiveEvent<Resource<SignupResponse>>()

    val res: LiveData<Resource<SignupResponse>>
        get() = _res

    fun vendorSignUp(vendorSignupRequest: VendorSignupRequest) = viewModelScope.launch {
        _res.postValue(cycleHubRepository.vendorSignUp(vendorSignupRequest))
    }

}