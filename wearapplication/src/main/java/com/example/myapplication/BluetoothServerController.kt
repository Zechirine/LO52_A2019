package com.example.myapplication

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

class BluetoothServerController(private val activity: MainActivity) : Thread() {
    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val btAdapter = BluetoothAdapter.getDefaultAdapter()
    private var serverSocket: BluetoothServerSocket? = null
    private var cancelled: Boolean = true
    private lateinit var socket: BluetoothSocket

    init {
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

    override fun run() {
        while(true) {
            if (cancelled) break

            try {
                socket = serverSocket!!.accept()
            } catch(e: IOException) {
                break
            }

            Log.i("server", "Connecting")
            BluetoothServer(socket, activity).start()
        }
    }
}