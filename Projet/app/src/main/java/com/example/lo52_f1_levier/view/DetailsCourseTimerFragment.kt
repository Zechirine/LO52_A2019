package com.example.lo52_f1_levier.view

import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.lo52_f1_levier.DAO.CoureurDao
import com.example.lo52_f1_levier.DAO.ParticipeDao
import com.example.lo52_f1_levier.R
import com.example.lo52_f1_levier.model.Coureur
import com.example.lo52_f1_levier.model.Participe
import com.example.lo52_f1_levier.model.Runner
import kotlinx.android.synthetic.main.activity_details_course_timer.*
import kotlinx.android.synthetic.main.fragment_progression.view.*


class DetailsCourseTimerFragment : AppCompatActivity() {

    private var courseId: Int = -1
    private var teamId: Int = -1
    private lateinit var participeDao: ParticipeDao
    private lateinit var runnerDao : CoureurDao

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_course_timer)

        courseId = intent.getIntExtra("courseId",0)
        teamId = intent.getIntExtra("teamId", 0)

        participeDao = ParticipeDao(this)
        runnerDao = CoureurDao(this)

       init()

    }

    /**
     * Display what has to be displayed relatively to the number of runners for a team
     * */
    private fun init()
    {
        // Get the different runners

        // The cursor to iterate through the DB
        val cursor = participeDao.getParticipeByC_ID_E_ID(courseId,teamId)

        // The list of the runners' ID
        val runnersId = ArrayList<Int>()

        // The list contanining the runners
        val runners = ArrayList<Runner>()

        // Let's iterate through the team's runners
        with(cursor!!){
            // While we still have something in our cursor
            while (moveToNext())
            {
                // We get the Id of the runner
                val runId = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                runnersId.add(runId)
            }
        }
        // We close the cursor
        cursor.close()

        runnersId.sort()
        // Now we once again iterate
        // We now get the runner from the runner table
        runnersId.forEach{
            val runCursor = runnerDao.getCoureurByID(it)
            with(runCursor!!){
                while(runCursor!!.moveToNext())
                {
                    // We create an instance of a runner
                    val runner = Runner(it,
                        getInt(getColumnIndex(Coureur.CoureurTable.NUMC)),
                        getString(getColumnIndex(Coureur.CoureurTable.CNAME)),
                        getString(getColumnIndex(Coureur.CoureurTable.SURNAME)))
                    // We add the runner to our list
                    runners.add(runner)
                }
            }
            runCursor?.close()
        }


        // for each of them, create a new "timer"
        runners.forEach{
            var view = LayoutInflater.from(this).inflate(R.layout.fragment_progression,null,false)


            // L'utilisateur ne pourra pas faire progresser la seek bar
            view.seekBarPremier.setOnTouchListener { v, event -> true }
            view.seekBarDeuxieme.setOnTouchListener { v, event -> true }


            verticalLayout.addView(view)
            val cRunner = participeDao.getParticipeByRunnerIdRunId(it.id,courseId)
            var t1 ="" // Sprint 1
            var t2 ="" // Obstacle 1
            var t3 ="" // PitStop
            var t4 ="" // Sprint 2
            var t5 ="" // Obstacle 2
            var t6 ="" // Sprint 1 2ème passage
            var t7 ="" // Obstacle 1 2ème passage
            var t8 ="" // PitStop 2ème passage
            var t9 ="" // Sprint 2 2ème passage
            var t10 ="" // Obstacle 2 2ème passage

            view.runnersInfos.text = "${it.cname} ${it.surname}"
            with(cRunner!!){
                while(moveToNext())
                {
                    t1 = getString(getColumnIndex(Participe.ParticipeTable.TIME1))
                    t2 = getString(getColumnIndex(Participe.ParticipeTable.TIME2))
                    t3 = getString(getColumnIndex(Participe.ParticipeTable.TIME3))
                    t4 = getString(getColumnIndex(Participe.ParticipeTable.TIME4))
                    t5 = getString(getColumnIndex(Participe.ParticipeTable.TIME5))
                    t6 = getString(getColumnIndex(Participe.ParticipeTable.TIME6))
                    t7 = getString(getColumnIndex(Participe.ParticipeTable.TIME7))
                    t8 = getString(getColumnIndex(Participe.ParticipeTable.TIME8))
                    t9 = getString(getColumnIndex(Participe.ParticipeTable.TIME9))
                    t10 = getString(getColumnIndex(Participe.ParticipeTable.TIME10))

                }
            }

            // set the progression according to the times

            // First sprint of the first run
            if(t1 != "-1" )
            {
                view.seekBarPremier.progress = 1
                view.SprintPremier1.text=getTime(t1)
            }
            else
            {
                view.SprintPremier1.text = "Sprint"
            }

            // First Obstacle of the first run
            if(t2 != "-1" )
            {
                view.seekBarPremier.progress = 2
                view.ObstaclePremier1.text=getTime(t2)
            }
            else
            {
                view.ObstaclePremier1.text = "Obstacle"
            }

            // Pit stop of the first run
            if(t3 != "-1" )
            {
                view.seekBarPremier.progress = 3
                view.PitStopPremier.text=getTime(t3)
            }
            else
            {
                view.PitStopPremier.text = "PitStop"
            }

            // Second sprint of the first run
            if(t4 != "-1" )
            {
                view.seekBarPremier.progress = 4
                view.SprintPremier2.text=getTime(t4)
            }
            else
            {
                view.SprintPremier2.text = "Sprint"
            }

            // Second obstacle of the first run
            if(t5 != "-1" )
            {
                view.seekBarPremier.progress = 6
                view.ObstaclePremier2.text=getTime(t5)
                val totalTime = (t1.toInt()+t2.toInt()+t3.toInt()+t4.toInt()+t5.toInt()).toString()
                view.FinishPremier.text = "1st run : ${getTime(totalTime)}"
            }
            else
            {
                view.ObstaclePremier2.text = "Obstacle"
            }

            // On passe sur la deuxième seekbar

            // First sprint of the second run
            if(t6 != "-1" )
            {
                view.seekBarDeuxieme.progress++
                view.SprintDeuxieme1.text=getTime(t6)
            }
            else
            {
                view.SprintDeuxieme1.text = "Sprint"
            }

            // First obstacle of the second run
            if(t7 != "-1" )
            {
                view.seekBarDeuxieme.progress++
                view.ObstacleDeuxieme1.text=getTime(t7)
            }
            else
            {
                view.ObstacleDeuxieme1.text = "Obstacle"
            }


            // Pit stop of the second run
            if(t8 != "-1" )
            {
                view.seekBarDeuxieme.progress++
                view.PitStopDeuxieme.text=getTime(t8)
            }
            else
            {
                view.PitStopDeuxieme.text = "PitStop"
            }

            // Second sprint of the second run
            if(t9 != "-1" )
            {
                view.seekBarDeuxieme.progress++
                view.SprintDeuxieme2.text=getTime(t9)
            }
            else
            {
                view.SprintDeuxieme2.text = "Sprint"
            }

            // Second obstacle of the second run
            if(t10 != "-1" )
            {
                view.seekBarDeuxieme.progress = 6
                view.ObstacleDeuxieme2.text=getTime(t10)
                val totalTime = (t1.toInt()+t2.toInt()+t3.toInt()+t4.toInt()+t5.toInt()+t6.toInt()+t7.toInt()+t8.toInt()+t9.toInt()+t10.toInt()).toString()
                view.FinishDeuxieme.text = "Total : ${getTime(totalTime)}"
            }
            else
            {
                view.ObstacleDeuxieme2.text = "Obstacle"
            }

        }

    }

    /**
     * takes the raw time from the database and converts it to a cool string
     * */
    private fun getTime(time : String) : String
    {
        var calculatedTime : String
        var seconds = time.toInt()/1000
        var ms = time.toInt()%1000
        var msString = ms.toString()
        if(msString.length<3)
        {
            msString= "0"+ms.toString()
        }
        var minutes = 0

        if(seconds>60)
        {
            minutes = seconds/60
            seconds %= 60
        }
        calculatedTime = "${minutes}' ${seconds}\":$msString"
        return calculatedTime
    }


}