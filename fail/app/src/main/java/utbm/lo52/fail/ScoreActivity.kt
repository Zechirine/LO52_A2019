package utbm.lo52.fail

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_recycler_navigation.*
import kotlinx.android.synthetic.main.widget_score.view.*
import utbm.lo52.fail.constants.LapType
import utbm.lo52.fail.db.*

fun formatTime(seconds: Double): String {
    return DateUtils.formatElapsedTime(seconds.toLong())
}

@ExperimentalStdlibApi
class ScoreActivity : AppCompatActivity() {

    private val players = ArrayList<Player>()
    private val teams = ArrayList<Team>()

    private var isPlayerScore = true

    private lateinit var race: Race
    private lateinit var db: DBHelper
    private lateinit var playerScoreAdapter: PlayerScoreAdapter
    private lateinit var teamScoreAdapter: TeamScoreAdapter

    // Add behavior on navbar menus
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.swapButton -> {
            isPlayerScore = !isPlayerScore
            selectAdapter()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    // Attach layout to navbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.swap, menu)
        return true
    }

    // Avoid back button to return to timer view
    override fun onBackPressed() {
        moveTaskToBack(false)
        goBack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_navigation)

        if (savedInstanceState != null) isPlayerScore =
            savedInstanceState.getBoolean("isPlayerScore", true)

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

        // Sort players by rank
        players.sortBy {
            (db.request(Lap::class).filterRelated(
                Player::class,
                "id",
                it.id!!
            ).all() as List<Lap>).map { lap -> lap.chrono }.sum()
        }
        playerScoreAdapter.submitList(players)

        teamScoreAdapter = TeamScoreAdapter(db)
        // Sort teams by winning
        teams.sortBy {
            (db.request(Lap::class).filterRelated(
                Player::class,
                "team_id",
                it.id!!
            ).all() as List<Lap>).map { lap -> lap.chrono }.sum()
        }
        teamScoreAdapter.submitList(teams)
        selectAdapter()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt("raceID", race.id!!)
        outState?.putBoolean("isPlayerScore", isPlayerScore)
    }

    private fun goBack() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    private fun selectAdapter() {
        if (isPlayerScore)
            ItemList.adapter = playerScoreAdapter
        else
            ItemList.adapter = teamScoreAdapter
    }

}

@ExperimentalStdlibApi
class ScoreViewHolder(private val view: View, private val db: DBHelper) :
    RecyclerView.ViewHolder(view) {
    fun bind(baseRequest: DBHelper.SQLRequest, name: String, rank: Int) {
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

        view.rankText.text = rank.toString()
        view.nameText.text = name
        view.sprintAverageText.text = formatTime(sprints.average())
        view.splitUpAverageText.text = formatTime(splitUp.average())
        view.pitStopAverageText.text = formatTime(pitStop.average())
        view.runningAverageText.text = formatTime(runningTime.average())
    }
}

@UseExperimental(ExperimentalStdlibApi::class)
class PlayerScoreAdapter(private val db: DBHelper) :
    ListAdapter<Player, ScoreViewHolder>(PlayerDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScoreViewHolder {
        return ScoreViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.widget_score, parent, false), db
        )
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val player = getItem(position)
        holder.bind(
            db.request(Lap::class).filterRelated(Player::class, "id", player.id!!),
            player.name,
            position + 1
        )
    }
}

@ExperimentalStdlibApi
class TeamScoreAdapter(private val db: DBHelper) :
    ListAdapter<Team, ScoreViewHolder>(TeamDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScoreViewHolder {
        return ScoreViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.widget_score, parent, false), db
        )
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val team = getItem(position)
        holder.bind(
            db.request(Lap::class).filterRelated(
                Player::class,
                "team_id",
                team.id!!
            ), team.name, position + 1
        )
    }
}