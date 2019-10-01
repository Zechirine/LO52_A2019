package com.example.projet

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_flic_app_not_installed.*

class FlicAppNotInstalled : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flic_app_not_installed)

        installFlicAppBtn.setOnClickListener{
            Log.d("eestet", "eestet")
            val appPackageName = "io.flic.app"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id="+appPackageName)
            startActivity(intent)
        }
    }
}
