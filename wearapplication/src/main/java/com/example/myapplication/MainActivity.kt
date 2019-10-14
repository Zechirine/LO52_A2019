package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.wearable.activity.WearableActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : WearableActivity() {

    @SuppressLint("ParcelCreator")
    class Task(val typeOfTask: String?, val roomNumber: Number, var status: Boolean): Parcelable {

        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readBoolean()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(typeOfTask)
            parcel.writeValue(roomNumber)
            parcel.writeBoolean(status)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Task> {
            override fun createFromParcel(parcel: Parcel): Task {
                return Task(parcel)
            }

            override fun newArray(size: Int): Array<Task?> {
                return arrayOfNulls(size)
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val parcel = Task("Manger", 4, false)
        text.text = ""
        // Enables Always-on
        setAmbientEnabled()
        pauseButton.setOnClickListener {
            launchPauseActivity()
        }
        launchTaskButton.setOnClickListener {
            if (text.text !== "") {
                launchTaskActivity(parcel)
            } else {
                // TODO log "no activity"
            }
        }
        simulTaskButton.setOnClickListener {
            text.text = "Chambre ${parcel.roomNumber} \n ${parcel.typeOfTask}"
        }
        urgenceButton.setOnClickListener {
            launchUrgenceActivity()
        }

        BluetoothServerController(this).start()
    }

    private fun launchUrgenceActivity() {
        val intent = Intent(this, UrgenceActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchPauseActivity() {
        val intent = Intent(this, PauseActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchTaskActivity(parcel: Task) {
        if (text.text != "") {
            val intent = Intent(this, CurrentTaskActivity::class.java)

            intent.putExtra("task", TaskModel("Manger", 4, false))

            startActivity(intent)
            //finish()
        }
    }
}
