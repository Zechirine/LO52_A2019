package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.wearable.activity.WearableActivity
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : WearableActivity() {

    @SuppressLint("ParcelCreator")

    private val waitingTaskText: String = "En attente de tâches..."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val task = TaskModel("Manger", 4, false)
        text.text = waitingTaskText
        // Enables Always-on
        setAmbientEnabled()
        pauseButton.setOnClickListener {
            launchPauseActivity()
        }
        launchTaskButton.setOnClickListener {
            if (text.text !== waitingTaskText) {
                launchTaskActivity(task)
            } else {
                // TODO log "no activity"
            }
        }
        simulTaskButton.setOnClickListener {
            text.text = "Chambre ${task.roomNumber} \n ${task.typeOfTask}"
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

    private fun launchTaskActivity(task: TaskModel) {
        if (text.text != waitingTaskText) {
            val intent = Intent(this, CurrentTaskActivity::class.java)

            intent.putExtra("task", task)

            startActivity(intent)
            //finish()
        }
    }
}
