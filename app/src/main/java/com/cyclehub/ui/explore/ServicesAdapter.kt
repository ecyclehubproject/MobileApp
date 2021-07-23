package com.cyclehub.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyclehub.databinding.ServicesItemContainerBinding
import com.cyclehub.model.ModelServices
import com.cyclehub.utils.loadSvgFromUrl
import com.cyclehub.utils.setOnSingleClickListener

class ServicesAdapter(
    private val servicesList: Array<ModelServices>,
    val clickListener: (ModelServices) -> Unit
) :
    RecyclerView.Adapter<ServicesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ServicesItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ServicesItemContainerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.serviceTitle.text = servicesList[position].name
        holder.binding.serviceIcon.loadSvgFromUrl(servicesList[position].icon)
        holder.binding.root.setOnSingleClickListener  {
            clickListener(servicesList[position])
        }
    }

    override fun getItemCount(): Int {
        return servicesList.size
    }
}