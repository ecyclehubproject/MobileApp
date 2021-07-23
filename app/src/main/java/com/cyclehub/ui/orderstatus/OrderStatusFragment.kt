package com.cyclehub.ui.orderstatus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cyclehub.databinding.OrderStatusOfflineFragmentBinding
import com.cyclehub.other.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderStatusFragment : Fragment() {
    private val args: OrderStatusFragmentArgs by navArgs()
    private lateinit var binding: OrderStatusOfflineFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OrderStatusOfflineFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.orderConfirm.text =
            if (args.paymentstatus) "Congratulations \n Order has been successfully placed" else "Oops! \n There is some issue with please try again later"

        MainScope().launch {
            delay(3000)
            findNavController().navigate(
                OrderStatusFragmentDirections.actionOrderStatusFragmentOnlineToMyOrderDetailsFragment2(
                    args.orderid,
                    Constants.CUSTOMER
                )
            )
        }

    }

    private fun showProgress(boolean: Boolean = false) {
        binding.progress.progressBar.isVisible = boolean
    }

}