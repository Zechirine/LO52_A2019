package com.example.lo52_f1_levier.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lo52_f1_levier.DAO.CoureurDao
import com.example.lo52_f1_levier.DAO.EquipeDao
import com.example.lo52_f1_levier.DAO.ParticipeDao
import com.example.lo52_f1_levier.R
import com.example.lo52_f1_levier.model.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_team_edit.*
import kotlinx.android.synthetic.main.tab_list_content.content
import kotlinx.android.synthetic.main.team_member_list_content.*

typealias aAvailableRunnerClickListener = (View, Runner) -> Unit
typealias aTeamRunnerClickListener = (View, Runner) -> Unit

class TeamEditActivity : AppCompatActivity() {

    private lateinit var coureurDao : CoureurDao
    private lateinit var equipeDao : EquipeDao
    private lateinit var participeDao: ParticipeDao
    private lateinit var selectedRunner : Runner
    private lateinit var selectedMember : Runner
    private lateinit var adapter: RunnerAdapter
    private lateinit var teamAdapter: TeamMemberAdapter

    private var teamId : Int = -1
    private var courseId : Int = -1
    private var isOver = false // True if the course is over

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_edit)

        // Get the value sent from the intent
        teamId = intent.getIntExtra("teamId", -1)
        courseId = intent.getIntExtra("courseId", -1)
        isOver = intent.getBooleanExtra("isOver", false)

        // Instanciate the dao
        coureurDao = CoureurDao(this)
        equipeDao = EquipeDao(this)
        participeDao = ParticipeDao(this)

        save.setOnClickListener{
            if(isOver){
                Toast.makeText(this, "La course est terminée, aucune modification de possible", Toast.LENGTH_SHORT).show()
            }else{
                if(!edt_teamName.text.isEmpty()) {

                    // Modifie the team composition if the number of member is between 1 and 3
                    if (teamAdapter.getItemCount()>=1 && teamAdapter.getItemCount() <4 ) {
                        equipeDao = EquipeDao(this)
                        participeDao = ParticipeDao(this)
                        participeDao.deleteParticipeByCourseIdAndTeamId(courseId,teamId) // delete the old participant
                        equipeDao.updateEquipeByID(teamId,edt_teamName.text.toString(),edt_teamNumber.text.toString().toInt())

                        for (position in 0 until teamAdapter.itemCount) {
                            participeDao.insertParticipe(courseId, teamAdapter.getItem(position).id, teamId)
                            coureurDao.updateCoureurNumc(teamAdapter.getItem(position).id, position)
                        }

                        Toast.makeText(this, "Modification enregistrée", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    else{
                        Toast.makeText(this, "L'équipe doit avoir au moins 1 membre et pas plus de 3", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                    Toast.makeText(this, "Veuiller renseigner le nom de l'équipe", Toast.LENGTH_SHORT).show()
            }
        }

        cancel.setOnClickListener {
            this.finish()
        }

        // Move a team member to list of available runner
        btn_left.setOnClickListener{
            if(isOver){
                Toast.makeText(this, "La course est terminée, aucune modification de possible", Toast.LENGTH_SHORT).show()
            }else{
                if(::selectedMember.isInitialized){
                    if(selectedMember.isTeamMember){
                        selectedMember.isTeamMember = false
                        adapter.addItem(selectedMember)
                        teamAdapter.removeItem(selectedMember)


                        edt_selectedMember.text.clear()
                    }
                    else
                        Toast.makeText(this, "Vous devez sélectionner un membre de l'équipe", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(this, "Vous devez sélectionner un membre de l'équipe", Toast.LENGTH_SHORT).show()
            }
        }

        // Move an available runner to the list of team member
        btn_right.setOnClickListener{
            if(isOver){
                Toast.makeText(this, "La course est terminée, aucune modification de possible", Toast.LENGTH_SHORT).show()
            }else{
                if(::selectedRunner.isInitialized){
                    if(!selectedRunner.isTeamMember){
                        selectedRunner.isTeamMember = true
                        if(teamAdapter.addItem(selectedRunner))
                            adapter.removeItem(selectedRunner)
                        else{
                            selectedRunner.isTeamMember = false
                            teamMaxSize()
                        }
                        edt_selectedParticipant.text.clear()
                    }
                    else
                        Toast.makeText(this, "Vous devez sélectionner un Participant", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(this, "Vous devez sélectionner un Participant", Toast.LENGTH_SHORT).show()
            }
        }

        // Move a team member up
        btn_up.setOnClickListener{
            if(isOver){
                Toast.makeText(this, "La course est terminée, aucune modification de possible", Toast.LENGTH_SHORT).show()
            }else{
                if(::selectedMember.isInitialized){
                    if(selectedMember.isTeamMember){
                        if(teamAdapter.getItemPos(selectedMember) > 0)
                            teamAdapter.moveUp(selectedMember)
                    }
                    else
                        Toast.makeText(this, "Vous devez sélectionner un membre de l'équipe", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(this, "Vous devez sélectionner un membre de l'équipe", Toast.LENGTH_SHORT).show()
            }
        }

        // Move a team member down
        btn_down.setOnClickListener{
            if(isOver){
                Toast.makeText(this, "La course est terminée, aucune modification de possible", Toast.LENGTH_SHORT).show()
            }else{
                if(::selectedMember.isInitialized){
                    if(selectedMember.isTeamMember){
                        if(teamAdapter.getItemPos(selectedMember) < 2)
                            teamAdapter.moveDown(selectedMember)
                    }
                    else
                        Toast.makeText(this, "Vous devez sélectionner un membre de l'équipe", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(this, "Vous devez sélectionner un membre de l'équipe", Toast.LENGTH_SHORT).show()
            }
        }

        val coureurs = ArrayList<Runner>() // Will contain the list of available runners

        adapter = RunnerAdapter(
            onClickListener = this::selectItem
        )
        adapter.replaceItems(coureurs)
        listFreeParticipant.layoutManager = LinearLayoutManager(this)
        listFreeParticipant.adapter = adapter

        teamAdapter = TeamMemberAdapter(this::selectMember, this)
        listTeamMembers.layoutManager = LinearLayoutManager(this)
        listTeamMembers.adapter = teamAdapter

        if(teamId != -1 && courseId != -1){

            // Get the ids of the team members
            var res = participeDao.getParticipeByC_ID_E_ID(courseId,teamId)
            var listIdTeamMember = ArrayList<Int>() // list of the team members id
            with(res!!){
                while (moveToNext()){
                    listIdTeamMember.add(getInt(getColumnIndexOrThrow(Participe.ParticipeTable.COUREUR_ID)))
                }
            }
            // Get the data of the team
            res = equipeDao.getEquipeByID(teamId)
            with(res!!){
                var team : Team
                while (moveToNext()){
                    team = Team(
                        teamId,
                        getString(getColumnIndexOrThrow(Equipe.EquipeTable.ENAME)),
                        getInt(getColumnIndexOrThrow(Equipe.EquipeTable.ENUM))
                    )
                    edt_teamName.setText(team.name)
                    edt_teamNumber.setText(team.number.toString())
                }
            }

            // Get the team members
            var teamRunners = ArrayList<Runner>()
            listIdTeamMember.forEach{
                res = coureurDao.getCoureurByID(it)
                with(res!!){
                    while (moveToNext()){
                        var teamMember = Runner(
                            it,
                            getInt(getColumnIndexOrThrow(Coureur.CoureurTable.NUMC)),
                            getString(getColumnIndexOrThrow(Coureur.CoureurTable.CNAME)),
                            getString(getColumnIndexOrThrow(Coureur.CoureurTable.SURNAME)))
                        teamMember.isTeamMember = true
                        teamRunners.add(teamMember)
                    }
                }
            }

            // Add the team members in the recyclerView
            teamAdapter.replaceItems(teamRunners)
            res = coureurDao.getCoureurFree(courseId)
            with(res!!){
                coureurs.clear()
                while (moveToNext()){
                    val freeRunner = Runner(
                        getInt(getColumnIndexOrThrow(BaseColumns._ID)),
                        getInt(getColumnIndexOrThrow(Coureur.CoureurTable.NUMC)),
                        getString(getColumnIndexOrThrow(Coureur.CoureurTable.CNAME)),
                        getString(getColumnIndexOrThrow(Coureur.CoureurTable.SURNAME))
                    )
                    coureurs.add(freeRunner)
                }
                adapter.replaceItems(coureurs)
            }


        }
    }

    // define the action when an item of list of availaible runners is selected
    private fun selectItem(view: View,runner: Runner) {
        selectedRunner = runner
        edt_selectedParticipant.setText(selectedRunner.cname + " " + selectedRunner.surname)
    }

    // define the action when an item of list of team members is selected
    private fun selectMember(view: View,runner: Runner){
        selectedMember = runner
        edt_selectedMember.setText(selectedMember.cname + " " + selectedMember.surname)
    }

    fun teamMaxSize(){
        Toast.makeText(this, "Taille maximum de l'équipe atteinte", Toast.LENGTH_SHORT).show()
    }

    // Adapter for the recyclerView displaying the availaible runners
    class RunnerAdapter(private val onClickListener: aAvailableRunnerClickListener) : RecyclerView.Adapter<RunnerAdapter.ViewHolder>(){
        private var runners = ArrayList<Runner>()
        private var selectedPos = RecyclerView.NO_POSITION


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

        fun addItem(runner: Runner){
            this.runners.add(runner)
            notifyDataSetChanged()
        }

        fun removeItem(runner: Runner){
            this.runners.remove(runner)
            notifyDataSetChanged()

        }

        override fun getItemCount(): Int {
            return runners.size
        }

        inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer
    }

    // Adapter for the recyclerView displaying the team members
    class TeamMemberAdapter(private val onClickListener: aTeamRunnerClickListener, private val context: Context?) : RecyclerView.Adapter<TeamMemberAdapter.ViewHolder>(){

        private var members = ArrayList<Runner>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.team_member_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val member = members[position]
            holder.content.text = member.cname + " " + member.surname
            holder.content.setOnClickListener { view ->
                onClickListener(view, member)
            }

            // Define the color of the member according to their position
            when(position){
                0 -> holder.imageView.setBackgroundColor(context!!.resources.getColor(R.color.runner1))
                1 -> holder.imageView.setBackgroundColor(context!!.resources.getColor(R.color.runner2))
                2 -> holder.imageView.setBackgroundColor(context!!.resources.getColor(R.color.runner3))
            }
        }

        fun replaceItems(runner: ArrayList<Runner>) {
            this.members = runner
            notifyDataSetChanged()
        }

        fun addItem(runner: Runner) : Boolean{
            if(members.size < 3){
                this.members.add(runner)
                notifyDataSetChanged()
                return true
            }
            else
                return false
        }

        fun moveUp(runner: Runner){
            var index = this.members.indexOf(runner) - 1
            var buffer = this.members[index]
            this.members[index] = this.members[this.members.indexOf(runner)]
            this.members[index + 1] = buffer
            notifyDataSetChanged()

        }

        fun moveDown(runner: Runner){
            var index = this.members.indexOf(runner) + 1

            if(this.members.size > index){
                var buffer = this.members[index]
                this.members[index] = this.members[this.members.indexOf(runner)]
                this.members[index - 1] = buffer
                notifyDataSetChanged()
            }
        }

        fun getItemPos(runner: Runner): Int{
            return this.members.indexOf(runner)
        }

        fun getItem(position : Int): Runner{
            return this.members[position]
        }

        fun removeItem(runner: Runner){
            this.members.remove(runner)
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return members.size
        }

        inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer
    }
}
