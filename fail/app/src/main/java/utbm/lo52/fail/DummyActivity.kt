package utbm.lo52.fail

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_dummy.*
import utbm.lo52.fail.db.DBHelper
import utbm.lo52.fail.db.ForeignKey
import utbm.lo52.fail.db.Race
import utbm.lo52.fail.db.Team

class DummyActivity : AppCompatActivity() {

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummy)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            Log.v("INFO", "TEST")
            val helper = DBHelper(this, null)
            Log.v("SAVING", helper.save(Race(null, "ceci est un test", 5) as Race).toString())
            val race = helper.request(Race::class).get("id", 3) as Race
            Log.v("RACE", race.toString())
            race.lap = 19
            helper.save(race)
            val team = Team(null, "test", ForeignKey(Race::class, race.id))
            helper.save(team)
            val fetchedTeam = helper.request(Team::class).get("id", 1) as Team
            Log.v("TEAM", fetchedTeam.toString())
            Log.v("TESTONS", fetchedTeam.race.getRelated(helper).toString())
        }
    }

}
