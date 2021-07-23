package com.cyclehub.api


import com.cyclehub.model.*
import javax.inject.Inject

class ECycleHubRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) : BaseDataSource() {
    suspend fun getServices() = getResult { apiService.getServices() }
    suspend fun getUser() = getResult { apiService.getUser() }
    suspend fun loginUser(mobile: String) = getResult { apiService.loginUser(mobile) }
    suspend fun signUpUser(signUpRequest: SignupRequest) =
        getResult { apiService.signUpUser(signUpRequest) }

    suspend fun getServicesByVehicleID(id: String) =
        getResult { apiService.getServicesByVehicleID(vehicle_id = id) }

    suspend fun getServicesDetails(serviceId: String) =
        getResult { apiService.getServicesDetails(id = serviceId) }

    suspend fun addAddress(addressRequest: AddressRequest) =
        getResult { apiService.addAddress(addressRequest) }

    suspend fun getAddress() = getResult { apiService.getAddress() }
    suspend fun getVehicleBrands() = getResult { apiService.getVehicleBrands() }
    suspend fun getVehicleCategory(id: String) =
        getResult { apiService.getVehicleCategory(id = id) }

    suspend fun startPayment(transactionRequest: TransactionRequest) =
        getResult { apiService.startPayment(transactionRequest = transactionRequest) }

    suspend fun confirmPayment(paymentRequest: PaymentRequest) =
        getResult { apiService.confirmPayment(paymentRequest = paymentRequest) }

    suspend fun getUserDetails() = getResult { apiService.getUserDetails() }
    suspend fun getOrders() = getResult { apiService.getOrders() }
    suspend fun getOrderDetail(id: String) = getResult { apiService.getOrderDetail(id = id) }
    suspend fun getTransactions() = getResult { apiService.getTransactions() }
    suspend fun getTransactionsDetail(id: String) =
        getResult { apiService.getTransactionsDetail(id = id) }

    suspend fun uploadImage(imageUploadRequest: ImageUploadRequest) =
        getResult { apiService.uploadImage(imageUploadRequest = imageUploadRequest) }

    suspend fun addOrderExtra(orderExtraRequest: OrderExtraRequest) =
        getResult { apiService.addOrderExtra(orderExtraRequest = orderExtraRequest) }

    suspend fun cancelOrder(id: String, region: String) =
        getResult { apiService.cancelOrder(order_id = id, region = region) }

    suspend fun vendorSignUp(vendorSignupRequest: VendorSignupRequest) =
        getResult { apiService.vendorSignUp(vendorSignupRequest) }

    suspend fun otpValidation(mobile: String, otp: String) =
        getResult { apiService.otpValidation(mobile, otp) }

    suspend fun updateProfile(profileUpdateRequest: ProfileUpdateRequest) =
        getResult { apiService.updateProfile(profileUpdateRequest) }

    suspend fun servesPinCodes(pincode: Int) =
        getResult { apiService.servesPinCodes(pincode = pincode) }

    suspend fun addEngineer(addEngineerRequest: AddEngineerRequest) =
        getResult { apiService.addEngineer(addEngineerRequest) }

    suspend fun getEngineerVendor() = getResult { apiService.getEngineerVendor() }

    suspend fun confirmOrder(
        order_id: String
    ) = getResult { apiService.confirmOrder(order_id = order_id) }

    suspend fun inProgressOrder(
        order_id: String
    ) = getResult { apiService.inProgressOrder(order_id = order_id) }

    suspend fun completeOrder(
        order_id: String
    ) = getResult { apiService.completeOrder(order_id = order_id) }

    suspend fun assignOrder(
        orderAssignRequest: OrderAssignRequest
    ) = getResult { apiService.assignOrder(orderAssignRequest = orderAssignRequest) }

    suspend fun getSlider() = getResult { apiService.getSlider() }
}