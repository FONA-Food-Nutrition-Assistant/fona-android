package com.bangkit23b2.fonaapp.ui.intro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit23b2.fonaapp.databinding.OnboardingItemContainerBinding

class IntroSliderAdapter(private val introSlides: List<IntroSlider>) :
    RecyclerView.Adapter<IntroSliderAdapter.IntroSliderViewHolder>() {

    inner class IntroSliderViewHolder(private val binding: OnboardingItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(introSlider: IntroSlider) {
            binding.tvTitleIntro.text = introSlider.title
            binding.tvDescriptionIntro.text = introSlider.description
            binding.ivWelcome.setImageResource(introSlider.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSliderViewHolder {
        val binding = OnboardingItemContainerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IntroSliderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return introSlides.size
    }

    override fun onBindViewHolder(holder: IntroSliderViewHolder, position: Int) {
        holder.bind(introSlides[position])
    }
}
