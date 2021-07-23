package com.cyclehub.ui.address

import androidx.lifecycle.LiveData
import com.cyclehub.other.SingleLiveEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclehub.model.AddressResponce
import com.cyclehub.model.AddressRequest
import com.cyclehub.repository.CycleHubRepository
import com.cyclehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val cycleHubRepository: CycleHubRepository
) : ViewModel() {
    private val _res = SingleLiveEvent<Resource<AddressResponce>>()

    val res: LiveData<Resource<AddressResponce>>
        get() = _res

    val user = cycleHubRepository.getUser()

    fun addAddress(addressRequest: AddressRequest) = viewModelScope.launch {
        _res.postValue(cycleHubRepository.addAddress(addressRequest))
    }

}