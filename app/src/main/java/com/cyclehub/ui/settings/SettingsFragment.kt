package com.cyclehub.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cyclehub.R
import com.cyclehub.databinding.FragmentSettingsBinding
import com.cyclehub.db.DataManager
import com.cyclehub.model.UserData
import com.cyclehub.other.Constants.ISPROFILEUPDATED
import com.cyclehub.ui.MainActivity
import com.cyclehub.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val settingViewModel: SettingViewModel by activityViewModels()
    private var userInfo: UserData? = null

    @Inject
    lateinit var dataManager: DataManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileIcon.loadSvg(R.raw.profile)
        binding.orderIcon.loadSvg(R.raw.history)
        binding.transactionIcon.loadSvg(R.raw.transactions)
        binding.myBikeIcon.loadSvg(R.raw.mybike)
        binding.logoutIcon.loadSvg(R.raw.logout)

        binding.orderButton.setOnSingleClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToMyOrdersFragment())
        }
        binding.transactionButton.setOnSingleClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToMyTransactionsFragment())
        }
        binding.editProfile.setOnSingleClickListener {
            findNavController().navigate(
                SettingsFragmentDirections.actionSettingsFragmentToCustomerEditProfileFragment(
                    userInfo
                )
            )
        }
        binding.myBikeButton.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToMyBikeFragment())
        }
        binding.logoutButton.setOnSingleClickListener {
            val dialog = BottomSheetDialog(binding.logoutButton.context)
            val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null)
            dialog.setContentView(bottomSheet)
            val btnClose = bottomSheet.findViewById<AppCompatButton>(R.id.cancel_button)
            val btnOk = bottomSheet.findViewById<AppCompatButton>(R.id.ok_button)
            val title = bottomSheet.findViewById<AppCompatTextView>(R.id.title)
            val subTitle = bottomSheet.findViewById<AppCompatTextView>(R.id.sub_title)
            title.text = getString(R.string.logout)
            subTitle.text = getString(R.string.logout_confirm)
            dialog.dismissWithAnimation = true
            btnClose.setOnSingleClickListener {
                dialog.dismiss()
            }
            btnOk.setOnSingleClickListener {
                dialog.dismiss()
                lifecycleScope.launch {
                    dataManager.clearData()
                    dataManager.clearUserToken()
                    emptyDatabase()
                    delay(1000)
                    Utils.triggerRebirth(btnOk.context, MainActivity::class.java)
                }
            }
            dialog.show()


        }
        settingViewModel.getUser()

        settingViewModel.user.observe(viewLifecycleOwner, {

            when (it.status) {
                Resource.Status.SUCCESS -> {
                    showProgress(false)
                    it?.data?.let { user ->
                        userInfo = user.model
                        binding.name.text = userInfo?.name
                        binding.email.text = userInfo?.phoneNumber
                        binding.profileImage.loadSvgFromUrl(userInfo!!.picture)
                    }
                }

                Resource.Status.ERROR ->
                    showProgress(false)


                Resource.Status.LOADING ->
                    showProgress(true)
            }
        })
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            ISPROFILEUPDATED
        )?.observe(
            viewLifecycleOwner
        ) { result ->
            if (result) settingViewModel.getUser()

        }
    }


    private fun showProgress(boolean: Boolean) {
        binding.progress.progressBar.isVisible = boolean
    }

}