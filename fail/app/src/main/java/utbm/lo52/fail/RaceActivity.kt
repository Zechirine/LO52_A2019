package utbm.lo52.fail

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_race.*
import utbm.lo52.fail.constants.LapType
import utbm.lo52.fail.constants.MIN_ORDERING
import utbm.lo52.fail.db.*

@ExperimentalStdlibApi
class RaceActivity : AppCompatActivity() {

    private val teams = ArrayList<Team>()

    private var timeAtPause: Long = 0
    private var isChronoStarted = false
    private var buttonMap = HashMap<Int, Button>()

    private lateinit var db: DBHelper
    private lateinit var race: Race

    private fun elapsedTime() = SystemClock.elapsedRealtime() - chrono.base

    private fun chronoTime(): Int {
        val splitTime = chrono.text.split(":")
        return splitTime[0].toInt() * 60 + splitTime[1].toInt()
    }

    private fun currentRunner(team: Team): Player {
        var min: Int? = null
        var minPlayer: Player? = null
        for (player in db.request(Player::class).filterRelated(
            Team::class,
            "id",
            team.id!!
        ).all() as List<Player>) {
            val laps =
                db.request(Lap::class).filterRelated(Player::class, "id", player.id!!).all().size
            if (min == null || laps < min) {
                min = laps
                minPlayer = player
            }
        }
        return minPlayer!!
    }

    private fun hasLapLeft(team: Team): Boolean {
        val player = currentRunner(team)
        val lastPlayer = db.request(Player::class).filterRelated(
            Team::class,
            "id",
            team.id!!
        ).all().last() as Player
        val laps = db.request(Lap::class).filterRelated(Player::class, "id", player.id!!).all().size

        return !(player.id == lastPlayer.id && laps > race.lap)
    }

    private fun addLap(team: Team) {
        val player = currentRunner(team)
        val laps = db.request(Lap::class).filterRelated(Player::class, "id", player.id!!).all().size

        Log.v(
            "TEST",
            (db.save(
                Lap(
                    null,
                    chronoTime(),
                    laps,
                    ForeignKey(Player::class, player.id)
                )
            ) as Lap).toString()
        )
    }

    private fun teamInfo(team: Team): String {
        val player = currentRunner(team)
        val laps = db.request(Lap::class).filterRelated(Player::class, "id", player.id!!).all().size
        return "${team.name}\n${player.name}\n${LapType.LAP_TYPE_ORDER[laps].getString(this)}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_race)

        if (savedInstanceState != null) {
            timeAtPause = savedInstanceState?.getLong("elapsedSeconds", 0)
            isChronoStarted = savedInstanceState?.getBoolean("isChronoStarted", false)
        }

        chrono.base = SystemClock.elapsedRealtime() - timeAtPause
        db = DBHelper(this, null)
        race = db.request(Race::class).get(
            "id",
            intent.getIntExtra(
                "raceID",
                if (savedInstanceState != null)
                    savedInstanceState?.getInt("raceID", 0)
                else 0
            )
        ) as Race

        for (team in db.request(Team::class).filterRelated(
            Race::class, "id", race.id!!
        ).all() as List<Team>) {
            teams.add(team)
            val player = db.request(Player::class).filterRelated(
                Team::class,
                "id",
                team.id!!
            ).filter("ordering", MIN_ORDERING).first() as Player
            val button = Button(this)
            button.text = teamInfo(team)
            button.setOnClickListener {
                addLap(team)
                if (hasLapLeft(team))
                    button.text = teamInfo(team)
                else button.isEnabled = false
            }
            button.isEnabled = false
            buttonMap[team.id!!] = button
            teamButtonLayout.addView(button)
        }

        pauseButton.isEnabled = false
        pauseButton.isVisible = false
        startButton.setOnClickListener {
            isChronoStarted = true
            chrono.base = SystemClock.elapsedRealtime() - timeAtPause
            chrono.start()
            startButton.isEnabled = false
            startButton.isVisible = false
            pauseButton.isEnabled = true
            pauseButton.isVisible = true

            for (key in buttonMap.keys) {
                if (hasLapLeft(db.request(Team::class).get("id", key) as Team))
                    buttonMap[key]!!.isEnabled = true
            }
        }

        pauseButton.setOnClickListener {
            isChronoStarted = false
            chrono.stop()
            timeAtPause = elapsedTime()
            startButton.isEnabled = true
            startButton.isVisible = true
            pauseButton.isEnabled = false
            pauseButton.isVisible = false

            for (key in buttonMap.keys) {
                buttonMap[key]!!.isEnabled = false
            }
        }

        if (isChronoStarted) startButton.callOnClick()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt("raceID", race.id!!)
        if (isChronoStarted) outState?.putLong("elapsedSeconds", elapsedTime())
        else outState?.putLong("elapsedSeconds", timeAtPause)
        outState?.putBoolean("isChronoStarted", isChronoStarted)
    }
}
