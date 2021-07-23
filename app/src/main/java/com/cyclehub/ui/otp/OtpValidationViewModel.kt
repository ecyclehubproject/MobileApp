package com.cyclehub.ui.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclehub.model.Login
import com.cyclehub.model.SignupResponse
import com.cyclehub.other.SingleLiveEvent
import com.cyclehub.repository.CycleHubRepository
import com.cyclehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpValidationViewModel @Inject constructor(
    private val cycleHubRepository: CycleHubRepository
) : ViewModel() {

    private val _res = SingleLiveEvent<Resource<Login>>()

    val res: LiveData<Resource<Login>>
        get() = _res


    fun otpValidation(mobile: String, otp: String) = viewModelScope.launch {
        _res.postValue(cycleHubRepository.otpValidation(mobile, otp))
    }

    private val _login = SingleLiveEvent<Resource<SignupResponse>>()

    val login: LiveData<Resource<SignupResponse>>
        get() = _login


    fun loginUser(mobile: String) = viewModelScope.launch {
        _login.postValue(cycleHubRepository.loginUser(mobile))
    }

}