package com.example.wearapplication

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : WearableActivity() {
    lateinit var mText: TextView
    private val btnOk: Button = findViewById(R.id.btn_ok)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mText = findViewById(R.id.text)
        btnOk.setOnClickListener {
            Toast.makeText(this@MainActivity, "You clicked", Toast.LENGTH_SHORT).show()
        }

        // Enables Always-on
        setAmbientEnabled()
    }
}
