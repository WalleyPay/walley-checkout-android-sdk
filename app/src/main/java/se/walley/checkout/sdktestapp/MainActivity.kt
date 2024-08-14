package se.walley.checkout.sdktestapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.walley.checkout.sdk.WalleyCheckoutEnvironment
import se.walley.checkout.sdk.WalleyCheckoutEventListener
import se.walley.checkout.sdk.WalleyCheckoutSDK
import se.walley.checkout.sdktestapp.ui.theme.SDKTestAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SDKTestAPPTheme {
                Checkout(context = this)
            }
        }
    }
}

@Composable
fun Checkout(context: Context) {
    // Remember the state of the TextField input
    val publicToken = remember { mutableStateOf("") }

    // Layout with Column to arrange the input and button vertically
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        // TextField for user to enter the publicToken
        OutlinedTextField(
            value = publicToken.value,
            onValueChange = { publicToken.value = it },
            label = { Text("Enter Public Token") },
            modifier = Modifier.fillMaxWidth()
        )

        // Button to trigger the showCheckout function
        // Parameters: https://dev.walleypay.com/docs/checkout/renderiframe/#script-element
        Button(
            onClick = {
                WalleyCheckoutSDK.showCheckout(
                    context,
                    WalleyCheckoutEnvironment.UAT,
                    publicToken.value,
                    "",
                    "",
                    "",
                    "",
                    "",
                    eventListener = object : WalleyCheckoutEventListener {
                        override fun onEventReceived(event: String) {
                            println("Checkout event received from SDK: $event")
                        }
                    }
                )
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Show Checkout")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IframePreview() {
    SDKTestAPPTheme {
        // Preview without context
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Enter Public Token") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {},
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Show Checkout")
            }
        }
    }
}