package com.cyclehub.ui.servicedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclehub.model.ServicesDetailsModel
import com.cyclehub.other.SingleLiveEvent
import com.cyclehub.repository.CycleHubRepository
import com.cyclehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServicesDetailsViewModel @Inject constructor(
    private val cycleHubRepository: CycleHubRepository
) : ViewModel() {

    private val _res = SingleLiveEvent<Resource<ServicesDetailsModel>>()

    val res: LiveData<Resource<ServicesDetailsModel>>
        get() = _res

    fun getServicesDetails(serviceId: String) = viewModelScope.launch {
        _res.postValue(cycleHubRepository.getServicesDetails(serviceId))
    }

}