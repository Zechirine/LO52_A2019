package fr.utbm.lo52.flicYouAndroid

import android.app.Application

class MyApplication : Application() {
    companion object{
        val myButtonManager : MyButtonManager = MyButtonManager()
        val myWatchManager : MyWatchManager = MyWatchManager()
        val myTaskManager : MyTaskManager = MyTaskManager()
    }

}