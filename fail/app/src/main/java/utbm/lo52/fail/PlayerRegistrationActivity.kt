package utbm.lo52.fail

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var db: DBHelper
    private lateinit var race: Race

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.sortButton -> {
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

        orderPlayers()
    }

    private fun orderPlayers() {
        players.sortWith(nullsLast(compareBy({ it.team.id }, { it.ordering })))
        ItemList.recycledViewPool.clear()
        playerAdapter.notifyDataSetChanged()
    }


    private fun addPlayer(team: Team, ordering: Int) {
        val player = db.save(
            Player(
                null,
                "TESTTEST",
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
}


@UseExperimental(ExperimentalStdlibApi::class)
class PlayerAdapter(private val activity: PlayerRegistrationActivity) :
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
        }
    }

    @ExperimentalStdlibApi
    class PlayerViewHolder(
        private val view: View,
        private val activity: PlayerRegistrationActivity
    ) :
        RecyclerView.ViewHolder(view) {

        private var playerNameListener: TextWatcher? = null

        fun bind(player: Player, playerPosition: Int) {

            // Handle Text entry
            if (playerNameListener != null) {
                view.playerNameText.removeTextChangedListener(playerNameListener)
            }
            playerNameListener = PlayerNameListener(player, playerPosition, activity)
            view.playerNameText.setText(player.name)
            view.playerNameText.addTextChangedListener(playerNameListener)

            // Handle drop down team list
            val teamAdapter = TeamSpinnerAdapter(activity.teams)
            view.teamSpinner.adapter = teamAdapter
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
                            ForeignKey(Team::class, teamAdapter.getItem(pos).id)
                        ), playerPosition
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // It can not happen (hopefully)
                }

            }
            view.teamSpinner.setSelection(teamAdapter.getPositionFromDbId(player.team.id))

            // Handle drop down ordering list
            val orderingAdapter = OrderingAdapter()
            view.orderingSpinner.adapter = orderingAdapter
            view.orderingSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // It can not happen (hopefully)
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        pos: Int,
                        id: Long
                    ) {
                        // Save the selected team for the player
                        activity.silentUpdatePlayer(
                            Player(
                                player.id,
                                player.name,
                                orderingAdapter.getItem(pos),
                                player.team
                            ), playerPosition
                        )
                    }

                }
            view.orderingSpinner.setSelection(orderingAdapter.getPositionFromValue(player.ordering))
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
        return oldItem.id == newItem.id &&
                oldItem.name == newItem.name &&
                oldItem.ordering == newItem.ordering &&
                oldItem.team.id == newItem.team.id
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

class OrderingAdapter : BaseAdapter() {
    private val orders = IntRange(MIN_ORDERING, MAX_ORDERING).toList()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(parent?.context)
            .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false) as TextView
        view.text = this.getItem(position).toString()
        view.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        return view
    }

    fun getPositionFromValue(value: Int): Int {
        for ((pos, ordering) in orders.withIndex()) {
            if (value == ordering)
                return pos
        }
        return 0
    }

    override fun getItem(position: Int): Int {
        return orders[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return orders.size
    }

}

