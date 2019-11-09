package fr.utbm.lo52.flicYouAndroid

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bluetooth_control.*
import java.io.IOException

class BluetoothControl: AppCompatActivity() {

    private val myWatchManager: MyWatchManager =
        MyApplication.myWatchManager

    private companion object {
        lateinit var socket: BluetoothSocket
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_control)

        socket = myWatchManager.myWatches.get(0).getBluetoothSocket()

        control_led_on.setOnClickListener { sendCommand("Repas") }
        control_led_off.setOnClickListener { sendCommand("Toilettes") }
        control_led_disconnect.setOnClickListener { sendCommand("disconnect"); disconnect() }
    }

    private fun sendCommand(input: String) {
        try {
            socket.outputStream.write(input.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun disconnect() {
        try {
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        finish()
    }
}
