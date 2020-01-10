package fr.utbm.lo52.flicYouAndroid.devicesManagment

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import fr.utbm.lo52.flicYouAndroid.*
import fr.utbm.lo52.flicYouAndroid.FlicAppNotInstalled
import fr.utbm.lo52.flicYouAndroid.MyApplication
import fr.utbm.lo52.flicYouAndroid.MyButton
import fr.utbm.lo52.flicYouAndroid.MyButtonManager
import io.flic.lib.FlicAppNotInstalledException
import io.flic.lib.FlicBroadcastReceiverFlags
import io.flic.lib.FlicButton
import io.flic.lib.FlicManager
import kotlinx.android.synthetic.main.activity_add_button.*

class AddButton : AppCompatActivity() {
    private val TAG = "AddButton"
    private val myButtonManager: MyButtonManager = MyApplication.myButtonManager
    private lateinit var flicButton: FlicButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_button)

        setListeners()
    }

    private fun setListeners() {
        selectButton.setOnClickListener {
            launchChooseAFlicButtonActivity()
        }

        addButton.setOnClickListener {
            if (roomNumber.text.isNotEmpty()) {
                val myButton = MyButton(Integer.parseInt(roomNumber.text.toString()), flicButton)
                myButtonManager.myButtons.add(myButton)

                launchListButtonActivity()

                Toast.makeText(this@AddButton, String.format(getString(R.string.this_button_has_been_succesfully_added), myButton.getName()), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@AddButton, String.format(getString(R.string.the_room_must_be_fill)), Toast.LENGTH_SHORT).show()
            }
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

    private fun launchListButtonActivity() {
        val flicAppNotInstalledActivity = Intent(applicationContext, ListButtons::class.java)
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
                flicButtonPending.registerListenForBroadcast(FlicBroadcastReceiverFlags.CLICK_OR_DOUBLE_CLICK_OR_HOLD)

                if(!isButtonAlreadyAdded(flicButtonPending)){
                    flicButton = flicButtonPending
                    selectButton.text = flicButtonPending.name
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
