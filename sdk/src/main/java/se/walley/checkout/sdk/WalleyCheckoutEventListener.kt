package se.walley.checkout.sdk

interface WalleyCheckoutEventListener {
    fun onEventReceived(event: String)
}