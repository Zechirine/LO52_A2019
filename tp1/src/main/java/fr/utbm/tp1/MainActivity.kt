package fr.utbm.tp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lo52.tp1.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onHelloWorldButtonClick(view: View){
        val intent = Intent(this, HelloWorldActivity::class.java)
        startActivity(intent)
    }
}
