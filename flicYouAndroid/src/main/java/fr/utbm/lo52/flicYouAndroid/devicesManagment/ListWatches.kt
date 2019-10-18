package fr.utbm.lo52.flicYouAndroid.devicesManagment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import fr.utbm.lo52.flicYouAndroid.*
import fr.utbm.lo52.flicYouAndroid.BluetoothDevicesList
import fr.utbm.lo52.flicYouAndroid.MyFlicManager

class ListWatches : AppCompatActivity() {
    private val TAG = "ListWatches"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_watches)

        MyFlicManager.setFlicCredentials()
    }

    fun launchBluetoothDevicesListActivity(view : View){
        val intent = Intent(this, BluetoothDevicesList::class.java)
        startActivity(intent)
    }
}
