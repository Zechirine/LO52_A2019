package com.example.projet

import io.flic.lib.FlicManager

class MyFlicManager {
    companion object {
        val appId = "b7c91d93-6fb4-424d-bd95-adfb88db5768"
        val appSecret = "79366696-c022-4fe9-86f8-60fc367bcaab"
        val appName = "Valink"

        fun setFlicCredentials() {
            FlicManager.setAppCredentials(appId, appSecret, appName)
        }
    }
}