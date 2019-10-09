package com.example.projet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import com.example.projet.devicesManagment.ListButtons
import com.example.projet.devicesManagment.ListWatches

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val myButtonManager: MyButtonManager = MyApplication.myButtonManager
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
                // TODO
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
