package com.example.myapplication

import android.bluetooth.BluetoothSocket
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class BluetoothServer(private val socket: BluetoothSocket, private val activity: MainActivity): Thread() {
    private val inputStream = this.socket.inputStream

    override fun run() {
        while(true) {
            try {
                val available = inputStream.available()
                val bytes = ByteArray(available)
                inputStream.read(bytes, 0, available)
                val text = String(bytes)

                if (text == "disconnect") {
                    inputStream.close()
                    socket.close()
                    this.interrupt()
                    break
                } else if (text != "") {
                    Log.i("server", "Message received:" + text)
                    activity.task.text = text
                }
            } catch (e: Exception) {
                Log.e("BluetoothServer", "Cannot read data", e)
            }
        }
    }
}