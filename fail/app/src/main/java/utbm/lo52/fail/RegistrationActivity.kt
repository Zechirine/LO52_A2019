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

@ExperimentalStdlibApi
class RegistrationActivity : AppCompatActivity() {

    private val players = ArrayList<Player>()
    private val teams = ArrayList<Team>()

    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var db: DBHelper
    private lateinit var race: Race

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)

        db = DBHelper(this, null)
        playerAdapter = PlayerAdapter(db)

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

        Log.v("pd", "raceid: ${race.id}, racename: ${race.name}")
        // Restore player list
        for (team in db.request(Team::class).filterRelated(
            Race::class,
            "id",
            race.id!!
        ).all() as List<Team>) {
            Log.v("pd", "teamid: ${team.id}, teamname: ${team.name}")
            teams.add(team)
            for (player in db.request(Player::class).filter(
                "team_id",
                team.id!!
            ).all() as List<Player>) {
                Log.v("pd", "playerid ${player.id}")
                players.add(player)
            }
        }

        playerAdapter.submitList(players)
        PlayerList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        PlayerList.adapter = playerAdapter

        addPlayerButton.setOnClickListener {
            addPlayer()
        }
    }

    private fun addPlayer() {
        val player = db.save(Player(null, "", 0, ForeignKey(Team::class, teams[0].id!!))) as Player
        players.add(player)
        playerAdapter.submitList(players)
        Log.v("players", players.toString())
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt("raceID", race.id!!)
    }
}


@UseExperimental(ExperimentalStdlibApi::class)
class PlayerAdapter(val db: DBHelper) :
    ListAdapter<Player, PlayerAdapter.PlayerViewHolder>(DiffCallback()) {

    @ExperimentalStdlibApi
    class PlayerViewHolder(val view: View, val db: DBHelper) : RecyclerView.ViewHolder(view) {
        fun bind(player: Player) {
            if (player.name != "") {
                view.editText.setText(player.name)
            }
            view.editText.addTextChangedListener(object : TextWatcher {
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.player_widget, parent, false), db
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