package fr.utbm.lo52.flicYouAndroid

import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import fr.utbm.lo52.flicYouAndroid.MyApplication.Companion.myWatchManager
import io.flic.lib.FlicBroadcastReceiver
import io.flic.lib.FlicButton
import java.io.IOException

class MyFlicBroadcastReceiver : FlicBroadcastReceiver() {

    private companion object {
        lateinit var socket: BluetoothSocket
    }

    override fun onRequestAppCredentials(context: Context) {
        MyFlicManager.setFlicCredentials()
    }

//    override fun onButtonUpOrDown(
//        context: Context,
//        button: FlicButton,
//        wasQueued: Boolean,
//        timeDiff: Int,
//        isUp: Boolean,
//        isDown: Boolean
//    ) {
//
//        if (isUp) {
//            Log.i("MainActivity", "isUp")
//            // Code for button up event here
//        } else {
//            Log.i("MainActivity", "!isUp")
//            // Code for button down event here
//        }
//    }

    override fun onButtonSingleOrDoubleClickOrHold(
        context: Context,
        button: FlicButton,
        wasQueued: Boolean,
        timeDiff: Int,
        isSingleClick: Boolean,
        isDoubleClick: Boolean,
        isHold: Boolean
    ) {
        try {
            socket = myWatchManager.myWatches.get(0).getBluetoothSocket()

            Log.i("isSingleClick", isSingleClick.toString())
            Log.i("isDoubleClick", isDoubleClick.toString())
            Log.i("isHold", isHold.toString())

            if (isSingleClick)
                sendCommand("Repas")
            if (isDoubleClick)
                sendCommand("Toilettes")
            if (isHold)
                sendCommand("Santé")
        } catch (e : Exception) {
            Log.i("Error", "Watch not connected")
        }
    }

    override fun onButtonRemoved(context: Context, button: FlicButton) {
        Log.i("MainActivity", "Button was removed")
    }

    private fun sendCommand(input: String) {
        try {
            socket.outputStream.write(input.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
