package com.cyclehub.ui.vendordashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
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
import com.cyclehub.databinding.VendorDashboardFragmentBinding
import com.cyclehub.db.DataManager
import com.cyclehub.di.AppModule
import com.cyclehub.model.EngineerData
import com.cyclehub.model.OrderAssignRequest
import com.cyclehub.other.Constants
import com.cyclehub.ui.MainActivity
import com.cyclehub.utils.Resource
import com.cyclehub.utils.Utils
import com.cyclehub.utils.emptyDatabase
import com.cyclehub.utils.setOnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class VendorDashboardFragment : Fragment() {

    @Inject
    lateinit var dataManager: DataManager
    private val viewModel: VendorDashboardViewModel by viewModels()
    private lateinit var binding: VendorDashboardFragmentBinding
    private var orderId = -1
    private var engineerId = -1

    private lateinit var engineerList: List<EngineerData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = VendorDashboardFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataManager.userToken.asLiveData().observe(viewLifecycleOwner, {
            AppModule.YOUR_TOKEN = it
        })
        binding.orders.setOnClickListener {
            showOrders()
        }
        binding.orderTitle.setOnClickListener {
            showOrders()
        }
        binding.engineer.setOnClickListener {
            showEngineer()
        }
        binding.engineerTitle.setOnClickListener {
            showEngineer()
        }

        viewModel.user.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let { res ->
                            binding.vendorName.text = res.name
                            viewModel.getOrders()
                            viewModel.getEngineers()
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
        viewModel.confirmOrder.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let {
                            Toast.makeText(context, "Order has been confirmed", Toast.LENGTH_SHORT)
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
        viewModel.assignOrder.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let {
                            Toast.makeText(
                                context,
                                "Order has been successfully assigned.",
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
        viewModel.orders.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let { res ->
                            binding.orders.performClick()
                            if (!res.model.orders.isNullOrEmpty() && !res.model.services.isNullOrEmpty() && !res.model.transactions.isNullOrEmpty() && !res.model.users.isNullOrEmpty()) {
                                binding.myOrders.layoutManager = LinearLayoutManager(context)
                                binding.myOrders.setHasFixedSize(true)
                                binding.myOrders.adapter = VendorOrderAdapter(
                                    res.model.orders.toTypedArray(),
                                    res.model.services.toTypedArray(),
                                    res.model.transactions.toTypedArray(),
                                    res.model.users.toTypedArray()
                                ) { order ->
                                    orderId = order.id
                                    if (order.status == Constants.ASSIGNED_ORDER_STATUS || order.status == Constants.CANCEL_ORDER_STATUS|| order.status == Constants.COMPLETED_ORDER_STATUS)
                                        findNavController().navigate(
                                            VendorDashboardFragmentDirections.actionVendorDashboardFragmentToMyOrderDetailsFragment(
                                                orderId.toString(), Constants.VENDOR
                                            )
                                        )
                                    else {
                                        val isConfirm = order.status == Constants.CREATED_ORDER_STATUS
                                        showOrderOptions(isConfirm)
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
        viewModel.engineers.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let { res ->
                            if (!res.model.isNullOrEmpty()) {
                                binding.myEngineers.layoutManager = LinearLayoutManager(context)
                                binding.myEngineers.setHasFixedSize(true)
                                engineerList = res.model
                                binding.myEngineers.adapter = EngineerListAdapter(
                                    engineerList.toTypedArray()
                                ) { order ->
                                    findNavController().navigate(
                                        VendorDashboardFragmentDirections.actionVendorDashboardFragmentToVendorEngineerDetailsFragment(
                                            order
                                        )
                                    )
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
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(VendorDashboardFragmentDirections.actionVendorDashboardFragmentToAddEngineerFragment())
        }
        viewModel.cancelOrder.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        showProgress(false)
                        it?.data?.let { res ->
                            if (res.msg == "success") {
                                Toast.makeText(
                                    activity,
                                    "Order has been canceled",
                                    Toast.LENGTH_SHORT
                                ).show()
                                viewModel.getOrders()
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
    }

    private fun showProgress(boolean: Boolean = false) {
        binding.progress.progressBar.isVisible = boolean
    }

    private fun showOrders() {
        binding.myEngineers.isVisible = false
        binding.myOrders.isVisible = true
        binding.floatingActionButton.isVisible = false
        binding.currentSelection.text = "Orders"
    }

    private fun showEngineer() {
        binding.myEngineers.isVisible = true
        binding.myOrders.isVisible = false
        binding.floatingActionButton.isVisible = true
        binding.currentSelection.text = "Registered Engineers"

    }

    fun showOrderOptions(isConfirm: Boolean) {

        val dialog = BottomSheetDialog(binding.myOrders.context)
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null)
        dialog.setContentView(bottomSheet)
        val btnClose = bottomSheet.findViewById<AppCompatButton>(R.id.cancel_button)
        val btnOk = bottomSheet.findViewById<AppCompatButton>(R.id.ok_button)
        val title = bottomSheet.findViewById<AppCompatTextView>(R.id.title)
        val subTitle = bottomSheet.findViewById<AppCompatTextView>(R.id.sub_title)
        val assignTitle = bottomSheet.findViewById<AppCompatTextView>(R.id.assign_title)
        val assignEngineerList = bottomSheet.findViewById<AppCompatSpinner>(R.id.engineer_list)
        dialog.dismissWithAnimation = true
        if (isConfirm) {
            assignTitle.isVisible = false
            assignEngineerList.isVisible = false
            title.text = "Confirm/Cancel Order"
            subTitle.text = "Are you sure you want to confirm order?"
            btnClose.text = "Confirm Order"
            btnOk.text = "Cancel Order"
            btnClose.setOnSingleClickListener {
                viewModel.confirmOrder(orderId.toString())
                dialog.dismiss()
            }
            btnOk.setOnSingleClickListener {
                viewModel.cancelOrder(orderId.toString(), "")
                dialog.dismiss()
            }
            dialog.show()
        } else {
            if (::engineerList.isInitialized && engineerList.isNotEmpty()) {
                title.text = "Assign Order"
                subTitle.text = "Please select engineer to Assign order"
                btnClose.text = "Assign Order"
                btnOk.text = "View Details"
                assignTitle.isVisible = true
                assignEngineerList.isVisible = true
                val userAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                    binding.myOrders.context,
                    R.layout.spinner_item_container,
                    engineerList
                )
                assignEngineerList.adapter = userAdapter
                assignEngineerList.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        val engineer: EngineerData = parent.selectedItem as EngineerData
                        engineerId = engineer.userId.id
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
                btnClose.setOnSingleClickListener {
                    val orderAssignRequest =
                        OrderAssignRequest(engineerId, orderId, Constants.ASSIGNED_ORDER_STATUS)
                    viewModel.assignOrder(orderAssignRequest)
                    dialog.dismiss()
                }
                btnOk.setOnSingleClickListener {
                    dialog.dismiss()
                    findNavController().navigate(
                        VendorDashboardFragmentDirections.actionVendorDashboardFragmentToMyOrderDetailsFragment(
                            orderId.toString(),
                            Constants.VENDOR
                        )
                    )
                }
                dialog.show()
            } else {
                Toast.makeText(
                    context,
                    "Please add engineer to assign order",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
