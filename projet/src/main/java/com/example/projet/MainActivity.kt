package com.example.projet

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import io.flic.lib.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFlicCredentials()

        try {
            FlicManager.getInstance(this) { manager ->
                manager.initiateGrabButton(this@MainActivity)
            }

            setContentView(R.layout.activity_main)
        }
        catch (err: FlicAppNotInstalledException) {
            val flicAppNotInstalledActivity = Intent(getApplicationContext(), FlicAppNotInstalled::class.java)
            startActivity(flicAppNotInstalledActivity)
            finish()
        }
    }

    private fun setFlicCredentials() {
        val appId = "b7c91d93-6fb4-424d-bd95-adfb88db5768"
        val appSecret = "79366696-c022-4fe9-86f8-60fc367bcaab"
        val appName = "Valink"
        FlicManager.setAppCredentials(appId, appSecret, appName)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        FlicManager.getInstance(this) { manager ->
            val button = manager.completeGrabButton(requestCode, resultCode, data)

            if (button != null) {
                button.registerListenForBroadcast(FlicBroadcastReceiverFlags.UP_OR_DOWN or FlicBroadcastReceiverFlags.REMOVED)
                Toast.makeText(this@MainActivity, "Grabbed a button", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Did not grab any button", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

class ExampleBroadcastReceiver : FlicBroadcastReceiver() {
    override fun onRequestAppCredentials(context: Context) {
        // Set app credentials by calling FlicManager.setAppCredentials here
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
        // Button was removed
        Log.d("MainActivity", "Button was removed")
    }
}
