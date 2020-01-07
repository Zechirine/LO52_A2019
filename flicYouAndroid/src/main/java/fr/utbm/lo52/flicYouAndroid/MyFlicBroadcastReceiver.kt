package fr.utbm.lo52.flicYouAndroid

import android.content.Context
import android.util.Log
import fr.utbm.lo52.flicYouAndroid.models.StateTask
import io.flic.lib.FlicBroadcastReceiver
import io.flic.lib.FlicButton

class MyFlicBroadcastReceiver : FlicBroadcastReceiver() {

    private val myTaskManager: MyTaskManager =
        MyApplication.myTaskManager

    override fun onRequestAppCredentials(context: Context) {
        MyFlicManager.setFlicCredentials()
    }

    override fun onButtonSingleOrDoubleClickOrHold(
        context: Context,
        button: FlicButton,
        wasQueued: Boolean,
        timeDiff: Int,
        isSingleClick: Boolean,
        isDoubleClick: Boolean,
        isHold: Boolean
    ) {
        var nameTask = ""

        if (isSingleClick)
            nameTask = "Repas"
        if (isDoubleClick)
            nameTask = "Toilettes"
        if (isHold)
            nameTask = "Santé"

        val myTask = MyTask(nameTask, StateTask.PENDING)
        myTaskManager.myTasks.add(myTask)
    }

    override fun onButtonRemoved(context: Context, button: FlicButton) {
        Log.i("MainActivity", "Button was removed")
    }
}
