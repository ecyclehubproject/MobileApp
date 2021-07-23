package com.cyclehub.ui.myorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclehub.db.ECycleHubDao
import com.cyclehub.model.MyOrders
import com.cyclehub.model.UserData
import com.cyclehub.other.SingleLiveEvent
import com.cyclehub.repository.CycleHubRepository
import com.cyclehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val cycleHubRepository: CycleHubRepository,
    eCycleHubDao: ECycleHubDao
) : ViewModel() {

    private val _orders = SingleLiveEvent<Resource<MyOrders>>()

    val orders: LiveData<Resource<MyOrders>>
        get() = _orders

    fun getOrders() = viewModelScope.launch {
        _orders.postValue(cycleHubRepository.getOrders())
    }

    val user: LiveData<UserData> = eCycleHubDao.getAllUser()


}