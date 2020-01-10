package utbm.lo52.fail

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_recycler_navigation.*
import kotlinx.android.synthetic.main.widget_team.view.*
import utbm.lo52.fail.constants.MAX_TEAM_PER_RACE
import utbm.lo52.fail.db.DBHelper
import utbm.lo52.fail.db.ForeignKey
import utbm.lo52.fail.db.Race
import utbm.lo52.fail.db.Team
import java.util.*

@ExperimentalStdlibApi
class TeamRegistrationActivity : AppCompatActivity() {

    private var teams = LinkedList<Team>()

    private lateinit var teamAdapter: TeamAdapter
    private lateinit var db: DBHelper
    private lateinit var race: Race

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_navigation)

        db = DBHelper(this, null)
        teamAdapter = TeamAdapter(this, db)

        // Get the current race
        race = db.request(Race::class).get(
            "id",
            intent.getIntExtra(
                "raceID",
                if (savedInstanceState != null) savedInstanceState?.getInt("raceID", 0) else 0
            )
        ) as Race

        validateButton.setOnClickListener {
            val intent = Intent(this, PlayerRegistrationActivity::class.java)
            intent.putExtra("raceID", race!!.id)
            startActivity(intent)
        }

        // Restore team list
        for (team in db.request(Team::class).filterRelated(
            Race::class,
            "id",
            race.id!!
        ).all() as List<Team>) {
            teams.add(team)
        }

        ItemList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ItemList.adapter = teamAdapter
        teamAdapter.submitList(teams)

        addElementButton.setOnClickListener {
            addTeam()
        }

        cancelButton.setOnClickListener {
            // Delete and clean the race
            db.delete(race) // Normally, the ORM should delete everything with db constraints
            // Go back to main menu
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // If there is no team, add at least two teams
        if (teams.isEmpty()) {
            addTeam()
            addTeam()
        }

    }

    private fun addTeam() {
        val team = db.save(
            Team(
                null,
                "${getString(R.string.registration_team_base_name)} ${teams.size + 1}",
                ForeignKey(Race::class, race.id)
            )
        ) as Team
        teams = LinkedList(teams)
        teams.add(team)
        teamAdapter.submitList(teams)
        validation()
    }

    fun removeTeam(id: Int?) {
        val newTeams = LinkedList<Team>()
        for (team in teams) {
            if (team.id != id) {
                newTeams.add(team)
            } else {
                db.delete(team)
            }
        }
        teams = newTeams
        teamAdapter.submitList(teams)
        validation()
    }

    fun validation() {
        validateButton.isEnabled = true
        addElementButton.isEnabled = true

        if (teams.isEmpty() || teams.any { it.name == "" })
            validateButton.isEnabled = false

        if (teams.size >= MAX_TEAM_PER_RACE)
            addElementButton.isEnabled = false
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt("raceID", race.id!!)
    }
}

@UseExperimental(ExperimentalStdlibApi::class)
class TeamAdapter(private val activity: TeamRegistrationActivity, private val db: DBHelper) :
    ListAdapter<Team, TeamAdapter.TeamViewHolder>(TeamDiffCallback()) {

    @ExperimentalStdlibApi
    class TeamViewHolder(
        private val view: View,
        private val activity: TeamRegistrationActivity,
        val db: DBHelper
    ) :
        RecyclerView.ViewHolder(view) {

        private var teamNameListener: TextWatcher? = null
        fun bind(team: Team) {
            view.teamNameText.setText(team.name)
            if (teamNameListener != null)
                view.teamNameText.removeTextChangedListener(teamNameListener)
            teamNameListener = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    team.name = s?.toString() ?: ""
                    db.save(team)
                    activity.validation()
                }
            }
            view.teamNameText.addTextChangedListener(teamNameListener)
            view.deleteTeamButton.setOnClickListener {
                activity.removeTeam(team.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        return TeamViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.widget_team, parent, false), activity, db
        )
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}


class TeamDiffCallback : DiffUtil.ItemCallback<Team>() {
    override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
        return oldItem == newItem
    }
}
