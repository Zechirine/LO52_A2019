package com.example.lo52_f1_levier.view.coursetimer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.lo52_f1_levier.R
import com.example.lo52_f1_levier.utils.GridAutofitLayoutManager
import com.example.lo52_f1_levier.view.DetailsCourseTimerFragment


class CourseTimerActivity : AppCompatActivity() {

    private lateinit var teamBoxGrid: RecyclerView
    private lateinit var timerFragment: Timer
    private var courseId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_timer)

        courseId = intent.getIntExtra("courseId", 0)

        // get timer fragment
        timerFragment = supportFragmentManager.findFragmentById(R.id.timerFgmt) as Timer

        // setup team box grid
        val columnWidth = resources.getDimension(R.dimen.teambox_width) + resources.getDimension(R.dimen.teambox_margin_side)
        teamBoxGrid = findViewById(R.id.teamBoxes)
        teamBoxGrid.layoutManager = GridAutofitLayoutManager(this, columnWidth.toInt())
        teamBoxGrid.adapter = TeamBoxGridAdapter(
            this,
            courseId,
            this::redirectToDetails,
            timerFragment::getTimeInMilliseconds,
            timerFragment::isStarted,
            this::onCourseInitialized,
            timerFragment::stop
        )
        teamBoxGrid.setHasFixedSize(true)
    }

    private fun redirectToDetails(teamNumber: Int): Boolean {
        val intent = Intent(this, DetailsCourseTimerFragment::class.java)
        intent.putExtra("courseId", courseId)
        intent.putExtra("teamId",teamNumber)
        startActivity(intent)
        return true
    }

    private fun onCourseInitialized(totalTime: Long, isOver: Boolean) {
        timerFragment.initClock(totalTime)
        if (isOver) {
            timerFragment.displayTimer()
        }
    }
}
