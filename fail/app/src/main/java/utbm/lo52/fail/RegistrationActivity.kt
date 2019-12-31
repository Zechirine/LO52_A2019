package utbm.lo52.fail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class RegistrationActivity : AppCompatActivity() {

    private var players = LinkedList<Player>()
    private val teams = ArrayList<Team>()

    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var db: DBHelper
    private lateinit var race: Race

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)

        db = DBHelper(this, null)
        playerAdapter = PlayerAdapter(this, db)

        // This should not fail, normally
        race = db.request(Race::class).get(
            "id",
            intent.getIntExtra(
                "raceID",
                if (savedInstanceState != null) savedInstanceState?.getInt("raceID", 0) else 0
            )
        ) as Race
        if (!db.request(Team::class).filterRelated(Race::class, "id", race.id!!).exists())
            db.save(Team(null, "team 1", ForeignKey(Race::class, race.id)))

        // Restore player list
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
        PlayerList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        PlayerList.adapter = playerAdapter
        playerAdapter.submitList(players)

        addPlayerButton.setOnClickListener {
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
//        playerAdapter.notifyDataSetChanged()
        Log.v("players", players.toString())
    }

    fun removePlayer(id: Int?) {
        val newPlayers = LinkedList<Player>()
        for (player in players) {
            if (player.id != id) {
                newPlayers.add(player)
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
class PlayerAdapter(val activity: RegistrationActivity, val db: DBHelper) :
    ListAdapter<Player, PlayerAdapter.PlayerViewHolder>(DiffCallback()) {

    @ExperimentalStdlibApi
    class PlayerViewHolder(val view: View, val activity: RegistrationActivity, val db: DBHelper) :
        RecyclerView.ViewHolder(view) {
        fun bind(player: Player) {
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


class DiffCallback : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem == newItem
    }
}