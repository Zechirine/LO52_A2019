package utbm.lo52.fail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_recycler_navigation.*
import kotlinx.android.synthetic.main.widget_player_score.view.*
import utbm.lo52.fail.constants.LapType
import utbm.lo52.fail.db.*

fun formatTime(seconds: Double): String {
    return seconds.toString()
}

@ExperimentalStdlibApi
class ScoreActivity : AppCompatActivity() {

    private val players = ArrayList<Player>()
    private val teams = ArrayList<Team>()

    private lateinit var race: Race
    private lateinit var db: DBHelper
    private lateinit var playerScoreAdapter: PlayerScoreAdapter

    override fun onBackPressed() {
        moveTaskToBack(false)
        goBack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_navigation)

        db = DBHelper(this, null)
        race = db.request(Race::class).get(
            "id",
            intent.getIntExtra(
                "raceID",
                if (savedInstanceState != null) savedInstanceState?.getInt("raceID", 0) else 0
            )
        ) as Race

        validateButton.hide()
        addElementButton.hide()
        cancelButton.setOnClickListener { goBack() }

        for (team in db.request(Team::class).filterRelated(
            Race::class,
            "id",
            race.id!!
        ).all() as List<Team>) {
            teams.add(team)
            players.addAll(
                db.request(Player::class).filterRelated(
                    Team::class,
                    "id",
                    team.id!!
                ).all() as List<Player>
            )
        }
        playerScoreAdapter = PlayerScoreAdapter(db)
        ItemList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ItemList.adapter = playerScoreAdapter
        players.sortBy {
            (db.request(Lap::class).filterRelated(
                Player::class,
                "id",
                it.id!!
            ).all() as List<Lap>).map { lap -> lap.chrono }.sum()
        }
        playerScoreAdapter.submitList(players)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt("raceID", race.id!!)
    }

    private fun goBack() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

}

@UseExperimental(ExperimentalStdlibApi::class)
class PlayerScoreAdapter(private val db: DBHelper) :
    ListAdapter<Player, PlayerScoreAdapter.PlayerScoreViewHolder>(PlayerDiffCallback()) {

    class PlayerScoreViewHolder(
        private val view: View,
        private val db: DBHelper
    ) : RecyclerView.ViewHolder(view) {
        fun bind(player: Player, position: Int) {
            val baseRequest = db.request(Lap::class).filterRelated(Player::class, "id", player.id!!)
            val sprints =
                (baseRequest.filter(
                    "laptype",
                    LapType.SPRINT.id
                ).all() as List<Lap>).map { it.chrono }
            val splitUp =
                (baseRequest.filter(
                    "laptype",
                    LapType.SPLIT_UP.id
                ).all() as List<Lap>).map { it.chrono }
            val pitStop =
                (baseRequest.filter(
                    "laptype",
                    LapType.PIT_STOP.id
                ).all() as List<Lap>).map { it.chrono }
            val runningTime = ArrayList<Int>()
            runningTime.addAll(sprints)
            runningTime.addAll(splitUp)

            view.playerRank.text = position.toString()
            view.playerNameText.text = player.name
            view.playerSprintTime.text = formatTime(sprints.average())
            view.playerSplitTime.text = formatTime(splitUp.average())
            view.playerPitTime.text = formatTime(pitStop.average())
            view.playerMeanTime.text = formatTime(runningTime.average())
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayerScoreViewHolder {
        return PlayerScoreViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.widget_player_score, parent, false), db
        )
    }

    override fun onBindViewHolder(holder: PlayerScoreViewHolder, position: Int) {
        holder.bind(getItem(position), position + 1)
    }
}
