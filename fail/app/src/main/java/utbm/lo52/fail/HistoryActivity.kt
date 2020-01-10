package utbm.lo52.fail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.widget_race.view.*
import utbm.lo52.fail.db.DBHelper
import utbm.lo52.fail.db.Race

@ExperimentalStdlibApi
class HistoryActivity : AppCompatActivity() {

    private val races = ArrayList<Race>()

    private lateinit var db: DBHelper
    private lateinit var raceAdapter: RaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        db = DBHelper(this, null)
        raceAdapter = RaceAdapter(this)
        ItemList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ItemList.adapter = raceAdapter
       
        races.addAll(db.request(Race::class).all() as List<Race>)
        raceAdapter.submitList(races)

        validateButton.hide()
        addElementButton.hide()
        cancelButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun goToResults(race: Race) {
        // Todo
    }
}

@UseExperimental(ExperimentalStdlibApi::class)
class RaceAdapter(private val activity: HistoryActivity) :
    ListAdapter<Race, RaceAdapter.RaceViewHolder>(RaceDiffCallback()) {

    @ExperimentalStdlibApi
    class RaceViewHolder(
        private val view: View,
        private val activity: HistoryActivity
    ) :
        RecyclerView.ViewHolder(view) {
        fun bind(race: Race) {
            view.raceNameTextView.text = race.name
            view.goToResultsButton.setOnClickListener {
                activity.goToResults(race)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceViewHolder {
        return RaceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.widget_race, parent, false), activity
        )
    }

    override fun onBindViewHolder(holder: RaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}


class RaceDiffCallback : DiffUtil.ItemCallback<Race>() {
    override fun areItemsTheSame(oldItem: Race, newItem: Race): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Race, newItem: Race): Boolean {
        return oldItem == newItem
    }
}
