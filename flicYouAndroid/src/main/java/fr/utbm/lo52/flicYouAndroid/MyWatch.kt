package fr.utbm.lo52.flicYouAndroid

import android.bluetooth.BluetoothSocket

class MyWatch(socket: BluetoothSocket) {

    private var myWatchSocket = socket
    private var available = true

    fun getName(): String {
        return "TEST"
    }

    fun getBluetoothSocket(): BluetoothSocket {
        return myWatchSocket
    }

    fun isAvailable(): Boolean{
        return available
    }

    fun setAvailable(_available: Boolean) {
        available = _available
    }
}

