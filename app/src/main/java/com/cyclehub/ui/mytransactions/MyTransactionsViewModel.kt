package com.cyclehub.ui.mytransactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclehub.model.Transaction
import com.cyclehub.other.SingleLiveEvent
import com.cyclehub.repository.CycleHubRepository
import com.cyclehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTransactionsViewModel @Inject constructor(
    private val cycleHubRepository: CycleHubRepository
) : ViewModel() {

    private val _orders = SingleLiveEvent<Resource<Transaction>>()

    val orders: LiveData<Resource<Transaction>>
        get() = _orders

    fun getTransactions() = viewModelScope.launch {
        _orders.postValue(cycleHubRepository.getTransactions())
    }

}