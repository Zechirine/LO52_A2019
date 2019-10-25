package fr.utbm.lo52.flicYouWear

import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import fr.utbm.lo52.flicYouWear.R
import fr.utbm.lo52.flicYouWear.MainActivity
import kotlinx.android.synthetic.main.activity_urgence.*

class UrgenceActivity: WearableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urgence)

        endUrgenceButton.setOnClickListener {
            returnToMainActivity()
        }
    }

    private fun returnToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}