package se.walley.checkout.sdk

import android.content.Context
import android.content.Intent

object WalleyCheckoutSDK {
    fun showCheckout(context: Context, publicToken: String, lang: String, actionColor: String) {
        val intent = Intent(context, LoaderActivity::class.java).apply {
            putExtra("publicToken", publicToken)
            putExtra("lang", lang)
            putExtra("actionColor", actionColor)
        }

        context.startActivity(intent)
    }
}