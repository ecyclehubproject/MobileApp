package com.cyclehub.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclehub.model.UserResponse
import com.cyclehub.other.SingleLiveEvent
import com.cyclehub.repository.CycleHubRepository
import com.cyclehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val cycleHubRepository: CycleHubRepository,
) : ViewModel() {


    private val _user = SingleLiveEvent<Resource<UserResponse>>()

    val user: LiveData<Resource<UserResponse>>
        get() = _user


    fun getUser() = viewModelScope.launch {
        _user.postValue(cycleHubRepository.getUserDataServer())
    }
}