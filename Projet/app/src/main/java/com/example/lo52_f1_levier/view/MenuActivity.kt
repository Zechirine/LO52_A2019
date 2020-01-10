package com.example.lo52_f1_levier.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lo52_f1_levier.R

import com.example.lo52_f1_levier.item.MenuContent
import kotlinx.android.synthetic.main.tab_list_content.view.*
import kotlinx.android.synthetic.main.tab_list.*

class MenuActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        if (tab_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }
        setupRecyclerView(tab_list)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, MenuContent.ITEMS, twoPane)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: MenuActivity,
        private val values: List<MenuContent.MainTab>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                parentActivity.appName.visibility = View.GONE
                parentActivity.appLogo.visibility = View.GONE
                // MenuContent initialise les "onglets" de base
                parentActivity.appName
                val item = v.tag as MenuContent.MainTab
                if (twoPane) {
                    // fragment par défault
                    var fragment : Fragment = Fragment()
                    // En fontion du nom de "l'onglet" selectionné, on affiche le bon fragment
                    when(item.content) {
                        "Participants" -> fragment = ParticipantFragment()
                        "Equipes" -> fragment = EquipeFragment()
                        "Courses" -> fragment = CourseFragment()
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.tab_detail_container, fragment)
                        .commit()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.menu_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.contentView.text = item.content

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val contentView: TextView = view.content
        }
    }
}
