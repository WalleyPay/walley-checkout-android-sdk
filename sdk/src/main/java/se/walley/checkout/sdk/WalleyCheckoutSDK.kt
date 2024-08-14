package se.walley.checkout.sdk

import android.content.Context
import android.content.Intent

object WalleyCheckoutSDK {

    private var checkoutEventListener: WalleyCheckoutEventListener? = null

    fun showCheckout(
        context: Context,
        environment: WalleyCheckoutEnvironment = WalleyCheckoutEnvironment.PRODUCTION,
        publicToken: String,
        lang: String,
        padding: String,
        containerId: String,
        actionColor: String,
        actionTextColor: String,
        eventListener: WalleyCheckoutEventListener? = null
    ) {
        this.checkoutEventListener = eventListener
        val intent = Intent(context, WalleyCheckoutActivity::class.java).apply {
            putExtra("environment", environment)
            putExtra("publicToken", publicToken)
            putExtra("lang", lang)
            putExtra("padding", padding)
            putExtra("containerId", containerId)
            putExtra("actionColor", actionColor)
            putExtra("actionTextColor", actionTextColor)
        }
        context.startActivity(intent)
    }

    internal fun notifyEvent(event: String) {
        checkoutEventListener?.onEventReceived(event)
    }
}

