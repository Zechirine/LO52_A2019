package com.example.tp1_ab.MainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.tp1_ab.SecondActivity.SecondActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.tp1_ab.R.layout.activity_main)


        // get reference to button
        val btn_click_me = findViewById(com.example.tp1_ab.R.id.buttonHelloWorld) as Button
        // set on-click listener
        btn_click_me.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            // start your next activity
            startActivity(intent)
        }

    }



}