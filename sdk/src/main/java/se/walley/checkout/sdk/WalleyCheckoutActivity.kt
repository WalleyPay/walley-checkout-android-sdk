package se.walley.checkout.sdk

import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

class WalleyCheckoutActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true

                        webViewClient = object : WalleyCheckoutWebViewClient(context) {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)

                                val eventListenerContent = buildEventListenerContent()
                                evaluateJavascript(eventListenerContent, null)
                            }
                        }

                        addJavascriptInterface(WebAppInterface(), "Android")

                        val environment = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra("environment", WalleyCheckoutEnvironment::class.java) ?: WalleyCheckoutEnvironment.PRODUCTION
                        } else {
                            @Suppress("DEPRECATION")
                            intent.getSerializableExtra("environment") as? WalleyCheckoutEnvironment ?: WalleyCheckoutEnvironment.PRODUCTION
                        }

                        val htmlContent = buildHtmlContent(
                            environment,
                            intent.getStringExtra("publicToken") ?: "",
                            intent.getStringExtra("lang") ?: "",
                            intent.getStringExtra("padding") ?: "",
                            intent.getStringExtra("containerId") ?: "",
                            intent.getStringExtra("actionColor") ?: "",
                            intent.getStringExtra("actionTextColor") ?: ""
                        )

                        loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                    }
                }
            )
        }
    }

    private fun buildHtmlContent(
        environment: WalleyCheckoutEnvironment,
        publicToken: String,
        lang: String,
        padding: String,
        containerId: String,
        actionColor: String,
        actionTextColor: String
    ): String {
        val loaderUrl = createLoaderUrl(environment)

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Walley Checkout</title>
            </head>
            <body>
                <script
                    src="$loaderUrl"
                    data-token="$publicToken"
                    data-lang="$lang"
                    data-padding="$padding"
                    data-container-id="$containerId"
                    data-action-color="$actionColor"
                    data-action-text-color="$actionTextColor"
                    data-webview="true">
                </script>
            </body>
            </html>
        """.trimIndent()
    }

    private fun buildEventListenerContent(): String {
        return WalleyCheckoutEvent.entries.joinToString("") { event ->
            """
                document.addEventListener('${event.eventName}', function() {
                    Android.onEvent('${event.eventName}');
                });
            """.trimIndent()
        }
    }

    private fun createLoaderUrl(environment: WalleyCheckoutEnvironment): String {
        val baseUrl = when (environment) {
            WalleyCheckoutEnvironment.PRODUCTION -> "https://checkout.collector.se"
            WalleyCheckoutEnvironment.UAT -> "https://checkout-uat.collector.se"
            WalleyCheckoutEnvironment.CI -> "https://checkout-ci.collector.se"
        }
        return "$baseUrl/walley-checkout-loader.js"
    }

    inner class WebAppInterface {
        @JavascriptInterface
        fun onEvent(event: String) {
            WalleyCheckoutSDK.notifyEvent(event)
        }
    }
}
