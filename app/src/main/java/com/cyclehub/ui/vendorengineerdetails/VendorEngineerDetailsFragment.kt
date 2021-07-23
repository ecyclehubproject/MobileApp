package com.cyclehub.ui.vendorengineerdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.cyclehub.databinding.VendorEngineerDetailsFragmentBinding
import com.cyclehub.utils.setOnSingleClickListener

class VendorEngineerDetailsFragment : Fragment() {

    private lateinit var binding: VendorEngineerDetailsFragmentBinding
    private val args: VendorEngineerDetailsFragmentArgs by navArgs()

    companion object {
        fun newInstance() = VendorEngineerDetailsFragment()
    }

    private lateinit var viewModel: VendorEngineerDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VendorEngineerDetailsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.engineerName.text = args.engineerData.userId.name
        binding.phoneNo.text = args.engineerData.userId.phoneNumber

        binding.phone.setOnClickListener {
            startCall(args.engineerData.userId.phoneNumber)
        }
        binding.whatsapp.setOnClickListener {
            startSupportChat(args.engineerData.userId.phoneNumber)
        }
        binding.email.setOnSingleClickListener {
            Toast.makeText(context,"Coming soon",Toast.LENGTH_SHORT).show()
        }
    }

    private fun startSupportChat(number: String) {
        if (number.isNotEmpty())
            try {
                val headerReceiver =
                    "" // Replace with your message.
                val trimToNumber = "+91$number" //10 digit number
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://wa.me/$trimToNumber/?text=$headerReceiver")
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        else {
            Toast.makeText(context, "Number is not valid", Toast.LENGTH_SHORT).show()
        }
    }


    private fun startCall(number: String) {
        if (number.isNotEmpty())
            startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null)))
        else
            Toast.makeText(context, "Number is not valid", Toast.LENGTH_SHORT).show()
    }
}