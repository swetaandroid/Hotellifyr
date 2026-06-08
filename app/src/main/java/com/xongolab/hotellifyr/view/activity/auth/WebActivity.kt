package com.xongolab.hotellifyr.view.activity.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityWebBinding
import com.xongolab.hotellifyr.utils.Constants

class WebActivity : CoreActivity() {
    private lateinit var binding: ActivityWebBinding

    private var title: String = ""
    private var webUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreenMode(window)

        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        if (intent != null) {
            title = intent.getStringExtra(Constants.TITLE).toString()
            webUrl = intent.getStringExtra(Constants.WEB_URL).toString()
        }

        binding.toolbar.toolbarTitle.text = title
        binding.toolbar.btnBack.setOnClickListener(this)

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
        }
        binding.webView.loadUrl(webUrl)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> finish()
        }
    }
}