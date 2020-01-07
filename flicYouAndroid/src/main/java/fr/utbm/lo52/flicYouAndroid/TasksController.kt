package fr.utbm.lo52.flicYouAndroid

import android.app.IntentService
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.util.Log
import fr.utbm.lo52.flicYouAndroid.models.StateTask
import java.io.IOException

class TasksController : IntentService("TasksController") {

    private companion object {
        lateinit var socket: BluetoothSocket
    }

    private val myTaskManager: MyTaskManager = MyApplication.myTaskManager
    private val myWatchManager: MyWatchManager = MyApplication.myWatchManager

    override fun onHandleIntent(intent: Intent) {
        var nbCurrentTasks = myTaskManager.myTasks.count()

        while(true) {
            if (nbCurrentTasks != myTaskManager.myTasks.count()) {
                nbCurrentTasks = myTaskManager.myTasks.count()
                sendTasksList()
            }

            if (myWatchManager.myWatches.count() != 0) {
                myWatchManager.myWatches.forEach { watch ->
                    if (watch.isAvailable()) {
                        if(myTaskManager.myTasks.count() != 0) {
                            myTaskManager.myTasks.forEach { task ->
                                if(task.getState() == StateTask.PENDING){
                                    try {
                                        socket = watch.getBluetoothSocket()
                                        sendCommand(task.getName())
                                        watch.setAvailable(false)
                                    } catch (e: Exception) {
                                        Log.i("Error", "Watch not connected")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun sendCommand(input: String) {
        try {
            socket.outputStream.write(input.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun sendTasksList() {
        val intent = Intent()
        intent.action = "fr.utbm.lo52.flicYouAndroid"
        sendBroadcast(intent)
    }
}