package com.example.lo52_f1_levier.view.coursetimer

import android.content.Context
import android.provider.BaseColumns
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.example.lo52_f1_levier.DAO.CoureurDao
import com.example.lo52_f1_levier.DAO.CourseDao
import com.example.lo52_f1_levier.DAO.EquipeDao
import com.example.lo52_f1_levier.DAO.ParticipeDao
import com.example.lo52_f1_levier.R
import com.example.lo52_f1_levier.model.Coureur
import com.example.lo52_f1_levier.model.Equipe
import com.example.lo52_f1_levier.model.Participe
import kotlinx.android.synthetic.main.team_box.view.*
import java.util.Collections.max
import kotlin.math.min


/**
 * Link data to the RecyclerView listing the team boxes in the CourseTimerActivity
 */
class TeamBoxGridAdapter(private val context: Context,
                         private val courseId: Int,
                         private val goToDetails: (Int) -> Boolean,
                         private val getTimerValue: () -> Long,
                         private val isTimerStarted: () -> Boolean,
                         private val courseReady: (lastTime: Long, isOver: Boolean) -> Unit,
                         private val courseOver: () -> Unit):
    RecyclerView.Adapter<TeamBoxGridAdapter.TeamBoxGridViewHolder>() {

    private val RUNNER_NAME_MAX_LENGTH = 15

    private val participeDao: ParticipeDao = ParticipeDao(context)
    private val runnerDao: CoureurDao = CoureurDao(context)
    private val teamDao: EquipeDao = EquipeDao(context)
    private val courseDao: CourseDao = CourseDao(context)

    private var teams: Array<TeamBoxData> = emptyArray()
    private var nbTeamFinished: Int = 0

    init {
        fetchTeamsData()

        val lastTime = max(teams.map { team -> team.totalTime })
        val isOver = courseDao.isCourseOver(courseId)
        courseReady(lastTime, isOver)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamBoxGridViewHolder {
        val teamBox: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.team_box, parent, false)
        return TeamBoxGridViewHolder(
            context,
            teamBox,
            goToDetails,
            isTimerStarted,
            this::saveTime,
            this::setTeamPosition,
            this::addTeamFinished
        )
    }

    override fun onBindViewHolder(viewHolder: TeamBoxGridViewHolder, position: Int) {
        viewHolder.setData(teams[position])
    }

    override fun getItemCount() = teams.size

    /**
     * Get the needed data of all the teams assigned to the course
     */
    private fun fetchTeamsData() {
        // get the runners of the course ordered by team
        val courseCursor = participeDao.getParticipeByC_ID(courseId)

        // check that course exists
        if (courseCursor === null || !courseCursor.moveToFirst()) {
            return
        }

        // indexes of relevant columns
        val teamIdIndex = courseCursor.getColumnIndex(Participe.ParticipeTable.EQUIPE_ID)
        val runnerIdIndex = courseCursor.getColumnIndex(Participe.ParticipeTable.COUREUR_ID)

        // info on the team being processed
        var currentTeam: Int = courseCursor.getInt(teamIdIndex)
        var currentTeamRunners: Array<String> = emptyArray()

        // turn every teams and runners data into relevant TeamBoxData
        do {
            val newTeam = courseCursor.getInt(teamIdIndex)

            // all runners of current team have been fetched
            if (currentTeam != newTeam) {
                // add team to the global list and prepare for the next one
                addTeamBoxData(currentTeam, currentTeamRunners)
                currentTeamRunners = emptyArray()
                currentTeam = newTeam
            }

            // add the current runner to the data of its team
            currentTeamRunners = fetchRunnerName(currentTeamRunners, courseCursor.getInt(runnerIdIndex))
        } while (courseCursor.moveToNext())
        courseCursor.close()

        // add the last team to the list
        addTeamBoxData(currentTeam, currentTeamRunners)
    }

    /**
     * Get the name of a given runner
     * and add it to the given list at the right position (depending on numc)
     * @return the new array
     */
    private fun fetchRunnerName(runners: Array<String>, runnerId: Int): Array<String> {
        val runnerCursor = runnerDao.getCoureurByID(runnerId)

        if (runnerCursor === null || !runnerCursor.moveToFirst()) {
            return addRunner(runners, "????", -1)
        }

        // get the relevant data of the runner
        val firstName = runnerCursor.getString(runnerCursor.getColumnIndex(Coureur.CoureurTable.CNAME))
        val lastName = runnerCursor.getString(runnerCursor.getColumnIndex(Coureur.CoureurTable.SURNAME))
        val position = runnerCursor.getInt(runnerCursor.getColumnIndex(Coureur.CoureurTable.NUMC))
        runnerCursor.close()

        // if full name fit in the team box
        if (("$firstName $lastName").length <= RUNNER_NAME_MAX_LENGTH) {
            return addRunner(runners, "$firstName $lastName", position)
        }

        // truncate the full name to make it fit
        val name = "${firstName[0]}. ${lastName.slice(0..min(12, lastName.length - 1))}"
        return addRunner(runners, name, position)
    }

    /**
     * Add a given runner name to a given list of runners at a given position
     * Increase the size of the array if necessary
     * @return the new array
     */
    private fun addRunner(runners: Array<String>, name: String, position: Int): Array<String> {
        if (position < 0) {
            return runners.plus(name)
        }

        // increase the size of the array just enough as to be able to fit the runner at the right pos
        var tmpRunners = runners.clone()
        if (position >= runners.size) {
            val tmp = (0..(position - runners.size)).toList().map { "????" }.toTypedArray()
            tmpRunners = runners.plus(tmp)
        }

        // add the runner to the list and return it
        tmpRunners[position] = name
        return tmpRunners
    }

    /**
     * Turn ugly raw data into a beautiful TeamBoxData
     * and add it to the global list
     */
    private fun addTeamBoxData(teamId: Int, runners: Array<String>) {
        // get the team data in db
        val teamCursor = teamDao.getEquipeByID(teamId)

        // fetch the relevant data
        val teamNumber : Int
        val teamPos : Int
        if (teamCursor === null || !teamCursor.moveToFirst()) {
            teamNumber = -1
            teamPos = 0
        } else {
            teamNumber = teamCursor.getInt(teamCursor.getColumnIndex(Equipe.EquipeTable.ENUM))
            teamPos = teamCursor.getInt(teamCursor.getColumnIndex(Equipe.EquipeTable.POSITION))
        }
        teamCursor?.close()

        // get the number of times (first) and the last time (second)
        val timeInfo = participeDao.getNbAndTotalTimeOfTeam(courseId, teamId)

        val teamData = TeamBoxData(teamId, teamNumber, runners, teamPos, timeInfo.second)
        teamData.setTotalStepsDone(timeInfo.first)

        teams = teams.plus(teamData)
    }

    /**
     * Save a new time for a given runner of a given team
     * @return the new time
     */
    private fun saveTime(teamId: Int, runnerPos: Int, numTime: Int, prevTotalTime: Long): Long {
        val runnerCursor = participeDao.getCoureurByTeamIdAndNumc(teamId, runnerPos)

        if (runnerCursor === null || !runnerCursor.moveToFirst()) {
            return 0
        }

        val runnerId = runnerCursor.getInt(runnerCursor.getColumnIndex(BaseColumns._ID))
        val newTime = getTimerValue() - prevTotalTime
        participeDao.setTimeByRunnerId(runnerId, numTime, newTime)

        runnerCursor.close()
        return newTime + prevTotalTime
    }

    @Synchronized
    private fun setTeamPosition(teamId: Int): Int {
        val nextPos = participeDao.nextTeamPosition(courseId)
        teamDao.updatePosition(teamId, nextPos)
        return nextPos
    }

    /**
     * Callback when a team finishes the course
     */
    private fun addTeamFinished() {
        nbTeamFinished++
        if (nbTeamFinished == teams.size) {
            courseOver()
            courseDao.setCourseOver(courseId)
        }
    }

    /**
     * Data management of a single team box
     */
    class TeamBoxGridViewHolder(private val context: Context?,
                                private val teambox: View,
                                private val goToDetails: (Int) -> Boolean,
                                private val isTimerStarted: () -> Boolean,
                                private val saveTime: (teamId: Int, runnerPos: Int, numTime: Int, prevTotalTime: Long) -> Long,
                                private val setTeamPosition: (teamId: Int) -> Int,
                                private val teamFinished: () -> Unit):
        RecyclerView.ViewHolder(teambox), View.OnCreateContextMenuListener {

        init {
            teambox.setOnCreateContextMenuListener(this)
        }

        /**
         * Link the team box with a given team
         */
        fun setData(team: TeamBoxData) {
            teambox.teamNumber.text = team.teamNumber.toString()
            teambox.progressBar.max = team.NB_TOTAL_STEPS
            updateData(team)

            teambox.setOnClickListener {
                if (isTimerStarted() && !team.isOver) {
                    nextStep(team)
                }
            }
        }

        /**
         * Update the team box with the new team data
         */
        private fun updateData(team: TeamBoxData) {
            if (team.isOver) {
                displayFinish(team)
                return
            }

            teambox.runnerName.text = team.getCurrentRunner()

            teambox.imgJersey.setImageResource(team.getDrawableRunner())
            teambox.imgPassage.setImageResource(team.getDrawablePassage())
            teambox.imgStep.setImageResource(team.getDrawableStep())

            teambox.progressBar.progress = team.getTotalStepsDone()
        }

        /**
         * Save the time and display the next step of the course for the team
         */
        private fun nextStep(team: TeamBoxData) {
            if (team.isOver) {
                return
            }

            val runnerStep = team.step + (team.passage - 1) * team.NB_STEPS
            team.totalTime = saveTime(team.teamId, team.runner-1, runnerStep, team.totalTime)
            team.incrementStep()
            updateData(team)
        }

        /**
         * Display the final state of the team box
         */
        private fun displayFinish(team: TeamBoxData) {
            if (team.position == 0) {
                team.position = setTeamPosition(team.teamId)
            }

            teamFinished()
            teambox.progressBar.progress = teambox.progressBar.max

            // disable running state
            teambox.runningState.visibility = View.INVISIBLE
            teambox.imgJersey.visibility = View.GONE

            // setup finish state
            teambox.finishState.visibility = View.VISIBLE
            teambox.runnerName.text = "Parcours Terminé"
            teambox.imgFinish.setImageResource(team.getDrawablePosition())
            teambox.teamTime.text = team.formattedLastTime()

            // setup team pos (image + text offset)
            val offset = context!!.resources.getDimension(team.getOffsetPosition()).toInt()
            teambox.teamPos.updateLayoutParams<ConstraintLayout.LayoutParams> { setMargins(0, 0, 0, offset) }
            teambox.teamPos.text = team.position.toString()
        }

        override fun onCreateContextMenu(menu: ContextMenu, teamBox: View, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu.add(teambox.teamNumber.text.toString().toInt(), 0, 0, "Détails")
                .setOnMenuItemClickListener { goToDetails(teamBox.teamNumber.text.toString().toInt()) }
        }
    }
}
