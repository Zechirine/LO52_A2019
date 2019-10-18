package fr.utbm.flicYouWear

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import com.lo52.flicYouWear.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : WearableActivity() {

    @SuppressLint("ParcelCreator")

    private val waitingTaskText: String = "En attente de tâches..."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var task = TaskModel("Manger", 4, false)
        text.text = waitingTaskText

        var data = intent.extras
        if (data?.getParcelable<TaskModel>("task") !== null) {
            task = data.getParcelable<TaskModel?>("task") as TaskModel
            if (!task.status) {
                text.text = "Chambre ${task.roomNumber} \n ${task.typeOfTask}"
            } else {
                text.text = "Tâche terminée"
            }
        } else {
            TaskModel("Manger", 4, false)
        }

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
