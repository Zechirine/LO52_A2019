package com.example.projet

import android.app.Application

class MyApplication : Application() {
    companion object{
        val myButtonManager : MyButtonManager = MyButtonManager()
        val myWatchManager : MyWatchManager = MyWatchManager()
    }

}