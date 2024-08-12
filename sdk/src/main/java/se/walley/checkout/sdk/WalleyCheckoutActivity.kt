package se.walley.checkout.sdk

import android.os.Bundle
import android.util.Log
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

        val publicToken = intent.getStringExtra("publicToken") ?: ""
        val lang = intent.getStringExtra("lang") ?: ""
        val actionColor = intent.getStringExtra("actionColor") ?: ""

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

                        val htmlContent = buildHtmlContent(publicToken, lang, actionColor)

                        loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                    }
                }
            )
        }
    }

    private fun buildHtmlContent(publicToken: String, lang: String, actionColor: String): String {
        val loaderUrl = createLoaderUrl()

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
                    data-actionColor="$actionColor"
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

    private fun createLoaderUrl(): String {
        val baseUrl = "https://api.ci.walleydev.com"
        return "$baseUrl/walley-checkout-loader.js"
    }

    inner class WebAppInterface {
        @JavascriptInterface
        fun onEvent(event: String) {
            WalleyCheckoutSDK.notifyEvent(event)
        }
    }
}
