package utbm.lo52.fail

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
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
import utbm.lo52.fail.constants.MAX_ORDERING
import utbm.lo52.fail.constants.MIN_ORDERING
import utbm.lo52.fail.db.*

@ExperimentalStdlibApi
class PlayerRegistrationActivity : AppCompatActivity() {

    val teams = ArrayList<Team>()
    private var players = ArrayList<Player>()
    private var sorted = false

    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var db: DBHelper
    private lateinit var race: Race

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.sortButton -> {
            sorted = true
            orderPlayers()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.reorganize, menu)
        return true;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)

        db = DBHelper(this, null)
        playerAdapter = PlayerAdapter(this)

        // Get the current race
        // This should not fail, normally
        race = db.request(Race::class).get(
            "id",
            intent.getIntExtra(
                "raceID",
                if (savedInstanceState != null) savedInstanceState?.getInt("raceID", 0) else 0
            )
        ) as Race
        sorted = if (savedInstanceState != null) savedInstanceState?.getBoolean(
            "sorted",
            false
        ) else false

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

        cancelButton.setOnClickListener {
            // Go back to team creation without touching anything
            val intent = Intent(this, TeamRegistrationActivity::class.java)
            intent.putExtra("raceID", race!!.id)
            startActivity(intent)
        }

        // If there is no player, there are as many default player as the number of team
        if (players.isEmpty()) {
            for (team in teams)
                addPlayer()
        }

        if (sorted)
            orderPlayers()
    }

    private fun orderPlayers() {
        players.sortWith(nullsLast(compareBy({ it.team.id }, { it.ordering })))
        playerAdapter.notifyDataSetChanged()
    }


    private fun addPlayer() {
        val player = db.save(
            Player(
                null,
                "",
                MIN_ORDERING,
                ForeignKey(Team::class, teams[0].id!!)
            )
        ) as Player
        players.add(player)
        playerAdapter.notifyDataSetChanged()
    }

    fun removePlayer(player: Player) {
        db.delete(player)
        players.remove(player)
        playerAdapter.notifyDataSetChanged()
    }

    fun updatePlayer(saved: Player, position: Int) {
        silentUpdatePlayer(saved, position)
        playerAdapter.notifyItemChanged(position)
    }

    fun silentUpdatePlayer(saved: Player, position: Int) {
        players[position] = db.save(saved) as Player
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt("raceID", race.id!!)
        outState?.putBoolean("sorted", sorted)
    }
}


@UseExperimental(ExperimentalStdlibApi::class)
class PlayerAdapter(private val activity: PlayerRegistrationActivity) :
    ListAdapter<Player, PlayerAdapter.PlayerViewHolder>(PlayerDiffCallback()) {

    @ExperimentalStdlibApi
    class PlayerViewHolder(
        private val view: View,
        private val activity: PlayerRegistrationActivity
    ) :
        RecyclerView.ViewHolder(view) {
        fun bind(player: Player, position: Int) {

            Log.v("test", player.toString())
            // Handle Text entry
            view.playerNameText.setText(player.name)
            view.playerNameText.addTextChangedListener(object : TextWatcher {
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
                        player, position
                    )
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
                    activity.silentUpdatePlayer(
                        Player(
                            player.id,
                            player.name,
                            player.ordering,
                            ForeignKey(Team::class, adapter.getItem(pos).id)
                        ), position
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // It can not happen (hopefully)
                }

            }
            view.teamSpinner.setSelection(adapter.getPositionFromDbId(player.team.id))

            // Handle delete button
            view.deletePlayerButton.setOnClickListener {
                activity.removePlayer(player)
            }

            // Handle ordering
            view.orderingText.setText(player.ordering.toString())
//            view.orderingText.setOnFocusChangeListener { v, hasFocus ->
//                if (hasFocus) v.orderingText.setText(
//                    ""
//                )
//                else view.orderingText.setText(player.ordering.toString())
//            }
            view.orderingText.addTextChangedListener(object : TextWatcher {
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
                    var ordering = s.toString().toIntOrNull()
                    if (ordering == null || ordering < MIN_ORDERING)
                        ordering = MIN_ORDERING
                    if (ordering > MAX_ORDERING)
                        ordering = MAX_ORDERING
                    player.ordering = ordering
                    activity.silentUpdatePlayer(
                        player, position
                    )
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.player_widget, parent, false), activity
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

