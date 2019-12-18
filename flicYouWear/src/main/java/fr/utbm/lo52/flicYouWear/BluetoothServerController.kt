package fr.utbm.lo52.flicYouWear

import android.app.IntentService
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.util.*

class BluetoothServerController() : IntentService("BluetoothServerController") {
    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val btAdapter = BluetoothAdapter.getDefaultAdapter()
    private var serverSocket: BluetoothServerSocket? = null
    private var cancelled: Boolean = true
    private lateinit var socket: BluetoothSocket

    private var nbByteRead: Int = 0
    private lateinit var inputStream: InputStream
    private lateinit var bytesMessageRead: ByteArray
    private lateinit var stringMessageRead: String

    override fun onCreate() {
        super.onCreate()
        if (btAdapter != null) {
            try {
                serverSocket = btAdapter.listenUsingRfcommWithServiceRecord("bluetoothConnectionListener", uuid)
                cancelled = false
            } catch (e: IOException) {
                e.printStackTrace()
                serverSocket = null
                cancelled = true
            }
        }
    }

    override fun onHandleIntent(intent: Intent) {
        while(true) {
            if (cancelled) break

            try {
                socket = serverSocket!!.accept()
            } catch(e: IOException) {
                break
            }

            while(true) {
                try {
                    inputStream = this.socket.inputStream
                    nbByteRead = inputStream.available()
                    bytesMessageRead = ByteArray(nbByteRead)
                    inputStream.read(bytesMessageRead, 0, nbByteRead)
                    stringMessageRead = String(bytesMessageRead)

                    if (stringMessageRead == "disconnect") {
                        inputStream.close()
                        socket.close()
                        break
                    } else if (stringMessageRead != "") {
                        sendTask(stringMessageRead)
                    }
                } catch (e: Exception) {
                    Log.e("BluetoothServer", "Cannot read data", e)
                }
            }
        }
    }

    private fun sendTask(message: String) {
        val intent = Intent()
        intent.action = "com.example.andy.myapplication"
        intent.putExtra("TASK", message)
        sendBroadcast(intent)
    }
}