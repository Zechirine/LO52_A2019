package fr.utbm.flicYouAndroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import com.lo52.projet.R
import fr.utbm.flicYouAndroid.devicesManagment.ListButtons
import fr.utbm.flicYouAndroid.devicesManagment.ListWatches

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val myButtonManager: MyButtonManager =
        MyApplication.myButtonManager
    private lateinit var buttons: ListView
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyFlicManager.setFlicCredentials()

//        setListeners()
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

}
