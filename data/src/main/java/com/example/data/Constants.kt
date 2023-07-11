package com.example.data

import java.util.UUID

object Constants {

    // References
    const val CATEGORIES_REF = "categories"
    const val PRODUCTS_REF = "products"
    const val DEALS_REF = "deals"
    const val CLIENTS_REF = "clients"
    const val CATEGORY_REF = "category"
    const val VOUCHERS_REF = "vouchers"
    const val COINS_PRODUCTS = "coins products"

    // Documents
    const val DEAL_OF_THE_DAY_DOC = "deal_of_the_day"

    // QUERY
    const val DEAL_TYPE_FIELD = "isDeal"
    const val PRODUCT_CATEGORY_NAME = "category"

    // Bluetooth things
    const val DESIRED_DEVICE_ADDRESS = "DD:0D:30:FE:41:EF"
    val SERVICE_UUID: UUID = UUID.fromString("0000FFF0-0000-1000-8000-00805F9B34FB")
    val CHARACTERISTICS_UUID: UUID = UUID.fromString("0000FFF1-0000-1000-8000-00805F9B34FB")
    val CLIENT_CHARACTERISTIC_CONFIG_UUID: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")


    // Stripe
    const val SK_TOKEN = "sk_test_51NRmkPHhDraBeFAVmxLwQuAWlCVL50xdFbIxmtP1ad0sauaSZdc3OHdMnoX4ZISKEWYSS3ba5T4qvAHx3tHMl86d007C0nKXNg"
    const val BASE_URL = "https://api.stripe.com/"
    const val MERCHANT_DISPLAY_NAME = "SMART"
    const val PUBLISHABLE_KEY =
        "pk_test_51NRmkPHhDraBeFAVpogfOwXblsMzitVUt3Ts79t1CznTGNhyCCqrYyxNeWdtfF709PB2DIQeQ2wgbZO0pncN6vu200XGmMclbS"
}