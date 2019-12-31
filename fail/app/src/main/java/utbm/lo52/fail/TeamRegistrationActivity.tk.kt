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
import kotlinx.android.synthetic.main.registration_activity.*
import kotlinx.android.synthetic.main.team_widget.view.*
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
        setContentView(R.layout.registration_activity)

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
        fun bind(team: Team) {
            if (team.name != "") {
                view.teamNameText.setText(team.name)
            }
            view.teamNameText.addTextChangedListener(object : TextWatcher {
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
                }
            })
            view.deleteTeamButton.setOnClickListener {
                activity.removeTeam(team.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        return TeamViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.team_widget, parent, false), activity, db
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
