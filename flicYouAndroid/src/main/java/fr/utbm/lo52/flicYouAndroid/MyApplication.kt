package fr.utbm.lo52.flicYouAndroid

import android.app.Application

class MyApplication : Application() {
    companion object{
        val myButtonManager : MyButtonManager = MyButtonManager()
    }

}