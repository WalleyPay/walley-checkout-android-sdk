package se.walley.checkout.sdk

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

class LoaderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val publicToken = intent.getStringExtra("publicToken") ?: ""
        val lang = intent.getStringExtra("lang") ?: ""
        val actionColor = intent.getStringExtra("actionColor") ?: ""

        val htmlContent = buildHtmlContent(publicToken, lang, actionColor)

        setContent {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = WebViewClient()
                        loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                    }
                }
            )
        }
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

private fun createLoaderUrl(): String {
    val baseUrl = "https://api.ci.walleydev.com"

    return "$baseUrl/walley-checkout-loader.js";
}