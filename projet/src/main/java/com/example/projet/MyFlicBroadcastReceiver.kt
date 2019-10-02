package com.example.projet

import android.content.Context
import android.util.Log
import io.flic.lib.FlicBroadcastReceiver
import io.flic.lib.FlicButton

class MyFlicBroadcastReceiver : FlicBroadcastReceiver() {
    override fun onRequestAppCredentials(context: Context) {
        MyFlicManager.setFlicCredentials()
    }

    override fun onButtonUpOrDown(
        context: Context,
        button: FlicButton,
        wasQueued: Boolean,
        timeDiff: Int,
        isUp: Boolean,
        isDown: Boolean
    ) {

        if (isUp) {
            Log.d("MainActivity", "isUp")
            // Code for button up event here
        } else {
            Log.d("MainActivity", "!isUp")
            // Code for button down event here
        }

    }

    override fun onButtonRemoved(context: Context, button: FlicButton) {
        Log.d("MainActivity", "Button was removed")
    }
}
