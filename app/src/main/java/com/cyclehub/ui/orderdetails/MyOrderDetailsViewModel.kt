package com.cyclehub.ui.orderdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.cyclehub.db.ECycleHubDao
import com.cyclehub.model.*
import com.cyclehub.other.SingleLiveEvent
import com.cyclehub.repository.CycleHubRepository
import com.cyclehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyOrderDetailsViewModel @Inject constructor(
    private val cycleHubRepository: CycleHubRepository,
    private val eCycleHubDao: ECycleHubDao
) : ViewModel() {

    private val _orderDetails = SingleLiveEvent<Resource<OrderById>>()

    val orderDetails: LiveData<Resource<OrderById>>
        get() = _orderDetails

    private val _orderExtraComment = SingleLiveEvent<Resource<OrderExtraResponse>>()

    private val _cancelOrder = SingleLiveEvent<Resource<CancelOrder>>()

    val cancelOrder: LiveData<Resource<CancelOrder>>
        get() = _cancelOrder

    val orderExtraComment: LiveData<Resource<OrderExtraResponse>>
        get() = _orderExtraComment

    private val _imageUpload = SingleLiveEvent<Resource<ImageUploadResponse>>()

    val imageUpload: LiveData<Resource<ImageUploadResponse>>
        get() = _imageUpload

    fun getOrderDetails(id: String) = viewModelScope.launch {
        _orderDetails.postValue(cycleHubRepository.getOrderDetail(id))
    }

    fun uploadImage(imageUploadRequest: ImageUploadRequest) = viewModelScope.launch {
        _imageUpload.postValue(cycleHubRepository.uploadImage(imageUploadRequest))
    }

    fun orderExtraComment(orderExtraRequest: OrderExtraRequest) = viewModelScope.launch {
        _orderExtraComment.postValue(cycleHubRepository.addOrderExtra(orderExtraRequest))
    }

    fun cancelOrder(id: String,region:String) = viewModelScope.launch {
        _cancelOrder.postValue(cycleHubRepository.cancelOrder(id,region))
    }

    private val _id = SingleLiveEvent<Int>()

    private val _service = _id.switchMap { id ->
        eCycleHubDao.getServices(id)
    }
    val service: LiveData<ModelServices> = _service

    fun getServiceByID(id: Int) {
        _id.value = id
    }
}