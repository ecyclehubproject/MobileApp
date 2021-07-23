package com.cyclehub.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclehub.model.ServesPinCodeResponse
import com.cyclehub.model.Slider
import com.cyclehub.other.SingleLiveEvent
import com.cyclehub.repository.CycleHubRepository
import com.cyclehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val cycleHubRepository: CycleHubRepository
) : ViewModel() {
    val services = cycleHubRepository.getServices()

    private val _servespincode = SingleLiveEvent<Resource<ServesPinCodeResponse>>()

    val servespincode: LiveData<Resource<ServesPinCodeResponse>>
        get() = _servespincode

    private val _slider = SingleLiveEvent<Resource<Slider>>()

    val slider: LiveData<Resource<Slider>>
        get() = _slider

    fun servesPinCode(pinCode: Int) = viewModelScope.launch {
        _servespincode.postValue(cycleHubRepository.servesPinCodes(pinCode))
    }

    fun getSlider() = viewModelScope.launch {
        _slider.postValue(cycleHubRepository.getSlider())
    }
}
