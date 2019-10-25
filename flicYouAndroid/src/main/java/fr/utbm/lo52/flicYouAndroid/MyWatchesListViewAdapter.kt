package fr.utbm.lo52.flicYouAndroid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.projet.MyWatch

class MyWatchesListViewAdapter(private val context: Context, private val myWatches: ArrayList<MyWatch>): BaseAdapter() {
    override fun getItem(position: Int): Any {
        return "TEST STRING"
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return myWatches.size
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val watchRowView = layoutInflater.inflate(R.layout.watch_row, viewGroup, false)

        val watchNameTextView = watchRowView.findViewById<View>(R.id.watchName) as TextView
        watchNameTextView.text = myWatches[position].getName()

        return watchRowView
    }
}