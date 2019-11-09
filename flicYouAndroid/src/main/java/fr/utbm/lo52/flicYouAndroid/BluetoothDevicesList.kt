package fr.utbm.lo52.flicYouAndroid

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import fr.utbm.lo52.flicYouAndroid.devicesManagment.ListWatches
import kotlinx.android.synthetic.main.activity_bluetooth_devices_list.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class BluetoothDevicesList : AppCompatActivity() {

    private var m_bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var m_pairedDevices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1

    private val myWatchManager: MyWatchManager =
        MyApplication.myWatchManager

    companion object {
        val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var isConnected: Boolean = false
        var connectSuccess: Boolean = true

        lateinit var bluetoothAdapter: BluetoothAdapter
        lateinit var socket: BluetoothSocket

        val EXTRA_ADDRESS: String = "Device_address"
    }

    lateinit var address: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_devices_list)

        checkBluetoothState()
        select_device_refresh.setOnClickListener{ pairedDeviceList() }
        pairedDeviceList()
    }

    private fun checkBluetoothState() {
        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(m_bluetoothAdapter == null){
            Toast.makeText(applicationContext,"this device doesn't support bluetooth", Toast.LENGTH_SHORT).show()
            return
        }

        if(!m_bluetoothAdapter!!.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }
    }

    private fun pairedDeviceList() {
        m_pairedDevices = m_bluetoothAdapter!!.bondedDevices
        val devices : ArrayList<BluetoothDevice> = ArrayList()
        val devicesName: ArrayList<String> = ArrayList()

        if(!m_pairedDevices.isEmpty()) {
            for (device: BluetoothDevice in m_pairedDevices) {
                devices.add(device)
                devicesName.add(device.name)
                Log.i("device", device.toString())
            }
        } else {
            Toast.makeText(applicationContext,"No paired bluetooth devices found", Toast.LENGTH_SHORT).show()
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, devicesName)
        Log.i("adapter", ""+adapter)
        select_device_list.adapter = adapter
        select_device_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            address = devices[position].address

            val intent = Intent(this, BluetoothDevicesList::class.java)
//            startActivity(intent)
            connectionAttemptToDevice()
        }
    }

    private fun connectionAttemptToDevice() {
        Thread(Runnable {
            this@BluetoothDevicesList.runOnUiThread {
                myProgressBar2.visibility = View.VISIBLE
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

            this@BluetoothDevicesList.runOnUiThread {
                if (!connectSuccess) {
                    Log.i("data", "Device isn't connect")
                } else {
                    Log.i("data", "Device is connect")
                    val myWatch = MyWatch(socket)
                    myWatchManager.myWatches.add(myWatch)
                    intent.putExtra(EXTRA_ADDRESS, myWatchManager.myWatches.lastIndexOf(myWatch))
                    launchListWatchActivity()
                }
                myProgressBar2.visibility = View.GONE
            }
        }).start()
    }

    private fun launchListWatchActivity() {
        val intent = Intent(applicationContext, ListWatches::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (m_bluetoothAdapter!!.isEnabled) {
                    Toast.makeText(applicationContext,"Bluetooth has been enabled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext,"Bluetooth has been disabled", Toast.LENGTH_SHORT).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(applicationContext,"Bluetooth enabling has been canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
