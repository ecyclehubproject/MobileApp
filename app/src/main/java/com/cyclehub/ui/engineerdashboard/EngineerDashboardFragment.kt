package com.cyclehub.ui.engineerdashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyclehub.R
import com.cyclehub.databinding.EngineerDashboardFragmentBinding
import com.cyclehub.db.DataManager
import com.cyclehub.di.AppModule
import com.cyclehub.other.Constants
import com.cyclehub.ui.MainActivity
import com.cyclehub.utils.*
import com.cyclehub.utils.Utils.showImage
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EngineerDashboardFragment : Fragment() {

    @Inject
    lateinit var dataManager: DataManager
    private val viewModel: EngineerDashboardViewModel by viewModels()
    private lateinit var binding: EngineerDashboardFragmentBinding
    private var orderId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EngineerDashboardFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataManager.userToken.asLiveData().observe(viewLifecycleOwner, {
            AppModule.YOUR_TOKEN = it
        })
        viewModel.user.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let { res ->
                            binding.engineerNameTitle.text = res.name
                            viewModel.getOrders()

                        }
                    }
                    Resource.Status.LOADING -> {
                        showProgress(true)
                    }
                    Resource.Status.ERROR -> {
                        showProgress(false)
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })

        viewModel.orders.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.model?.let { res ->
                            if (!res.orders.isNullOrEmpty() && !res.services.isNullOrEmpty() && !res.transactions.isNullOrEmpty() && !res.users.isNullOrEmpty()) {
                                binding.myOrders.layoutManager = LinearLayoutManager(context)
                                binding.myOrders.setHasFixedSize(true)
                                binding.myOrders.adapter = EngineerOrderAdapter(
                                    res.orders.toTypedArray(),
                                    res.services.toTypedArray(),
                                    res.transactions.toTypedArray(),
                                    res.users.toTypedArray()
                                ) { order ->
                                    orderId = order.id
                                    if (order.status == Constants.COMPLETED_ORDER_STATUS || order.status == Constants.CANCEL_ORDER_STATUS)
                                        findNavController().navigate(
                                            EngineerDashboardFragmentDirections.actionEngineerDashboardFragmentToMyOrderDetailsFragment(
                                                orderId.toString(), Constants.ENGINEER
                                            )
                                        )
                                    else {
                                        val isAssigned =
                                            order.status == Constants.ASSIGNED_ORDER_STATUS
                                        showOrderOptions(isAssigned)
                                    }
                                }

                            } else {
                                Utils.showEmptyView(
                                    binding.myOrders,
                                    binding.emptyView,
                                    "No Orders Available"
                                )
                            }
                        }

                    }
                    Resource.Status.LOADING -> {
                        showProgress(true)
                    }
                    Resource.Status.ERROR -> {
                        showProgress(false)
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
        viewModel.inProgressOrder.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let {
                            Toast.makeText(context, "Job has been started", Toast.LENGTH_SHORT)
                                .show()
                            viewModel.getOrders()
                        }
                    }
                    Resource.Status.LOADING -> {
                        showProgress(true)
                    }
                    Resource.Status.ERROR -> {
                        showProgress(false)
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
        viewModel.completeOrder.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let {
                            Toast.makeText(
                                context,
                                "Order has been successfully fulfilled.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            viewModel.getOrders()
                        }
                    }
                    Resource.Status.LOADING -> {
                        showProgress(true)
                    }
                    Resource.Status.ERROR -> {
                        showProgress(false)
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
        binding.logoutButton.setOnSingleClickListener {
            val dialog = BottomSheetDialog(binding.logoutButton.context)
            val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null)
            dialog.setContentView(bottomSheet)
            val btnClose = bottomSheet.findViewById<AppCompatButton>(R.id.cancel_button)
            val btnOk = bottomSheet.findViewById<AppCompatButton>(R.id.ok_button)
            val title = bottomSheet.findViewById<AppCompatTextView>(R.id.title)
            val subTitle = bottomSheet.findViewById<AppCompatTextView>(R.id.sub_title)
            title.text = getString(R.string.logout)
            subTitle.text = getString(R.string.logout_confirm)
            dialog.dismissWithAnimation = true
            btnClose.setOnSingleClickListener {
                dialog.dismiss()
            }
            btnOk.setOnSingleClickListener {
                dialog.dismiss()
                lifecycleScope.launch {
                    dataManager.clearData()
                    dataManager.clearUserToken()
                    emptyDatabase()
                    delay(1000)
                    Utils.triggerRebirth(btnOk.context, MainActivity::class.java)
                }
            }
            dialog.show()
        }

    }

    private fun showProgress(boolean: Boolean = false) {
        binding.progress.progressBar.isVisible = boolean
    }


    fun showOrderOptions(isAssigned: Boolean) {
        val dialog = BottomSheetDialog(binding.myOrders.context)
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null)
        dialog.setContentView(bottomSheet)
        val btnClose = bottomSheet.findViewById<AppCompatButton>(R.id.cancel_button)
        val btnOk = bottomSheet.findViewById<AppCompatButton>(R.id.ok_button)
        val title = bottomSheet.findViewById<AppCompatTextView>(R.id.title)
        val subTitle = bottomSheet.findViewById<AppCompatTextView>(R.id.sub_title)
        val assignTitle = bottomSheet.findViewById<AppCompatTextView>(R.id.assign_title)
        val qr = bottomSheet.findViewById<AppCompatImageView>(R.id.qr)
        val assignEngineerList = bottomSheet.findViewById<AppCompatSpinner>(R.id.engineer_list)
        dialog.dismissWithAnimation = true
        if (isAssigned) {
            qr.isVisible = false
            assignTitle.isVisible = false
            assignEngineerList.isVisible = false
            title.text = "Start Job"
            subTitle.text = "Make order in progress?"
            btnClose.text = "Start Job"
            btnOk.text = "View Details"
            btnClose.setOnSingleClickListener {
                viewModel.inProgressOrder(orderId.toString())
                dialog.dismiss()
            }
        } else {
            qr.isVisible = true
            qr.loadSvgFromUrl("https://e-cyclehub.s3.ap-south-1.amazonaws.com/services/Graphics/QR.png")
            title.text = "Complete Job"
            subTitle.text = "Order completed?"
            btnClose.text = "Complete Order"
            btnOk.text = "View Details"
            btnClose.setOnSingleClickListener {
                viewModel.completeOrder(orderId.toString())
                dialog.dismiss()
            }
            qr.setOnSingleClickListener {
                showImage(
                    qr.context,
                    "https://e-cyclehub.s3.ap-south-1.amazonaws.com/services/qr.jpeg"
                )
            }
        }



        btnOk.setOnSingleClickListener {
            dialog.dismiss()
            findNavController().navigate(
                EngineerDashboardFragmentDirections.actionEngineerDashboardFragmentToMyOrderDetailsFragment(
                    orderId.toString(),
                    Constants.ENGINEER
                )
            )
        }
        dialog.show()
    }
}