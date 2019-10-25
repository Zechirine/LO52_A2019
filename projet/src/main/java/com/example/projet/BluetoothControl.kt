package com.example.projet

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.projet.devicesManagment.ListWatches
import kotlinx.android.synthetic.main.activity_bluetooth_control.*
import java.io.IOException
import java.util.*

class BluetoothControl: AppCompatActivity() {

    private val myWatchManager: MyWatchManager =
        MyApplication.myWatchManager

    private companion object {
        val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var isConnected: Boolean = false
        var connectSuccess: Boolean = true

        lateinit var bluetoothAdapter: BluetoothAdapter
        lateinit var socket: BluetoothSocket
        lateinit var address: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_control)

        address = intent.getStringExtra(BluetoothDevicesList.EXTRA_ADDRESS)

        connectionAttemptToDevice()

        control_led_on.setOnClickListener { sendCommand("Repas") }
        control_led_off.setOnClickListener { sendCommand("Toilettes") }
        control_led_disconnect.setOnClickListener { sendCommand("disconnect"); disconnect() }
    }

    private fun connectionAttemptToDevice() {
        Thread(Runnable {
            this@BluetoothControl.runOnUiThread {
                myProgressBar.visibility = View.VISIBLE
            }

            try {
                if (!isConnected) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

                    val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(address)
                    socket = device.createRfcommSocketToServiceRecord(uuid)

                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()

                    socket.connect()
                    connectSuccess = true
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }

            this@BluetoothControl.runOnUiThread {
                if (!connectSuccess) {
                    Log.i("data", "Device isn't connect")
                } else {
                    Log.i("data", "Device is connect")
//                    val myWatch = MyWatch()
//                    myWatchManager.myWatches.add(myWatch)
//                    Log.i("BluetoothControl", "Nombre d'appareil: " + myWatchManager.myWatches.size)
//                    launchListWatchActivity()
                }
                myProgressBar.visibility = View.GONE
            }
        }).start()
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
            isConnected = false
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //TODO refresh list of watches
        finish()
    }

    private fun launchListWatchActivity() {
        val intent = Intent(applicationContext, ListWatches::class.java)
        startActivity(intent)
    }

}
