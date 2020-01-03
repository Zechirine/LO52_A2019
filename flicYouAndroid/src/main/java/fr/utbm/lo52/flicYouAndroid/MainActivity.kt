package fr.utbm.lo52.flicYouAndroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import fr.utbm.lo52.flicYouAndroid.R
import fr.utbm.lo52.flicYouAndroid.devicesManagment.ListButtons
import fr.utbm.lo52.flicYouAndroid.devicesManagment.ListWatches
import fr.utbm.lo52.flicYouAndroid.models.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val myButtonManager: MyButtonManager =
        MyApplication.myButtonManager
    private lateinit var buttons: ListView
    private lateinit var addButton: Button
    //for zoneDao
    private var daoZone = ZoneDao()
    private var daoCaregiver = CaregiverDao()
    private var daoMessage = MessageDao()
    private var daoRoom = RoomDao()
    private var daoHave = HaveDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyFlicManager.setFlicCredentials()

//        setListeners()
        //to initiate Model's Data
        //initDatabase()

//        daoZone.removeAll()
//        Log.i("DATA", "Zones supprimées")
//
//        daoCaregiver.removeAll()
//        Log.i("DATA", "caregivers supprimés")
//
//        daoHave.removeAll()
//        Log.i("DATA", "Relation Have is deleted")
//
//        val haves =  daoHave.queryForAll()
//        if(haves.size == 0){
//            Log.i("DATA", "no relation Have")
//            return
//        }
//        else{
//            for(have in haves){
//                println(haves.toString())
//            }
//        }


        //List all zone
        val zones =  daoZone.queryForAll()
        if(zones.size == 0){
            Log.i("DATA", "Pas de zone")
            return;
        }else{
            for(zone in zones){
                println(zone.toString())
            }
        }

        //List all caregiver
        val caregivers =  daoCaregiver.queryForAll()
        if(caregivers.size == 0){
            Log.i("DATA", "Pas de caregiver")
            return;
        }else{
            for(caregiver in caregivers){
                println(caregiver.toString())
            }
        }


//        for(message in daoMessage.queryforAll()){
//            Log.i("DATA", " le message est :" + message.content)
//        }
//
//        for(caregiver in daoCaregiver.queryForAll()){
//            Log.i("DATA", "l' aide soignant est  :" + caregiver.name)
//        }
//
//        for(room in daoRoom.queryForAll()){
//            Log.i("DATA", "le nom du malade est :" + room.nameSufferer + "\n le numéro de sa chambre est : " + room.number)
//        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.buttons_managment -> {
                launchListButtonActivity()
                true
            }
            R.id.watches_managment -> {
                launchListWatchesActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun launchListButtonActivity() {
        val intent = Intent(this, ListButtons::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchListWatchesActivity() {
        val intent = Intent(this, ListWatches::class.java)
        startActivity(intent)
        finish()
    }

    //to destroy instance of database and close it
    override fun onDestroy() {
        super.onDestroy()
        DatabaseManager.close()
    }

    private fun initDatabase(){
        //for zone
        var zone : Zone = Zone(3)
        var zone2 : Zone = Zone(2)
        daoZone.add(zone)
        daoZone.add(zone2)

        //for message
        var message: Message = Message("Manger")
        var message2: Message = Message("Boire")
        daoMessage.add(message)
        daoMessage.add(message2)

        //for Caregiver
        var caregiver : Caregiver =  Caregiver(1, "DONTIO")
        var caregiver2 : Caregiver =  Caregiver(2, "SNUFFY")
        daoCaregiver.add(caregiver2)
        daoCaregiver.add(caregiver)

        //for Room
        var room :  Room = Room(1, "Schwartz", "lksjfljf", zone, caregiver)
        var room2 :  Room = Room(1, "Puyol", "lksjfljffgdfg", zone2, caregiver2)
        daoRoom.add(room2)
        daoRoom.add(room)

        //for have
        var have = Have(room, message)
        var have2 = Have(room2, message2)
        var have3 = Have(room, message2)
        var have4 = Have(room2, message2)
        daoHave.add(have)
        daoHave.add(have2)
        daoHave.add(have3)
        daoHave.add(have4)
    }
}
