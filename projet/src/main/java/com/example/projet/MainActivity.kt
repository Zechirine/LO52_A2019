package com.example.projet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.flic.lib.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyFlicManager.setFlicCredentials()

        try {
            FlicManager.getInstance(this) { manager ->
                launchChooseAFlicButtonActivity(manager)
            }
        }
        catch (err: FlicAppNotInstalledException) {
            launchFlicAppNotInstalledActivity()
            finish()
        }

        setContentView(R.layout.activity_main)
    }

    private fun launchFlicAppNotInstalledActivity() {
        val flicAppNotInstalledActivity =
            Intent(getApplicationContext(), FlicAppNotInstalled::class.java)
        startActivity(flicAppNotInstalledActivity)
    }

    private fun launchChooseAFlicButtonActivity(manager: FlicManager) {
        manager.initiateGrabButton(this@MainActivity)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // TODO sélectionner plusieurs flic buttons

        FlicManager.getInstance(this) { manager ->
            val button = manager.completeGrabButton(requestCode, resultCode, data)

            if (button != null) {
                button.registerListenForBroadcast(FlicBroadcastReceiverFlags.UP_OR_DOWN or FlicBroadcastReceiverFlags.REMOVED)

                grabedButtonText.text = button.toString()

            } else {
                launchChooseAFlicButtonActivity(manager)
                Toast.makeText(this@MainActivity, "Vous devez sélectionner au moins un bouton pour continuer", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
