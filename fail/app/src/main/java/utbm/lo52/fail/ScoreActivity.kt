package utbm.lo52.fail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import utbm.lo52.fail.db.DBHelper
import utbm.lo52.fail.db.Race

@ExperimentalStdlibApi
class ScoreActivity : AppCompatActivity() {

    private lateinit var race: Race
    private lateinit var db: DBHelper

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
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt("raceID", race.id!!)
    }
}
