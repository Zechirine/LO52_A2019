package fr.utbm.lo52.flicYouAndroid.devicesManagment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.projet.*
import kotlinx.android.synthetic.main.activity_bluetooth_devices_list.*
import kotlinx.android.synthetic.main.activity_list_watches.*
import kotlinx.android.synthetic.main.button_row.*
import android.view.View
import fr.utbm.lo52.flicYouAndroid.*
import fr.utbm.lo52.flicYouAndroid.BluetoothDevicesList
import fr.utbm.lo52.flicYouAndroid.MyFlicManager

class ListWatches : AppCompatActivity() {
    private val TAG = "ListWatches"
    private val myWatchManager: MyWatchManager =
        MyApplication.myWatchManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_watches)

        Log.i("ListWatches", "Nombre d'appareil: " + myWatchManager.myWatches.size)
        watches.adapter = MyWatchesListViewAdapter(this, myWatchManager.myWatches)

        setListeners()
    }

    private fun setListeners() {
        addWatch.setOnClickListener {
            launchBluetoothDevicesListActivity()
        }

        watches.adapter
        watches.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val watch = myWatchManager.myWatches.get(position)
            myWatchManager.myWatches.remove(watch)
        }
    }

    fun launchBluetoothDevicesListActivity(){
        val intent = Intent(this, BluetoothDevicesList::class.java)
        startActivity(intent)
    }
}
