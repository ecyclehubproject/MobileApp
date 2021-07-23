package com.cyclehub.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cyclehub.databinding.PromotionItemContainerBinding
import com.cyclehub.model.ModelXXXXXXXX
import com.cyclehub.utils.loadAnyUrl

class PromotionSliderAdapter(private val promoSlides: List<ModelXXXXXXXX>) :
    RecyclerView.Adapter<PromotionSliderAdapter.PromoSlideViewHolder>() {
    var onImagePassed: ((imageView: ImageView) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoSlideViewHolder {
        return PromoSlideViewHolder(
            PromotionItemContainerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return promoSlides.size
    }

    override fun onBindViewHolder(holder: PromoSlideViewHolder, position: Int) {
        holder.bind(promoSlides[position])
    }

    inner class PromoSlideViewHolder(private val binding: PromotionItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(promoSlides: ModelXXXXXXXX) {
            binding.imageSlideIcon.loadAnyUrl(promoSlides.value)
            onImagePassed?.invoke(binding.imageSlideIcon)
        }
    }
}