package com.cyclehub.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.cyclehub.R
import com.cyclehub.databinding.ActivityMainBinding
import com.cyclehub.model.RazorPayError
import com.cyclehub.model.RazorPayPayment
import com.cyclehub.utils.*
import com.google.gson.Gson
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PaymentResultWithDataListener {
    private val TAG: String = MainActivity::class.toString()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
        lifecycleScope.launchWhenResumed {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.exploreFragment, R.id.settings_fragment, R.id.supportFragment -> {
                        binding.bottomNav.slideVisibility(true)
                    }
                    else -> binding.bottomNav.slideVisibility(false)
                }
            }
        }
        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.explore -> {
                    navController.navigate(R.id.explore_navigation)
                    return@setOnItemSelectedListener true
                }
                R.id.settings -> {
                    navController.navigate(R.id.settings_navigation)
                    return@setOnItemSelectedListener true
                }
                R.id.support -> {
                    navController.navigate(R.id.support_navigation)
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }
        binding.bottomNav.setOnItemReselectedListener { }

    }


    override fun onPaymentError(
        errorCode: Int,
        errorDescription: String?,
        paymentData: PaymentData?
    ) {

        if (paymentData != null) {
            val razorPayPayment =
                RazorPayPayment("", paymentData)
            PaymentObserver.paymentStatus = razorPayPayment
        }
        try {
            val convertedObject: RazorPayError? =
                Gson().fromJson(errorDescription, RazorPayError::class.java)

            Toast.makeText(
                this,
                convertedObject?.error?.description,
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
        }
    }

    override fun onPaymentSuccess(rzpPaymentId: String?, paymentData: PaymentData?) {
        if (!rzpPaymentId.isNullOrEmpty() && paymentData != null) {
            val razorPayPayment =
                RazorPayPayment(rzpPaymentId, paymentData)
            PaymentObserver.paymentStatus = razorPayPayment
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.exploreFragment, R.id.vendorDashboardFragment, R.id.engineerDashboardFragment -> {
                super.onBackPressed()
                finishAffinity()
                exitProcess(0)
            }
            R.id.settings_fragment -> {
                binding.bottomNav.selectedItemId = R.id.explore
            }
            R.id.supportFragment -> {
                binding.bottomNav.selectedItemId = R.id.settings
            }
            else -> {
                onSupportNavigateUp()
            }

        }
    }

}
