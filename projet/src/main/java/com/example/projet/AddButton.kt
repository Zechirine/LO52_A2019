package com.example.projet

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import io.flic.lib.FlicAppNotInstalledException
import io.flic.lib.FlicBroadcastReceiverFlags
import io.flic.lib.FlicButton
import io.flic.lib.FlicManager

class AddButton : AppCompatActivity() {
    private val TAG = "AddButton"
    private lateinit var selectButtonButton: Button
    private lateinit var addButtonButton: Button
    private lateinit var roomNumberTextView: TextView
    private val myButtonManager: MyButtonManager = MyApplication.myButtonManager
    private var roomNumber: Int = 0
    private lateinit var flicButton: FlicButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_button)

        selectButtonButton = findViewById(R.id.selectButton)
        addButtonButton = findViewById(R.id.addButton)
        roomNumberTextView = findViewById(R.id.roomNumber)

        setListeners()
    }

    private fun setListeners() {
        selectButtonButton.setOnClickListener {
            launchChooseAFlicButtonActivity()
        }

        // TODO prevent if roomNumber not set or flicButton not selected
        addButtonButton.setOnClickListener {
            roomNumber = Integer.parseInt(roomNumberTextView.text.toString())

            val myButton = MyButton(roomNumber, flicButton)
            myButtonManager.myButtons.add(myButton)

            launchMainActivity()

            Toast.makeText(this@AddButton, String.format(getString(R.string.this_button_has_been_succesfully_added), myButton.getName()), Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchChooseAFlicButtonActivity() {
        try {
            FlicManager.getInstance(this) { manager ->
                manager.initiateGrabButton(this@AddButton)
            }
        }
        catch (err: FlicAppNotInstalledException) {
            launchFlicAppNotInstalledActivity()
        }
    }

    private fun launchMainActivity() {
        val flicAppNotInstalledActivity = Intent(applicationContext, MainActivity::class.java)
        startActivity(flicAppNotInstalledActivity)
        finish()
    }

    private fun launchFlicAppNotInstalledActivity() {
        val flicAppNotInstalledActivity = Intent(applicationContext, FlicAppNotInstalled::class.java)
        startActivity(flicAppNotInstalledActivity)
        finish()
    }

    @SuppressLint("MissingSuperCall")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        FlicManager.getInstance(this) { manager ->
            val flicButtonPending = manager.completeGrabButton(requestCode, resultCode, data)

            if (flicButtonPending != null) {
                flicButtonPending.registerListenForBroadcast(FlicBroadcastReceiverFlags.UP_OR_DOWN or FlicBroadcastReceiverFlags.REMOVED)

                if(!isButtonAlreadyAdded(flicButtonPending)){
                    flicButton = flicButtonPending
                    selectButtonButton.text = flicButtonPending.name
                } else{
                    Toast.makeText(this@AddButton, String.format(getString(R.string.this_button_has_already_been_added), flicButtonPending.name), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isButtonAlreadyAdded(flicButton: FlicButton): Boolean {
        var isButtonAlreadyAdded = false
        for (myFlicButton in myButtonManager.myButtons) {
            if (myFlicButton.flicButton.buttonId == flicButton.buttonId){
                isButtonAlreadyAdded = true
            }
        }
        return isButtonAlreadyAdded
    }
}
