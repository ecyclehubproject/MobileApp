package com.cyclehub.ui.checkout

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
class CheckOutViewModel @Inject constructor(
    private val cycleHubRepository: CycleHubRepository
) : ViewModel() {
    private val _res = SingleLiveEvent<Resource<GetAddress>>()

    private val _paymentSuccess = SingleLiveEvent<Boolean>()

    val paymentSuccess: LiveData<Boolean>
        get() = _paymentSuccess

    val res: LiveData<Resource<GetAddress>>
        get() = _res

    private val _purchase = SingleLiveEvent<Resource<TransactionResponce>>()

    val purchase: LiveData<Resource<TransactionResponce>>
        get() = _purchase

    private val _payment = SingleLiveEvent<Resource<PaymentResponce>>()

    val payment: LiveData<Resource<PaymentResponce>>
        get() = _payment

    private val _imageUpload = SingleLiveEvent<Resource<ImageUploadResponse>>()

    val imageUpload: LiveData<Resource<ImageUploadResponse>>
        get() = _imageUpload

    private val _user = SingleLiveEvent<Resource<UserResponse>>()

    val user: LiveData<Resource<UserResponse>>
        get() = _user


    fun getLastAddress() = viewModelScope.launch {
        _res.postValue(cycleHubRepository.getAddress())
    }

    fun startPayment(transactionRequest: TransactionRequest) = viewModelScope.launch {
        _purchase.postValue(cycleHubRepository.startPayment(transactionRequest))
    }

    fun confirmPayment(paymentRequest: PaymentRequest) = viewModelScope.launch {
        _payment.postValue(cycleHubRepository.confirmPayment(paymentRequest))
    }

    fun uploadImage(imageUploadRequest: ImageUploadRequest) = viewModelScope.launch {
        _imageUpload.postValue(cycleHubRepository.uploadImage(imageUploadRequest))
    }

    fun getUser() = viewModelScope.launch {
        _user.postValue(cycleHubRepository.getUserDataServer())
    }

    private val _address = SingleLiveEvent<Resource<AddressResponce>>()

    val address: LiveData<Resource<AddressResponce>>
        get() = _address

    fun addAddress(addressRequest: AddressRequest) = viewModelScope.launch {
        _address.postValue(cycleHubRepository.addAddress(addressRequest))
    }

    private val _servespincode = SingleLiveEvent<Resource<ServesPinCodeResponse>>()

    val servespincode: LiveData<Resource<ServesPinCodeResponse>>
        get() = _servespincode

    fun servesPinCode(pinCode: Int) = viewModelScope.launch {
        _servespincode.postValue(cycleHubRepository.servesPinCodes(pinCode))
    }

}