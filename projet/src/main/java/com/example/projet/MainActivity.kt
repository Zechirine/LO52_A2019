package com.example.projet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.flic.lib.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var myButtonManager: MyButtonManager = MyButtonManager()
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyFlicManager.setFlicCredentials()

        setContentView(R.layout.activity_main)
        setListeners()
    }

    private fun setListeners() {
        addAButtonBtn.setOnClickListener {
            launchChooseAFlicButtonActivity()
        }
    }

    private fun launchFlicAppNotInstalledActivity() {
        val flicAppNotInstalledActivity =
            Intent(getApplicationContext(), FlicAppNotInstalled::class.java)
        startActivity(flicAppNotInstalledActivity)
    }

    private fun launchChooseAFlicButtonActivity() {
        try {
            FlicManager.getInstance(this) { manager ->
                manager.initiateGrabButton(this@MainActivity)
            }
        }
        catch (err: FlicAppNotInstalledException) {
            launchFlicAppNotInstalledActivity()
            finish()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        FlicManager.getInstance(this) { manager ->
            val flicButton = manager.completeGrabButton(requestCode, resultCode, data)

            if (flicButton != null) {
                flicButton.registerListenForBroadcast(FlicBroadcastReceiverFlags.UP_OR_DOWN or FlicBroadcastReceiverFlags.REMOVED)

                // TODO move to : when save form
                if(!isButtonAlreadyAdded(flicButton)){
                    val roomId = 1 // TODO true roomId
                    val myButton = MyButton(flicButton, roomId)
                    addButton(myButton)
                } else{
                    Toast.makeText(this@MainActivity, "Ce bouton a déjà été ajouté.", Toast.LENGTH_SHORT).show() // TODO affiché le nom du boutton
                }
            }
        }
    }

    private fun addButton(myButton: MyButton) {
        myButtonManager.myButtons.add(myButton)

        refreshButtonList()

        Toast.makeText(this@MainActivity, "Le bouton a bien été ajouté.", Toast.LENGTH_SHORT).show() // TODO affiché le nom du boutton
    }

    private fun refreshButtonList() {
        var flicButtonsAsString = ""
        for (myFlicButton in myButtonManager.myButtons) {
            flicButtonsAsString += myFlicButton.toString()
        }
        grabedButtonText.text = flicButtonsAsString
    }

    private fun isButtonAlreadyAdded(flicButton: FlicButton): Boolean {
        var isButtonAlreadyAdded = false
        for (myFlicButton in myButtonManager.myButtons) {
            if (myFlicButton.flicButton.buttonId == flicButton.buttonId)
                isButtonAlreadyAdded = true
        }
        return isButtonAlreadyAdded
    }

}
