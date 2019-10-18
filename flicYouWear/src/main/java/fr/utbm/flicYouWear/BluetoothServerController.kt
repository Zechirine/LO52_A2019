package fr.utbm.flicYouWear

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

class BluetoothServerController(private val activity: MainActivity) : Thread() {

    val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var cancelled: Boolean
    private val serverSocket: BluetoothServerSocket?

    init {
        val btAdapter = BluetoothAdapter.getDefaultAdapter()
        if (btAdapter != null) {
            this.serverSocket = btAdapter.listenUsingRfcommWithServiceRecord("test", uuid) // 1
            this.cancelled = false
        } else {
            this.serverSocket = null
            this.cancelled = true
        }

    }

    override fun run() {
        var socket: BluetoothSocket

        while(true) {
            Log.i("BLUETOOTHSERVERCONTOLLER", "LOOP")

            if (this.cancelled) {
                break
            }

            try {
                socket = serverSocket!!.accept()  // 2
            } catch(e: IOException) {
                break
            }

            if (!this.cancelled && socket != null) {
                Log.i("server", "Connecting")

                BluetoothServer(socket, activity).start() // 3
            }
        }
    }

    fun cancel() {
        this.cancelled = true
        this.serverSocket!!.close()
    }
}