package com.example.projet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val myButtonManager: MyButtonManager = MyApplication.myButtonManager
    private lateinit var buttons: ListView
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttons = findViewById(R.id.buttons)
        addButton = findViewById(R.id.addButton)

        MyFlicManager.setFlicCredentials()

        buttons.adapter = MyButtonsListViewAdapter(this, myButtonManager.myButtons)

        setListeners()
    }

    private fun setListeners() {
        addButton.setOnClickListener {
            launchAddButtonActivity(myButtonManager)
        }
    }

    private fun launchAddButtonActivity(myButtonManager: MyButtonManager) {
        val intent = Intent(this, AddButton::class.java)
        startActivity(intent)
        finish()
    }

}
