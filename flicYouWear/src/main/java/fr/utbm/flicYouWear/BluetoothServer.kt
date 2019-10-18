package fr.utbm.flicYouWear

import android.bluetooth.BluetoothSocket
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class BluetoothServer(private val socket: BluetoothSocket, private val activity: MainActivity): Thread() {
    private val inputStream = this.socket.inputStream

    override fun run() {
        Log.i("BLUETOOTHSERVER", "LOOP")

        while(true) {
            try {
                val available = inputStream.available()
                val bytes = ByteArray(available)
                inputStream.read(bytes, 0, available)
                val text = String(bytes)
                if(text != "") {
                    Log.i("server", "Message received:" + text)
                    activity.text.setText(text)
                }
            } catch (e: Exception) {
                Log.e("client", "Cannot read data", e)
            }
        }
    }
}