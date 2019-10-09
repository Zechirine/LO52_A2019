package com.example.projet.devicesManagment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.projet.*

class ListButtons : AppCompatActivity() {
    private val TAG = "ListButtons"
    private val myButtonManager: MyButtonManager =
        MyApplication.myButtonManager
    private lateinit var buttons: ListView
    private lateinit var addButton: Button
    private lateinit var ejectButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_buttons)

        buttons = findViewById(R.id.buttons)
        addButton = findViewById(R.id.addButton)

        MyFlicManager.setFlicCredentials()

        buttons.adapter =
            MyButtonsListViewAdapter(this, myButtonManager.myButtons)

        setListeners()
    }

    private fun setListeners() {
        addButton.setOnClickListener {
            launchAddButtonActivity()
        }
        ejectButton.setOnClickListener {
            // TODO associate ID to eject button or row
            // get it
            // pass it to remove function
            // remove corresponding myButton
            // unlink Flic Button from app
//            val myButton = ...
//            myButtonManager.myButtons.remove(myButton)
        }
    }

    private fun launchAddButtonActivity() {
        val intent = Intent(this, AddButton::class.java)
        startActivity(intent)
        finish()
    }

}
