package com.cyclehub.repository

import com.cyclehub.api.ECycleHubRemoteDataSource
import com.cyclehub.db.ECycleHubDao
import com.cyclehub.model.*
import com.cyclehub.utils.performGetOperation
import javax.inject.Inject

class CycleHubRepository @Inject constructor(
    private val eCycleHubRemoteDataSource: ECycleHubRemoteDataSource,
    private val eCycleHubDao: ECycleHubDao

) {
    suspend fun loginUser(mobile: String) = eCycleHubRemoteDataSource.loginUser(mobile)
    suspend fun signUpUser(signUpRequest: SignupRequest) =
        eCycleHubRemoteDataSource.signUpUser(signUpRequest)

    suspend fun vendorSignUp(vendorSignupRequest: VendorSignupRequest) =
        eCycleHubRemoteDataSource.vendorSignUp(vendorSignupRequest)

    suspend fun addAddress(addressRequest: AddressRequest) =
        eCycleHubRemoteDataSource.addAddress(addressRequest)

    suspend fun getAddress() = eCycleHubRemoteDataSource.getAddress()
    suspend fun startPayment(transactionRequest: TransactionRequest) =
        eCycleHubRemoteDataSource.startPayment(transactionRequest)

    suspend fun getTransactions() =
        eCycleHubRemoteDataSource.getTransactions()

    suspend fun confirmPayment(paymentRequest: PaymentRequest) =
        eCycleHubRemoteDataSource.confirmPayment(paymentRequest)

    suspend fun uploadImage(imageUploadRequest: ImageUploadRequest) =
        eCycleHubRemoteDataSource.uploadImage(imageUploadRequest = imageUploadRequest)

    suspend fun getOrders() = eCycleHubRemoteDataSource.getOrders()

    fun getServices() = performGetOperation(
        databaseQuery = { eCycleHubDao.getAllServices() },
        networkCall = { eCycleHubRemoteDataSource.getServices() },
        saveCallResult = { eCycleHubDao.insertAllServices(it.model) }
    )

    fun getUser() = performGetOperation(
        databaseQuery = { eCycleHubDao.getAllUser() },
        networkCall = { eCycleHubRemoteDataSource.getUser() },
        saveCallResult = { eCycleHubDao.insertUser(it.model) }
    )

    suspend fun getUserDataServer() = eCycleHubRemoteDataSource.getUser()


    suspend fun getUserDetails() = eCycleHubRemoteDataSource.getUserDetails()

    suspend fun getOrderDetail(id: String) =
        eCycleHubRemoteDataSource.getOrderDetail(id)

    suspend fun addOrderExtra(orderExtraRequest: OrderExtraRequest) =
        eCycleHubRemoteDataSource.addOrderExtra(orderExtraRequest = orderExtraRequest)

    suspend fun cancelOrder(id: String, region: String) =
        eCycleHubRemoteDataSource.cancelOrder(id, region)

    suspend fun getServicesDetails(serviceId: String) =
        eCycleHubRemoteDataSource.getServicesDetails(serviceId)

    suspend fun otpValidation(mobile: String, otp: String) =
        eCycleHubRemoteDataSource.otpValidation(mobile, otp)

    suspend fun updateProfile(profileUpdateRequest: ProfileUpdateRequest) =
        eCycleHubRemoteDataSource.updateProfile(profileUpdateRequest)

    suspend fun servesPinCodes(pincode: Int) = eCycleHubRemoteDataSource.servesPinCodes(pincode)

    suspend fun addEngineer(addEngineerRequest: AddEngineerRequest) =
        eCycleHubRemoteDataSource.addEngineer(addEngineerRequest)

    suspend fun getEngineerVendor() = eCycleHubRemoteDataSource.getEngineerVendor()

    suspend fun confirmOrder(order_id: String) = eCycleHubRemoteDataSource.confirmOrder(order_id)

    suspend fun assignOrder(orderAssignRequest: OrderAssignRequest) =
        eCycleHubRemoteDataSource.assignOrder(orderAssignRequest)

    suspend fun inProgressOrder(order_id: String) =
        eCycleHubRemoteDataSource.inProgressOrder(order_id)

    suspend fun completeOrder(order_id: String) = eCycleHubRemoteDataSource.completeOrder(order_id)
    suspend fun getSlider() = eCycleHubRemoteDataSource.getSlider()

}