package utbm.lo52.fail

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_race.*
import utbm.lo52.fail.constants.MIN_ORDERING
import utbm.lo52.fail.db.DBHelper
import utbm.lo52.fail.db.Player
import utbm.lo52.fail.db.Race
import utbm.lo52.fail.db.Team

@ExperimentalStdlibApi
class RaceActivity : AppCompatActivity() {

    private val teams = ArrayList<Team>()

    private lateinit var db: DBHelper
    private lateinit var race: Race

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_race)

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
        chrono.start()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt("raceID", race.id!!)
    }
}
