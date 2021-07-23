package com.cyclehub.ui.servicedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyclehub.databinding.ServicesDescriptionItemContainerBinding

class ServicesDescriptionAdapter(private val servicesDescriptionList: List<String>) :
    RecyclerView.Adapter<ServicesDescriptionAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ServicesDescriptionItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ServicesDescriptionItemContainerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.description.text = servicesDescriptionList[position]
    }

    override fun getItemCount(): Int {
        return servicesDescriptionList.size
    }
}