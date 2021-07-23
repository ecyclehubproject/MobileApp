package com.cyclehub.ui.engineerdashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclehub.model.MyOrders
import com.cyclehub.model.VendorOrderConfirmResponse
import com.cyclehub.other.SingleLiveEvent
import com.cyclehub.repository.CycleHubRepository
import com.cyclehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EngineerDashboardViewModel @Inject constructor(
    private val cycleHubRepository: CycleHubRepository
) : ViewModel() {

    private val _orders = SingleLiveEvent<Resource<MyOrders>>()

    val orders: LiveData<Resource<MyOrders>>
        get() = _orders

    val services = cycleHubRepository.getServices()
    fun getOrders() = viewModelScope.launch {
        _orders.postValue(cycleHubRepository.getOrders())
    }

    val user = cycleHubRepository.getUser()


    private val _inProgressOrder = SingleLiveEvent<Resource<VendorOrderConfirmResponse>>()

    val inProgressOrder: LiveData<Resource<VendorOrderConfirmResponse>>
        get() = _inProgressOrder

    fun inProgressOrder(order_id: String) = viewModelScope.launch {
        _inProgressOrder.postValue(cycleHubRepository.inProgressOrder(order_id))
    }

    private val _completeOrder = SingleLiveEvent<Resource<VendorOrderConfirmResponse>>()

    val completeOrder: LiveData<Resource<VendorOrderConfirmResponse>>
        get() = _completeOrder

    fun completeOrder(order_id: String) = viewModelScope.launch {
        _completeOrder.postValue(cycleHubRepository.completeOrder(order_id))
    }
}