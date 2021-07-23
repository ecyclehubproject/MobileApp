package com.cyclehub.ui.signup

import androidx.lifecycle.LiveData
import com.cyclehub.other.SingleLiveEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclehub.model.SignupRequest
import com.cyclehub.model.SignupResponse
import com.cyclehub.repository.CycleHubRepository
import com.cyclehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val cycleHubRepository: CycleHubRepository
) : ViewModel() {

    private val _res = SingleLiveEvent<Resource<SignupResponse>>()

    val res: LiveData<Resource<SignupResponse>>
        get() = _res


    fun signUp(userDetails: SignupRequest) = viewModelScope.launch {
        _res.postValue(cycleHubRepository.signUpUser(userDetails))
    }

}