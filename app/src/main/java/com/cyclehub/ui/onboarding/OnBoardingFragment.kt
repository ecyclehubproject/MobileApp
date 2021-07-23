package com.cyclehub.ui.onboarding

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
import coil.load
import com.cyclehub.R
import com.cyclehub.databinding.FragmentOnboardingBinding
import com.cyclehub.db.DataManager
import com.cyclehub.model.IntroSlide
import com.cyclehub.other.Constants
import com.cyclehub.utils.colorSpannableStringWithUnderLineOneEnd
import com.cyclehub.utils.colorSpannableStringWithUnderLineOneStart
import com.cyclehub.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding
    val handler = Handler(Looper.getMainLooper())
    val delay = 2000L

    @Inject
    lateinit var dataManager: DataManager

    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlide(
                "Get best services delivered on a click",
                R.raw.onboarding
            ),
            IntroSlide(
                "Easy Connect with local vendors",
                R.raw.onboarding2
            ),
            IntroSlide(
                "Sign up as Customer Vendor or Engineer",
                R.raw.onboarding3
            )
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = introSliderAdapter
        binding.indicator.setViewPager(binding.viewPager)
        binding.logo.load(R.drawable.logo_ecyclehub)
        dataManager.isLogin.asLiveData().observe(viewLifecycleOwner) {
            if (it)
                dataManager.userType.asLiveData().observe(viewLifecycleOwner) { userType ->
                    when (userType) {
                        Constants.CUSTOMER -> findNavController().navigate(
                            OnBoardingFragmentDirections.actionOnboardingFragmentToExploreNavigation()
                        )
                        Constants.VENDOR -> findNavController().navigate(
                            OnBoardingFragmentDirections.actionOnboardingFragmentToVendorNavigation()
                        )
                        Constants.ENGINEER -> findNavController().navigate(
                            OnBoardingFragmentDirections.actionOnboardingFragmentToEngineerNavigation()
                        )
                    }
                }
        }

        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val runnable = Runnable { binding.viewPager.currentItem = position + 1 }
                    if (position < binding.viewPager.adapter?.itemCount ?: 0) {
                        handler.postDelayed(runnable, delay)
                    }
                    if (position + 1 == binding.viewPager.adapter?.itemCount) {
                        handler.postDelayed({
                            binding.viewPager.currentItem = 0
                        }, delay)
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    /**
                     * The user swiped forward or back and we need to
                     * invalidate the previous handler.
                     */
                    if (state == SCROLL_STATE_DRAGGING) handler.removeMessages(0)
                }
            })
        binding.buttonSignUp.setOnSingleClickListener {
            findNavController().navigate(OnBoardingFragmentDirections.actionOnboardingFragmentToSignupFragment())
        }
        binding.vendorLoginButton.colorSpannableStringWithUnderLineOneStart(
            "Click Here",
            "to Become Service Partners with \ne-Cyclehub"
        ) {
            findNavController()
                .navigate(OnBoardingFragmentDirections.actionOnboardingFragmentToVendorLoginFragment())
        }

        binding.vendorLoginButton.invalidate()

        binding.cutomerLoginButton.colorSpannableStringWithUnderLineOneEnd(
            "Existing user?",
            "Login Here"
        ) {
            findNavController().navigate(OnBoardingFragmentDirections.actionOnboardingFragmentToLoginFragment())
        }

        binding.vendorLoginButton.invalidate()

    }
}
