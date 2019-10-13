package com.startup42.equal

import android.app.Application
import android.content.Context

class Equal: Application() {

    companion object {
        var application: Application? = null
        val context: Context
            get() {
                return application?.applicationContext!!
            }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}