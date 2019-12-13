package com.startup42.tp4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private external fun readJNI(): Int
    private external fun writeJNI(): Int
    private external fun directionJNI(direction: String): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        readButton.setOnClickListener {
            val a = readJNI()
            textView.text = "Read : ${a}"
        }

        writeButton.setOnClickListener {
            val a = writeJNI()
            textView.text = "Write : ${a}"
        }

        upButton.setOnClickListener {
            textView.text = directionJNI(upButton.text.toString())
        }

        downButton.setOnClickListener {
            textView.text = directionJNI(downButton.text.toString())
        }

        leftButton.setOnClickListener {
            textView.text = directionJNI(leftButton.text.toString())
        }

        rightButton.setOnClickListener {
            textView.text = directionJNI(rightButton.text.toString())
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
