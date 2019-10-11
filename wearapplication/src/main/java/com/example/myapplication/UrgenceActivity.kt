package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import kotlinx.android.synthetic.main.activity_pause.*

class UrgenceActivity: WearableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urgence)

        stopPauseButton.setOnClickListener {
            returnToMainActivity()
        }
    }

    private fun returnToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}