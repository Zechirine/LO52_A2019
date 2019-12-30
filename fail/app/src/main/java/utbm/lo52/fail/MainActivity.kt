package utbm.lo52.fail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import utbm.lo52.fail.constants.LAP_PER_RACE
import utbm.lo52.fail.db.DBHelper
import utbm.lo52.fail.db.Race
import java.time.LocalDateTime

@ExperimentalStdlibApi
class MainActivity : AppCompatActivity() {

    private lateinit var db: DBHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DBHelper(this, null)
        beginRaceButton.setOnClickListener {
            var raceName = LocalDateTime.now().toString()
            while (db.request(Race::class).filter(
                    "name",
                    raceName
                ).filter("lap", LAP_PER_RACE).exists()
            ) {
                raceName += "bis"
            }
            val race = db.save(Race(null, raceName, LAP_PER_RACE))
            val intent = Intent(this, RegistrationActivity::class.java)
            intent.putExtra("raceID", race!!.id)
            startActivity(intent)
        }
    }
}
