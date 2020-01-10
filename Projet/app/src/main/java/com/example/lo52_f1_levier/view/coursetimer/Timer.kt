package com.example.lo52_f1_levier.view.coursetimer

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lo52_f1_levier.R
import kotlinx.android.synthetic.main.fragment_timer.*
import kotlin.math.max

class Timer : Fragment() {

    private var clockInitialized = false
    private var started = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startButton.setOnClickListener { startTimer() }
    }

    fun initClock(time: Long) {
        timer.base = SystemClock.elapsedRealtime() - max(time, 0L)
        clockInitialized = true
    }

    fun getTimeInMilliseconds(): Long {
        return SystemClock.elapsedRealtime() - timer.base
    }

    fun displayTimer() {
        startButton.visibility = View.GONE
        timer.visibility = View.VISIBLE
    }

    private fun startTimer() {
        displayTimer()

        if (!clockInitialized) {
            timer.base = SystemClock.elapsedRealtime()
            clockInitialized = true
        }

        timer.start()
        started = true
    }

    fun isStarted(): Boolean {
        return started
    }

    fun stop() {
        timer.stop()
    }
}
