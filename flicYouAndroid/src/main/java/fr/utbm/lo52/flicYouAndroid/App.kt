package fr.utbm.lo52.flicYouAndroid

import android.app.Application

class App : Application() {

    companion object{
        lateinit var  instance : App
    }

    init {
        instance = this
    }
}