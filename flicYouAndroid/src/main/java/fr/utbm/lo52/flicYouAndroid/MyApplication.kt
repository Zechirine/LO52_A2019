package fr.utbm.lo52.flicYouAndroid

import android.app.Application
import com.example.projet.MyWatchManager

class MyApplication : Application() {
    companion object{
        val myButtonManager : MyButtonManager = MyButtonManager()
        val myWatchManager : MyWatchManager = MyWatchManager()
    }

}