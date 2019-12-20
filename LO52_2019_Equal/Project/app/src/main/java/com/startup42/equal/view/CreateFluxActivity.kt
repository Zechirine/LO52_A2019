package com.startup42.equal.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.startup42.equal.R
import com.startup42.equal.model.CreateFluxResult
import com.startup42.equal.viewModel.CreateFluxViewModel

import kotlinx.android.synthetic.main.activity_create_flux.*

class CreateFluxActivity : AppCompatActivity() {

    val viewModel = CreateFluxViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_flux)


        //val wallet = intent.getParcelableExtra<>("wallet")
        //print(wallet)
    }
}
