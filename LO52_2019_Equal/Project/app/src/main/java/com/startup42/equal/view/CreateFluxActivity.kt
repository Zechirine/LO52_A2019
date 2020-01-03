package com.startup42.equal.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.startup42.equal.R
import com.startup42.equal.viewModel.CreateFluxViewModel

import kotlinx.android.synthetic.main.activity_create_flux.*

class CreateFluxActivity : AppCompatActivity() {

    val viewModel = CreateFluxViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_flux)

        segmentedButton.setTintColor(getResources().getColor(R.color.mainColor))

        //val wallet = intent.getParcelableExtra<>("wallet")
        //print(wallet)
    }
}
