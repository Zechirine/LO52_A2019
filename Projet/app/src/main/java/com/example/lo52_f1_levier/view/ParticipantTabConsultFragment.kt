package com.example.lo52_f1_levier.view


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lo52_f1_levier.DAO.CoureurDao
import com.example.lo52_f1_levier.R
import com.example.lo52_f1_levier.model.Coureur
import com.example.lo52_f1_levier.model.Runner
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_participant_tab_consult.*
import kotlinx.android.synthetic.main.tab_list_content.*
import android.os.Build
import kotlinx.android.synthetic.main.fragment_participant.*
import android.app.Activity
import android.provider.BaseColumns


typealias MyRunnerClickListener = (View, Runner) -> Unit

class ParticipantTabConsultFragment : Fragment() {

    private lateinit var selectedRunner : Runner
    private lateinit var adapter: RunnerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_participant_tab_consult, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){

        // Initialize the dao
        val coureurDao = CoureurDao(this.context!!)
        val coureurs = ArrayList<Runner>()
        val cursor = coureurDao.getAllCoureur()

        // Get the list of runners to display
        with(cursor!!){
            while (moveToNext()){
                val runner = Runner(getInt(getColumnIndexOrThrow(BaseColumns._ID)),
                    getInt(getColumnIndexOrThrow(Coureur.CoureurTable.NUMC)),
                    getString(getColumnIndexOrThrow(Coureur.CoureurTable.CNAME)),
                    getString(getColumnIndexOrThrow(Coureur.CoureurTable.SURNAME)))
                coureurs.add(runner)
            }
        }

        adapter = RunnerAdapter(
            onClickListener = this::selectItem
        )
        adapter.replaceItems(coureurs)
        listParticipant.layoutManager = LinearLayoutManager(this.context)
        listParticipant.adapter = adapter
        editTextNbParticipant.setText(coureurs.size.toString())

        delete.setOnClickListener{
            if(::selectedRunner.isInitialized){
                coureurDao.deleteCoureur(selectedRunner.numc)

                val ft = fragmentManager!!.beginTransaction()
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false)
                }
                ft.detach(this).attach(this).commit() // Refresh the fragment
                editTextNbParticipant.setText((coureurs.size - 1).toString())
            }
            else
                Toast.makeText(this.context, "Vous devez sélectionner un Participant", Toast.LENGTH_SHORT).show()
        }

        add_participant.setOnClickListener{
            this.parentFragment?.tab_layout?.getTabAt(0)?.select() // Redirect to Tab 0
        }

        edit.setOnClickListener{

            // Redirect to the activity that allows to modifie the runner
            if(::selectedRunner.isInitialized) {
                val intent = Intent(this.context, ParticipantEditActivity::class.java)
                intent.putExtra("runnerId", selectedRunner.id)
                startActivityForResult(intent, 10001)

            }else
                Toast.makeText(this.context, "Vous devez sélectionner un Participant", Toast.LENGTH_SHORT).show()
        }
    }

    // Define the action when a runner of the list is selected
    private fun selectItem(view: View,runner: Runner) {
        selectedRunner = runner
        edt_selectedParticipant.setText(selectedRunner.cname + " " + selectedRunner.surname)
    }

    // Get the return from the edit activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10001 && resultCode == Activity.RESULT_OK) {
            // recreate your fragment here
            fragmentManager!!
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit()
            edt_selectedParticipant.text.clear()
        }
    }

    // Adapter for the recyclerView displaying the runners
    class RunnerAdapter(private val onClickListener: MyRunnerClickListener) : RecyclerView.Adapter<RunnerAdapter.ViewHolder>(){
        private var runners = ArrayList<Runner>()


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.tab_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val runner = runners[position]
            holder.content.text = runner.cname + " " + runner.surname
            holder.content.setOnClickListener { view ->
                onClickListener(view, runner)
            }
        }

        fun replaceItems(runner: ArrayList<Runner>) {
            this.runners = runner
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return runners.size
        }

        inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment ParticipantTabConsultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ParticipantTabConsultFragment().apply {
            }
    }
}