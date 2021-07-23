package com.cyclehub.ui.vendordashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyclehub.databinding.VendorEngineerItemContainerBinding
import com.cyclehub.model.EngineerData
import com.cyclehub.utils.loadSvgFromUrl
import com.cyclehub.utils.setOnSingleClickListener

class EngineerListAdapter(
    private val myEngineerList: Array<EngineerData>,
    val clickListener: (EngineerData) -> Unit
) :
    RecyclerView.Adapter<EngineerListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: VendorEngineerItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            VendorEngineerItemContainerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.nameUser.text = myEngineerList[position].userId.name.trim()
        holder.binding.engineerImage.loadSvgFromUrl(myEngineerList[position].userId.picture)
        holder.binding.root.setOnSingleClickListener {
            clickListener(myEngineerList[position])
        }
    }

    override fun getItemCount(): Int {
        return myEngineerList.size
    }
}