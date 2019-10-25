package fr.utbm.lo52.flicYouWear

import android.bluetooth.BluetoothSocket
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class BluetoothServer(private val socket: BluetoothSocket, private val activity: MainActivity): Thread() {
    private val inputStream = this.socket.inputStream
    private var nbByteRead: Int = 0
    private lateinit var bytesMessageRead: ByteArray
    private lateinit var stringMessageRead: String

    override fun run() {
        while(true) {
            try {
                nbByteRead = inputStream.available()
                bytesMessageRead = ByteArray(nbByteRead)
                inputStream.read(bytesMessageRead, 0, nbByteRead)
                stringMessageRead = String(bytesMessageRead)


                if (stringMessageRead == "disconnect") {
                    inputStream.close()
                    socket.close()
                    this.interrupt()
                    break
                } else if (stringMessageRead != "") {
                    Log.i("BluetoothServer", "Message received:" + stringMessageRead)
                    activity.text.text = stringMessageRead
                }
            } catch (e: Exception) {
                Log.e("BluetoothServer", "Cannot read data", e)
            }
        }
    }
}