package se.walley.checkout.sdk

enum class WalleyCheckoutEvent(val eventName: String) {
    CUSTOMER_UPDATED("walleyCheckoutCustomerUpdated"),
    LOCKED("walleyCheckoutLocked"),
    UNLOCKED("walleyCheckoutUnlocked"),
    RESUMED("walleyCheckoutResumed"),
    SHIPPING_UPDATED("walleyCheckoutShippingUpdated"),
    PURCHASE_COMPLETED("walleyCheckoutPurchaseCompleted"),
    CRM_UPDATED("walleyCheckoutCrmUpdated"),
    ORDER_VALIDATION_FAILED("walleyCheckoutOrderValidationFailed");
}
