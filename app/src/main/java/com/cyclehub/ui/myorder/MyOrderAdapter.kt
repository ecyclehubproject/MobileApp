package com.cyclehub.ui.myorder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyclehub.R
import com.cyclehub.databinding.MyOrderItemContainerBinding
import com.cyclehub.model.OrderX
import com.cyclehub.model.ServiceX
import com.cyclehub.model.TransactionX
import com.cyclehub.model.UserX
import com.cyclehub.other.Constants
import com.cyclehub.utils.loadSvgFromUrl
import com.cyclehub.utils.setOnSingleClickListener
import com.cyclehub.utils.toCustomPatten
import com.cyclehub.utils.toDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class MyOrderAdapter(
    private val myOrdersList: Array<OrderX>,
    private val myServiceList: Array<ServiceX>,
    private val myTransactionList: Array<TransactionX>,
    private val users: Array<UserX>,
    val clickListener: (OrderX) -> Unit

) :
    RecyclerView.Adapter<MyOrderAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: MyOrderItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MyOrderItemContainerBinding.inflate(
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
        val userName = ArrayList<String>()
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
            userName.add(
                users.firstOrNull { it.id.toInt() == orderlist.customerId }?.name ?: ""
            )
        }
        val orderOnDate = myOrdersList[position].createdOn.toInstant().toLocalDateTime(
            TimeZone.currentSystemDefault()
        ).date.toDate().toCustomPatten("dd/MM/yy")
        if (serviceName[position].isNotEmpty()) holder.binding.serviceName.text =
            serviceName[position]
        holder.binding.serviceImage.loadSvgFromUrl(serviceImage[position])
        if (userName[position].isNotEmpty()) holder.binding.nameUser.text = userName[position]
        holder.binding.orderOn.text = orderOnDate
        if (amount[position].isNotEmpty())
            holder.binding.amount.text =
                holder.binding.amount.context.getString(
                    R.string.amount,
                    amount[position].toDouble()
                )
        if (address[position].isNotEmpty()) holder.binding.userAddress.text = address[position]
        holder.binding.orderId.text = holder.binding.orderId.context.getString(
            R.string.order_id,
            myOrdersList[position].orderId
        )
        holder.binding.orderStatus.text =
            if (myOrdersList[position].status == Constants.CANCEL_ORDER_STATUS)
                "CANCELED"
            else
                myOrdersList[position].status
        holder.binding.root.setOnSingleClickListener {
            clickListener(myOrdersList[position])
        }
    }

    override fun getItemCount(): Int {
        return myOrdersList.size
    }
}