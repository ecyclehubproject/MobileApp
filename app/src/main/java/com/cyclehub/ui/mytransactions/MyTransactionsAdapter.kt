package com.cyclehub.ui.mytransactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.cyclehub.R
import com.cyclehub.databinding.MyTransactionsItemContainerBinding
import com.cyclehub.model.OrdersList
import com.cyclehub.model.Transactions
import com.cyclehub.utils.toDate
import com.cyclehub.utils.toDateStandardConcise
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class MyTransactionsAdapter(
    private val myTransactionsList: Array<Transactions>,
    private val findNavController: NavController
) :
    RecyclerView.Adapter<MyTransactionsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: MyTransactionsItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MyTransactionsItemContainerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.orderId.text = String.format(
            holder.binding.orderId.context.getString(R.string.order_id),
            myTransactionsList[position].transactionId
        )
        holder.binding.date.text = String.format(
            holder.binding.orderId.context.getString(R.string.transaction_date),
            myTransactionsList[position].createdOn.toInstant()
                .toLocalDateTime(
                    TimeZone.currentSystemDefault()
                ).date.toDate().toDateStandardConcise()
        )

        holder.binding.mode.text = String.format(
            holder.binding.orderId.context.getString(R.string.payment_mode),
            myTransactionsList[position].mode
        )
        holder.binding.status.text = String.format(
            holder.binding.orderId.context.getString(R.string.payment_status),
            myTransactionsList[position].status
        )


//        holder.binding.root.setOnSingleClickListener  {
//            findNavController.navigate(
//                MyTransactionsFragmentDirections.actionMyOrdersFragmentToMyOrderDetailsFragment(
//                    myOrdersList[position].id.toString()
//                )
//            )
//        }

    }

    override fun getItemCount(): Int {
        return myTransactionsList.size
    }
}