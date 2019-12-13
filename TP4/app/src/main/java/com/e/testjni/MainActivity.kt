package com.e.testjni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Attach listeners
        up.setOnClickListener({
            direction.text = ToGerman("Up")
        })
        down.setOnClickListener({
            direction.text = ToGerman("Down")
        })
        left.setOnClickListener({
            direction.text = ToGerman("Left")
        })
        right.setOnClickListener({
            direction.text = ToGerman("Right")
        })

        read.setOnClickListener({
            readLabelOut.text = READ()
        })
       write.setOnClickListener({
            readLabelOut.text = WRITE()
        })
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun READ(): String
    external fun WRITE(): String
    external fun ToGerman(text: String): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
