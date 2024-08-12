package se.walley.checkout.sdk

import android.content.Context
import android.content.Intent

object WalleyCheckoutSDK {

    private var checkoutEventListener: WalleyCheckoutEventListener? = null

    fun showCheckout(
        context: Context,
        publicToken: String,
        lang: String,
        actionColor: String,
        eventListener: WalleyCheckoutEventListener? = null
    ) {
        this.checkoutEventListener = eventListener
        val intent = Intent(context, WalleyCheckoutActivity::class.java).apply {
            putExtra("publicToken", publicToken)
            putExtra("lang", lang)
            putExtra("actionColor", actionColor)
        }
        context.startActivity(intent)
    }

    internal fun notifyEvent(event: String) {
        checkoutEventListener?.onEventReceived(event)
    }
}

