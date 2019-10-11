package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity() {

    private val simulatedTask: String = "Chambre 4 \n Manger"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()
        pauseButton.setOnClickListener {
            launchPauseActivity()
        }
        button.setOnClickListener {
            launchAddButtonActivity()
        }
        simulTaskButton.setOnClickListener {
            text.text = simulatedTask
        }
        urgenceButton.setOnClickListener {
            launchUrgenceActivity()
        }

    }

    private fun launchUrgenceActivity() {
        val intent = Intent(this, UrgenceActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchPauseActivity() {
        val intent = Intent(this, PauseActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchAddButtonActivity() {
        val intent = Intent(this, CurrentTaskActivity::class.java)
        startActivity(intent)
        finish()
    }
}
