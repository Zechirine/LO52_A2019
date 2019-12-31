package utbm.lo52.fail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.player_widget.view.*
import kotlinx.android.synthetic.main.registration_activity.*
import utbm.lo52.fail.db.*
import java.util.*
import kotlin.collections.ArrayList

@ExperimentalStdlibApi
class PlayerRegistrationActivity : AppCompatActivity() {

    val teams = ArrayList<Team>()
    private var players = LinkedList<Player>()

    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var db: DBHelper
    private lateinit var race: Race


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)

        db = DBHelper(this, null)
        playerAdapter = PlayerAdapter(this, db)

        // Get the current race
        // This should not fail, normally
        race = db.request(Race::class).get(
            "id",
            intent.getIntExtra(
                "raceID",
                if (savedInstanceState != null) savedInstanceState?.getInt("raceID", 0) else 0
            )
        ) as Race

        // Restore player and team list
        for (team in db.request(Team::class).filterRelated(
            Race::class,
            "id",
            race.id!!
        ).all() as List<Team>) {
            teams.add(team)
            for (player in db.request(Player::class).filter(
                "team_id",
                team.id!!
            ).all() as List<Player>) {
                players.add(player)
            }

        }
        ItemList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ItemList.adapter = playerAdapter
        playerAdapter.submitList(players)

        addElementButton.setOnClickListener {
            addPlayer()
        }

        // If there is no player, there are as many default player as the number of team
        if (players.isEmpty()) {
            for (team in teams)
                addPlayer()
        }

    }

    private fun addPlayer() {
        val player = db.save(Player(null, "", 0, ForeignKey(Team::class, teams[0].id!!))) as Player
        players = LinkedList(players)
        players.add(player)
        playerAdapter.submitList(players)
    }

    fun removePlayer(id: Int?) {
        val newPlayers = LinkedList<Player>()
        for (player in players) {
            if (player.id != id) {
                newPlayers.add(player)
            } else {
                db.delete(player)
            }
        }
        players = newPlayers
        playerAdapter.submitList(players)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt("raceID", race.id!!)
    }
}


@UseExperimental(ExperimentalStdlibApi::class)
class PlayerAdapter(private val activity: PlayerRegistrationActivity, private val db: DBHelper) :
    ListAdapter<Player, PlayerAdapter.PlayerViewHolder>(PlayerDiffCallback()) {

    @ExperimentalStdlibApi
    class PlayerViewHolder(
        private val view: View,
        private val activity: PlayerRegistrationActivity,
        val db: DBHelper
    ) :
        RecyclerView.ViewHolder(view) {
        fun bind(player: Player) {

            // Handle Text entry
            if (player.name != "") {
                view.playerNameText.setText(player.name)
            }
            view.playerNameText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    player.name = s?.toString() ?: ""
                    db.save(player)
                }
            })

            // Handle drop down team list
            val adapter = TeamSpinnerAdapter(activity.teams)
            view.teamSpinner.adapter = adapter
            view.teamSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    pos: Int,
                    id: Long
                ) {
                    // Save the selected team for the player
                    val player = Player(
                        player.id,
                        player.name,
                        player.ordering,
                        ForeignKey(Team::class, adapter.getItem(pos).id)
                    )
                    db.save(player)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // It can not happen (hopefully)
                }

            }
            view.teamSpinner.setSelection(adapter.getPositionFromDbId(player.team.id))

            // Handle delete button
            view.deletePlayerButton.setOnClickListener {
                activity.removePlayer(player.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.player_widget, parent, false), activity, db
        )
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}


class PlayerDiffCallback : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem == newItem
    }
}

class TeamSpinnerAdapter(private val teams: List<Team>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(parent?.context)
            .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false) as TextView
        view.text = this.getItem(position).name
        return view
    }

    override fun getItem(position: Int): Team {
        return teams[position]
    }

    override fun getItemId(position: Int): Long {
        return teams[position].id!!.toLong()
    }

    override fun getCount(): Int {
        return teams.size
    }

    fun getPositionFromDbId(id: Int?): Int {
        for ((pos, team) in teams.withIndex()) {
            if (team.id == id)
                return pos
        }
        return 0
    }
}

