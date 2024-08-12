package se.walley.checkout.sdk

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

open class WalleyCheckoutWebViewClient(private val context: Context) : WebViewClient() {

   override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url.toString()

        when {
            url.startsWith("bankid://") -> {
                handleExternalAppUrl(url, "BankID app not installed")
                return true
            }
            url.startsWith("swish://") -> {
                handleExternalAppUrl(url, "Swish app not installed")
                return true
            }
        }

        return super.shouldOverrideUrlLoading(view, request)
    }

    private fun handleExternalAppUrl(url: String, errorMessage: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        if (intent.resolveActivity(context.packageManager) == null) {
            Log.e("Could not handle external URL $url", errorMessage)
            return
        }

        context.startActivity(intent)
    }
}