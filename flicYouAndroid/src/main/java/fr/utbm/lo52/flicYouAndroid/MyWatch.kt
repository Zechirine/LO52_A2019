package fr.utbm.lo52.flicYouAndroid

import android.bluetooth.BluetoothSocket

class MyWatch(val socket: BluetoothSocket) {

    private var myWatchSocket = socket

    fun getName(): String {
        return "TEST"
    }

    fun getBluetoothSocket(): BluetoothSocket {
        return myWatchSocket
    }
}

