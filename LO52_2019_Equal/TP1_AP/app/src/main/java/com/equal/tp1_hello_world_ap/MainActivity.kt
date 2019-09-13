package com.equal.tp1_hello_world_ap

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main. activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonToHelloWorld.setOnClickListener(){
            val intent = Intent(this, HelloActivity::class.java)
            startActivity(intent)
        }
    }
}
