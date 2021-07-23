package com.cyclehub.api

import com.cyclehub.model.*
import com.cyclehub.other.Constants
import com.cyclehub.other.Constants.BASE_URL_MEDIA
import com.cyclehub.other.Constants.BASE_URL_ORDER
import com.cyclehub.other.Constants.BASE_URL_TRANSACTIONS
import com.cyclehub.other.Constants.BASE_URL_VEHICLE
import com.cyclehub.other.Constants.SERVESPINCODES
import com.cyclehub.other.Constants.SLIDER
import retrofit2.Response
import retrofit2.http.*


interface ApiService {


    @POST("dev/login")
    suspend fun loginUser(
        @Query("mobile") mobile: String
    ): Response<SignupResponse>

    @Headers("Accept: application/json")
    @POST("dev/signup")
    suspend fun signUpUser(@Body signUpRequest: SignupRequest): Response<SignupResponse>

    @GET
    suspend fun getServices(@Url url: String = Constants.BASE_URL_VMS_DEFAULT_SERVICES): Response<ServicesModel>

    @GET("dev/user")
    suspend fun getUser(): Response<UserResponse>

    @GET("dev/user")
    suspend fun getUserDetails(): Response<User>

    @GET
    suspend fun getServicesByVehicleID(
        @Url url: String = Constants.BASE_URL_VMS_DEFAULT_SERVICES,
        @Query("vehicle_id") vehicle_id: String
    ): Response<ServicesModel>

    @GET
    suspend fun getServicesDetails(
        @Url url: String = Constants.BASE_URL_VMS_DEFAULT_SERVICES,
        @Query("id") id: String,
    ): Response<ServicesDetailsModel>

    @POST("dev/address")
    suspend fun addAddress(
        @Body addressRequest: AddressRequest,
    ): Response<AddressResponce>

    @GET("dev/address")
    suspend fun getAddress(
    ): Response<GetAddress>

    @GET("dev/address")
    suspend fun getAddressById(
        @Query("id") id: String,
    ): Response<AddressResponce>

    //Vehicle
    @GET
    suspend fun getVehicleBrands(@Url url: String = BASE_URL_VEHICLE): Response<GetVehicleBrands>

    @GET
    suspend fun getVehicleCategory(
        @Url url: String = BASE_URL_VEHICLE,
        @Query("action") action: String = "product",
        @Query("id") id: String
    ): Response<VehicleCategory>

    //Orders
    @GET
    suspend fun getOrders(
        @Url url: String = BASE_URL_ORDER
    ): Response<MyOrders>

    @GET
    suspend fun getOrderDetail(
        @Url url: String = BASE_URL_ORDER,
        @Query("id") id: String
    ): Response<OrderById>

    //Transactions
    @GET
    suspend fun getTransactions(
        @Url url: String = BASE_URL_TRANSACTIONS
    ): Response<Transaction>

    @GET
    suspend fun getTransactionsDetail(
        @Url url: String = BASE_URL_ORDER,
        @Query("id") id: String
    ): Response<TransactionResponce>

    @POST
    suspend fun startPayment(
        @Url url: String = BASE_URL_TRANSACTIONS,
        @Body transactionRequest: TransactionRequest
    ): Response<TransactionResponce>

    @PUT
    suspend fun confirmPayment(
        @Url url: String = BASE_URL_TRANSACTIONS,
        @Body paymentRequest: PaymentRequest
    ): Response<PaymentResponce>

    @POST
    suspend fun uploadImage(
        @Url url: String = BASE_URL_MEDIA,
        @Body imageUploadRequest: ImageUploadRequest
    ): Response<ImageUploadResponse>

    @POST
    suspend fun addOrderExtra(
        @Url url: String = BASE_URL_ORDER,
        @Query("action") action: String = "activity",
        @Body orderExtraRequest: OrderExtraRequest
    ): Response<OrderExtraResponse>

    @POST("dev/signup")
    suspend fun vendorSignUp(@Body vendorSignupRequest: VendorSignupRequest): Response<SignupResponse>

    @GET("dev/login/otp")
    suspend fun otpValidation(
        @Query("mobile") mobile: String,
        @Query("otp") otp: String
    ): Response<Login>

    @PUT("dev/user")
    suspend fun updateProfile(
        @Body profileUpdateRequest: ProfileUpdateRequest
    ): Response<UserResponse>

    @GET
    suspend fun servesPinCodes(
        @Url url: String = SERVESPINCODES,
        @Query("pincode") pincode: Int
    ): Response<ServesPinCodeResponse>

    @POST("dev/user")
    suspend fun addEngineer(@Body addEngineerRequest: AddEngineerRequest): Response<AddEngineerResponse>

    @GET("dev/user/vendor")
    suspend fun getEngineerVendor(): Response<GetEnineerVendorResponse>

    @PUT
    suspend fun confirmOrder(
        @Url url: String = BASE_URL_ORDER,
        @Query("action") action: String = Constants.CONFIRM_ORDER_STATUS.lowercase(),
        @Query("order_id") order_id: String
    ): Response<VendorOrderConfirmResponse>

    @PUT
    suspend fun assignOrder(
        @Url url: String = BASE_URL_ORDER,
        @Body orderAssignRequest: OrderAssignRequest
    ): Response<VendorOrderConfirmResponse>

    @PUT
    suspend fun completeOrder(
        @Url url: String = BASE_URL_ORDER,
        @Query("action") action: String = Constants.COMPLETED_ORDER_STATUS.lowercase(),
        @Query("order_id") order_id: String
    ): Response<VendorOrderConfirmResponse>

    @PUT
    suspend fun inProgressOrder(
        @Url url: String = BASE_URL_ORDER,
        @Query("action") action: String = Constants.IN_PROCESS_ORDER_STATUS.lowercase(),
        @Query("order_id") order_id: String
    ): Response<VendorOrderConfirmResponse>

    @PUT
    suspend fun cancelOrder(
        @Url url: String = BASE_URL_ORDER,
        @Query("action") action: String = Constants.CANCEL_ORDER_STATUS.lowercase(),
        @Query("order_id") order_id: String,
        @Query("reason") region: String
    ): Response<CancelOrder>

    @GET
    suspend fun getSlider(
        @Url url: String = SLIDER
    ): Response<Slider>

}