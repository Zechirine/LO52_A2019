package utbm.lo52.fail

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_dummy.*
import utbm.lo52.fail.db.DBHelper
import utbm.lo52.fail.db.Race

class DummyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummy)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            Log.v("INFO", "TEST")
            val helper = DBHelper(this, null)
            val races = helper.getAllRaces()
            for (race in races) {
                Log.v("COURSE", "${race.id}, ${race.name}, ${race.lap}")
            }
        }
    }

}
