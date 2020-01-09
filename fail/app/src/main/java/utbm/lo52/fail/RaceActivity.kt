package utbm.lo52.fail

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_race.*
import utbm.lo52.fail.constants.MIN_ORDERING
import utbm.lo52.fail.db.DBHelper
import utbm.lo52.fail.db.Player
import utbm.lo52.fail.db.Race
import utbm.lo52.fail.db.Team

@ExperimentalStdlibApi
class RaceActivity : AppCompatActivity() {

    private val teams = ArrayList<Team>()

    private var timeAtPause: Long = 0
    private var isChronoStarted = false

    private lateinit var db: DBHelper
    private lateinit var race: Race

    private fun elapsedTime() = SystemClock.elapsedRealtime() - chrono.base

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
            val but = Button(this)
            but.text = "${team.name}\n${player.name}"
            teamButtonLayout.addView(but)
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
        }

        pauseButton.setOnClickListener {
            isChronoStarted = false
            chrono.stop()
            timeAtPause = elapsedTime()
            startButton.isEnabled = true
            startButton.isVisible = true
            pauseButton.isEnabled = false
            pauseButton.isVisible = false
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
