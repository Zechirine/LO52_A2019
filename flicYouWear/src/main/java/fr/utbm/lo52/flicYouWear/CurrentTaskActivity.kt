package fr.utbm.lo52.flicYouWear

import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import fr.utbm.lo52.flicYouWear.R
import kotlinx.android.synthetic.main.activity_current_task.*

class CurrentTaskActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_task)

        var data = intent.extras
        var task = data?.getParcelable<TaskModel>("task")
        text.text = "Chambre ${task?.roomNumber} \n ${task?.typeOfTask}"
        // val bundle = intent.getBundleExtra("Bundle")
        // val task = bundle.getParcelable<MainActivity.Task>("key")
        // text.text = "Chambre ${task?.roomNumber} \n ${task?.typeOfTask}"
        cancelButton.setOnClickListener {
            returnToMainActivity(task)
        }
        doneButton.setOnClickListener {
            task?.status = true
            returnToMainActivity(task)
        }
    }

    private fun returnToMainActivity(task: TaskModel?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("task", task)
        startActivity(intent)
        finish()
    }
}
