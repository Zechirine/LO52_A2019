package com.startup42.equal.view

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.startup42.equal.R
import com.startup42.equal.model.HomeResponse
import com.startup42.equal.viewModel.HomeViewModel

class HomeActivity : AppCompatActivity() {

    val viewModel = HomeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val prefs = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val userId = prefs.getString("userId", "errorid")
/*
        viewModel.receiveData(userId)
            .doOnNext {
                //TODO do some stuff
            }
            .doOnError { it.printStackTrace() }
            .doOnComplete{ println("onComplete!") }
            .subscribe()*/
    }
}
