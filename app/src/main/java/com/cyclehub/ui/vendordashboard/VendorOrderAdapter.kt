package com.cyclehub.ui.vendordashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyclehub.R
import com.cyclehub.databinding.VendorOrderItemContainerBinding
import com.cyclehub.model.OrderX
import com.cyclehub.model.ServiceX
import com.cyclehub.model.TransactionX
import com.cyclehub.model.UserX
import com.cyclehub.other.Constants
import com.cyclehub.utils.setOnSingleClickListener
import com.cyclehub.utils.toCustomPatten
import com.cyclehub.utils.toDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class VendorOrderAdapter(
    private val myOrdersList: Array<OrderX>,
    private val myServiceList: Array<ServiceX>,
    private val myTransactionList: Array<TransactionX>,
    private val users: Array<UserX>,
    val clickListener: (OrderX) -> Unit,
) :
    RecyclerView.Adapter<VendorOrderAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: VendorOrderItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            VendorOrderItemContainerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val serviceName = ArrayList<String>()
        val serviceImage = ArrayList<String>()
        val amount = ArrayList<String>()
        val address = ArrayList<String>()
        val engineerName = ArrayList<String>()
        myOrdersList.forEach { orderlist ->
            serviceName.add(
                myServiceList.firstOrNull { it.id.toInt() == orderlist.serviceId }?.name ?: ""
            )
            serviceImage.add(
                myServiceList.firstOrNull { it.id.toInt() == orderlist.serviceId }?.icon ?: ""
            )
            amount.add(
                myTransactionList.firstOrNull { it.orderId == orderlist.orderId }?.amount
                    ?: ""
            )
            address.add(
                users.firstOrNull { it.id.toInt() == orderlist.customerId && it.addressId == orderlist.addressId }?.address1
                    ?: "".plus(
                        users.firstOrNull { it.id.toInt() == orderlist.customerId && it.addressId == orderlist.addressId }?.address2
                            ?: ""
                    ).plus(
                        users.firstOrNull { it.id.toInt() == orderlist.customerId && it.addressId == orderlist.addressId }?.address3
                            ?: ""
                    )
            )
            engineerName.add(
                users.firstOrNull { it.id.toInt() == orderlist.engineerId }?.name ?: ""
            )
        }
        val orderOnDate = myOrdersList[position].createdOn.toInstant().toLocalDateTime(
            TimeZone.currentSystemDefault()
        ).date.toDate().toCustomPatten("dd/MM/yy")
        holder.binding.assignAt.text = orderOnDate
        holder.binding.orderId.text = holder.binding.orderId.context.getString(
            R.string.order_id,
            myOrdersList[position].orderId
        )
        if (serviceName[position].isNotEmpty()) holder.binding.serviceName.text =
            serviceName[position]
        holder.binding.orderStatus.text =
            if (myOrdersList[position].status == Constants.CANCEL_ORDER_STATUS)
                "CANCELED"
            else
                myOrdersList[position].status

        if (engineerName[position].isNotEmpty()) holder.binding.assignTo.text =
            "Assigned to: ${engineerName[position]}"

        if (address[position].isNotEmpty()) holder.binding.userAddress.text = address[position]


        holder.binding.root.setOnSingleClickListener {
            clickListener(myOrdersList[position])
        }
    }

    override fun getItemCount(): Int {
        return myOrdersList.size
    }
}