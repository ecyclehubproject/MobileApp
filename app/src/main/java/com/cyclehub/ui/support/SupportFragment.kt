package com.cyclehub.ui.support

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.cyclehub.databinding.FragmentSupportBinding


class SupportFragment : Fragment() {
    private lateinit var binding: FragmentSupportBinding
    private var webURL = "https://e-cyclehub.s3.ap-south-1.amazonaws.com/support.html" // Change it with your URL

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSupportBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean {
                view.loadUrl(url)
                return true
            }
        }

        binding.webview.loadUrl(webURL)
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.allowContentAccess = true
        binding.webview.settings.domStorageEnabled = true
        binding.webview.settings.useWideViewPort = true
    }

}