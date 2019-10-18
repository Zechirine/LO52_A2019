package fr.utbm.flicYouAndroid

import android.app.Application

class MyApplication : Application() {
    companion object{
        val myButtonManager : MyButtonManager = MyButtonManager()
    }

}