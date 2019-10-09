package com.example.projet.devicesManagment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projet.*

class ListWatches : AppCompatActivity() {
    private val TAG = "ListWatches"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_watches)

        MyFlicManager.setFlicCredentials()

        setListeners()
    }

    private fun setListeners() {

    }

}
