package com.cyclehub.ui.addengineer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclehub.model.AddEngineerRequest
import com.cyclehub.model.AddEngineerResponse
import com.cyclehub.other.SingleLiveEvent
import com.cyclehub.repository.CycleHubRepository
import com.cyclehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEngineerViewModel @Inject constructor(
    private val cycleHubRepository: CycleHubRepository
) : ViewModel() {

    private val _res = SingleLiveEvent<Resource<AddEngineerResponse>>()

    val res: LiveData<Resource<AddEngineerResponse>>
        get() = _res


    fun addEngineer(addEngineerRequest: AddEngineerRequest) = viewModelScope.launch {
        _res.postValue(cycleHubRepository.addEngineer(addEngineerRequest))
    }

}