package com.example.wearapplication

import android.os.Bundle
import android.support.wearable.activity.WearableActivity

class CurrentTask : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_task)
    }
}
