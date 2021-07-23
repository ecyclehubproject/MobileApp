package com.cyclehub.ui.vendordashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclehub.model.*
import com.cyclehub.other.SingleLiveEvent
import com.cyclehub.repository.CycleHubRepository
import com.cyclehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VendorDashboardViewModel @Inject constructor(
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

    private val _engineers = SingleLiveEvent<Resource<GetEnineerVendorResponse>>()

    val engineers: LiveData<Resource<GetEnineerVendorResponse>>
        get() = _engineers

    fun getEngineers() = viewModelScope.launch {
        _engineers.postValue(cycleHubRepository.getEngineerVendor())
    }

    private val _confirmOrder = SingleLiveEvent<Resource<VendorOrderConfirmResponse>>()

    val confirmOrder: LiveData<Resource<VendorOrderConfirmResponse>>
        get() = _confirmOrder

    fun confirmOrder(order_id: String) = viewModelScope.launch {
        _confirmOrder.postValue(cycleHubRepository.confirmOrder(order_id))
    }

    private val _assignOrder = SingleLiveEvent<Resource<VendorOrderConfirmResponse>>()

    val assignOrder: LiveData<Resource<VendorOrderConfirmResponse>>
        get() = _assignOrder

    fun assignOrder(orderAssignRequest: OrderAssignRequest) = viewModelScope.launch {
        _assignOrder.postValue(cycleHubRepository.assignOrder(orderAssignRequest))
    }

    private val _cancelOrder = SingleLiveEvent<Resource<CancelOrder>>()

    val cancelOrder: LiveData<Resource<CancelOrder>>
        get() = _cancelOrder

    fun cancelOrder(order_id: String,region:String) = viewModelScope.launch {
        _cancelOrder.postValue(cycleHubRepository.cancelOrder(order_id,region))
    }
}