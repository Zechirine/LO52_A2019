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
import kotlinx.android.synthetic.main.widget_player.view.*
import utbm.lo52.fail.constants.MAX_ORDERING
import utbm.lo52.fail.db.*

@ExperimentalStdlibApi
class PlayerRegistrationActivity : AppCompatActivity() {

    private val teams = ArrayList<Team>()
    private var players = ArrayList<Player>()

    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var db: DBHelper
    private lateinit var race: Race

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_navigation)

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

        addElementButton.hide()

        cancelButton.setOnClickListener {
            // Go back to team creation without touching anything
            val intent = Intent(this, TeamRegistrationActivity::class.java)
            intent.putExtra("raceID", race!!.id)
            startActivity(intent)
        }

        validateButton.setOnClickListener {
            val intent = Intent(this, RaceActivity::class.java)
            intent.putExtra("raceID", race!!.id)
            startActivity(intent)
        }

        // If there is no player, there are as many default player as the number of team
        if (players.isEmpty()) {
            for (team in teams) {
                for (i in IntRange(1, MAX_ORDERING))
                    addPlayer(team, i)
            }
        }
        validation()
    }

    private fun addPlayer(team: Team, ordering: Int) {
        val player = db.save(
            Player(
                null,
                "Player ${players.size + 1}",
                ordering,
                ForeignKey(Team::class, team.id)
            )
        ) as Player
        players.add(player)
        playerAdapter.notifyDataSetChanged()
    }

    fun silentUpdatePlayer(saved: Player, position: Int) {
        players[position] = db.save(saved) as Player
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt("raceID", race.id!!)
    }

    fun validation() {
        validateButton.isEnabled = true

        // Check that every player has a non empty name
        if (players.any { it.name == "" }) {
            validateButton.isEnabled = false
        }
    }
}


@UseExperimental(ExperimentalStdlibApi::class)
class PlayerAdapter(private val activity: PlayerRegistrationActivity, private val db: DBHelper) :
    ListAdapter<Player, PlayerAdapter.PlayerViewHolder>(PlayerDiffCallback()) {

    class PlayerNameListener(
        private val player: Player,
        private val playerPosition: Int,
        private val activity: PlayerRegistrationActivity
    ) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            player.name = s?.toString() ?: ""
            activity.silentUpdatePlayer(
                player, playerPosition
            )
            activity.validation()
        }
    }

    @ExperimentalStdlibApi
    class PlayerViewHolder(
        private val view: View,
        private val activity: PlayerRegistrationActivity,
        private val db: DBHelper
    ) :
        RecyclerView.ViewHolder(view) {

        private var playerNameListener: TextWatcher? = null

        fun bind(player: Player, playerPosition: Int) {

            // Handle Text entry
            if (playerNameListener != null) {
                view.nameText.removeTextChangedListener(playerNameListener)
            }
            playerNameListener = PlayerNameListener(player, playerPosition, activity)
            view.nameText.setText(player.name)
            view.nameText.addTextChangedListener(playerNameListener)

            view.teamTextView.text = (player.team.getRelated(db) as Team).name
            view.orderingTextView.text = player.ordering.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.widget_player, parent, false), activity, db
        )
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

}


class PlayerDiffCallback : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.name == newItem.name &&
                oldItem.ordering == newItem.ordering &&
                oldItem.team.id == newItem.team.id
    }
}
