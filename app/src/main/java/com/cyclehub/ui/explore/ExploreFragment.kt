package com.cyclehub.ui.explore

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.cyclehub.databinding.FragmentExploreBinding
import com.cyclehub.db.DataManager
import com.cyclehub.di.AppModule.YOUR_TOKEN
import com.cyclehub.utils.Resource
import com.cyclehub.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [ExploreFragment] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ExploreFragment : Fragment() {

    @Inject
    lateinit var dataManager: DataManager
    lateinit var binding: FragmentExploreBinding
    private val exploreViewModel: ExploreViewModel by viewModels()
    val handler = Handler(Looper.getMainLooper())
    val delay = 2000L
    var vehicleId: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exploreViewModel.getSlider()
        dataManager.userToken.asLiveData().observe(viewLifecycleOwner, {
            YOUR_TOKEN = it
        })
        getServices()

        exploreViewModel.slider.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    val promotionSliderAdapter = it.data?.model?.toList()?.let { it1 ->
                        PromotionSliderAdapter(
                            it1
                        )
                    }
                    binding.promotionViewpager.adapter = promotionSliderAdapter
                    binding.indicator.setViewPager(binding.promotionViewpager)

                }
                Resource.Status.ERROR -> {
                    showProgress(false)

                }
                Resource.Status.LOADING ->
                    showProgress(true)
            }
        })

        binding.promotionViewpager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val runnable =
                        Runnable { binding.promotionViewpager.currentItem = position + 1 }
                    if (position < binding.promotionViewpager.adapter?.itemCount ?: 0) {
                        handler.postDelayed(runnable, delay)
                    }
                    if (position + 1 == binding.promotionViewpager.adapter?.itemCount) {
                        handler.postDelayed({
                            binding.promotionViewpager.currentItem = 0
                        }, delay)
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager2.SCROLL_STATE_DRAGGING) handler.removeMessages(0)
                }
            })
    }

//    private fun startObserving() {
//        exploreViewModel.servespincode.observe(viewLifecycleOwner, {
//            when (it.status) {
//                Resource.Status.SUCCESS -> {
//                    showProgress(false)
//                    it?.data?.model?.let { serves ->
//                        if (serves.servesPinCode) getServices() else Utils.showEmptyView(
//                            binding.services,
//                            binding.emptyView,
//                            "Sorry, Our services aren't available in ${locationData.city} yet. Coming soon to your location."
//                        )
//
//                    }
//
//
//                }
//                Resource.Status.ERROR -> {
//                    showProgress(false)
//                    Utils.showEmptyView(
//                        binding.services,
//                        binding.emptyView,
//                        it.message.toString()
//                    )
//                }
//
//
//                Resource.Status.LOADING ->
//                    showProgress(true)
//            }
//        })
//
//
//    }

    private fun showProgress(boolean: Boolean) {
        binding.progress.progressBar.isVisible = boolean
    }

    private fun getServices() {
        exploreViewModel.services.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        binding.services.layoutManager = GridLayoutManager(context, 3)
                        binding.services.setHasFixedSize(true)
                        binding.services.adapter = ServicesAdapter(
                            it.data.toTypedArray()
                        ) { service ->
                            findNavController().navigate(
                                ExploreFragmentDirections.actionExploreFragmentToServicesDetailsFragment(
                                    service.id.toString()
                                )
                            )
                        }
                    }
                    lifecycleScope.launch {
                        delay(1000)
                        showProgress(false)
                    }

                }
                Resource.Status.ERROR -> {
                    showProgress(false)
                    Utils.showEmptyView(
                        binding.services,
                        binding.emptyView,
                        it.message.toString()
                    )
                }
                Resource.Status.LOADING ->
                    showProgress(true)
            }
        })
    }
}
