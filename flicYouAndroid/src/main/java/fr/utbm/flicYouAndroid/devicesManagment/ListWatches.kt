package fr.utbm.flicYouAndroid.devicesManagment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lo52.projet.*
import fr.utbm.flicYouAndroid.BluetoothDevicesList
import fr.utbm.flicYouAndroid.MyFlicManager

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
