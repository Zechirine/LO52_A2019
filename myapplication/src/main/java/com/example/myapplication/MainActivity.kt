package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()

        button.setOnClickListener {
            launchAddButtonActivity()
        }
    }

    private fun launchAddButtonActivity() {
        val intent = Intent(this, Main2Activity::class.java)
        startActivity(intent)
        finish()
    }
}
