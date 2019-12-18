package fr.utbm.lo52.flicYouWear

import android.app.IntentService
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class BluetoothServer(private val socket: BluetoothSocket): Thread() {


    override fun run() {
    }
}