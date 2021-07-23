//package com.cyclehub.ui.payment
//
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.NavController
//import androidx.navigation.fragment.NavHostFragment
//import androidx.navigation.navArgs
//import com.cyclehub.R
//import com.cyclehub.databinding.ActivityPaymentBinding
//import com.cyclehub.model.OrderDetails
//import com.cyclehub.model.OrderExtraInfo
//import com.cyclehub.model.PaymentRequest
//import com.cyclehub.model.PurchaseData
//import com.cyclehub.utils.PaymentObserver
//import com.razorpay.PaymentData
//import com.razorpay.PaymentResultWithDataListener
//import dagger.hilt.android.AndroidEntryPoint
//
//
//@AndroidEntryPoint
//class PaymentActivity : AppCompatActivity(), PaymentResultWithDataListener {
//    private val args: PaymentActivityArgs by navArgs()
//    private val TAG: String = PaymentActivity::class.toString()
//    private lateinit var binding: ActivityPaymentBinding
//    private var orderData: OrderDetails? = null
//    private var purchasedata: PurchaseData? = null
//    private var orderExtraInfo: OrderExtraInfo? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityPaymentBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        orderData = args.orderdata
//        purchasedata = args.purchasedata
//        orderExtraInfo = args.orderExtraInfo
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController: NavController = navHostFragment.navController
//        if (args.p)
//        navController
//            .setGraph(R.navigation.payment_navigation, intent.extras)
//    }
//
//
//
//}
